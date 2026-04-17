package com.lava.bakery.service;

import com.lava.bakery.dto.DashboardResponseDTO;
import com.lava.bakery.dto.OrderResponseDTO;
import com.lava.bakery.entity.*;
import com.lava.bakery.exception.BusinessException;
import com.lava.bakery.repository.CakeRepository;
import com.lava.bakery.repository.OrderRepository;
import com.lava.bakery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private DeliveryBoyService deliveryBoyService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CakeRepository cakeRepository;
    private final TelegramService telegramService;

    public OrderService(UserRepository userRepository,
                        OrderRepository orderRepository,
                        CakeRepository cakeRepository,
                        TelegramService telegramService) {

        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cakeRepository = cakeRepository;
        this.telegramService = telegramService;
    }
    // =========================================
    // USER - Place Order
    // =========================================
    public OrderResponseDTO placeOrderDTO(
            Long cakeId,
            double orderedKg,
            String deliveryAddress,
            String phoneNumber,
            PaymentMethod paymentMethod,
            String eggType,
            String cakeMessage,
            LocalDate deliveryDate,
            String deliverySlot,
            String cakeShape,
            double cakePrice,
            double deliveryCharge) {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("User not found"));

        Cake cake = cakeRepository.findById(cakeId)
                .orElseThrow(() -> new BusinessException("Cake not found"));

        if (!cake.isAvailable()) {
            throw new BusinessException("Cake not available");
        }

        if (orderedKg <= 0 ||
                orderedKg < cake.getMinOrderKg() ||
                orderedKg > cake.getMaxOrderKg()) {
            throw new BusinessException("Invalid order weight");
        }

        if (orderedKg > cake.getDailyCapacityKg()) {
            throw new BusinessException("Not enough capacity available today");
        }

        double totalPrice = cakePrice + deliveryCharge;


        cake.setDailyCapacityKg(
                cake.getDailyCapacityKg() - orderedKg
        );
        cakeRepository.save(cake);

        String shape = cake.getShapes();

        Order order = new Order(
                userEmail,
                user,
                cake,
                orderedKg,
                totalPrice,
                OrderStatus.PREPARING,
                paymentMethod,
                deliveryAddress,
                phoneNumber,
                eggType,
                cakeMessage,
                deliveryDate,
                deliverySlot,
                shape,
                cakePrice,
                deliveryCharge
        );

        order.setCustomerName(user.getName());

//        return convertToDTO(orderRepository.save(order));    changee for tele mssg
        Order savedOrder = orderRepository.save(order);

// TELEGRAM MESSAGE
        String msg = "🍰 New Order\n\n"
                + "🆔 Order ID: " + savedOrder.getId()
                + "\n👤 Customer: " + savedOrder.getCustomerName()
                + "\n📧 Email: " + savedOrder.getUserEmail()
                + "\n🎂 Cake: " + savedOrder.getCake().getName()
                + "\n⚖ Kg: " + savedOrder.getOrderedKg()
                + "\n📅 Date: " + savedOrder.getDeliveryDate()
                + "\n⏰ Slot: " + savedOrder.getDeliverySlot()
                + "\n📞 Phone: " + savedOrder.getPhoneNumber()
                + "\n💬 Message: " + savedOrder.getCakeMessage()
                + "\n🎨 Shape: " + savedOrder.getCakeShape()
                + "\n💰 Total: ₹" + savedOrder.getTotalPrice();

// ⚠ encode (important)
//        msg = java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);

// 🔥 SEND
        telegramService.sendMessage(msg);

// ✅ return
        return convertToDTO(savedOrder);
    }
    // =========================================
    // USER - Pay For Order
    // =========================================
    public OrderResponseDTO payForOrder(Long orderId) {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getUserEmail().equals(userEmail)) {
            throw new BusinessException("You cannot pay for this order");
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("Payment not allowed for this order");
        }

        order.setStatus(OrderStatus.PAID);

        return convertToDTO(orderRepository.save(order));
    }

    // =========================================
    // USER - Cancel Own Order
    // =========================================
    public OrderResponseDTO cancelMyOrder(Long orderId) {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if (!order.getUserEmail().equals(userEmail)) {
            throw new BusinessException("You are not allowed to cancel this order");
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException("Completed orders cannot be cancelled");
        }

        Cake cake = order.getCake();

        cake.setDailyCapacityKg(
                cake.getDailyCapacityKg() + order.getOrderedKg()
        );

        cakeRepository.save(cake);

        order.setStatus(OrderStatus.CANCELLED);

        return convertToDTO(orderRepository.save(order));
    }

    // =========================================
    // ADMIN - View All Orders
    // =========================================
    public Page<OrderResponseDTO> getAllOrdersDTO(int page,
                                                  int size,
                                                  String sortBy,
                                                  String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository
                .findAll(pageable)
                .map(this::convertToDTO);
    }

    // =========================================
    // USER - View Own Orders
    // =========================================
    public Page<OrderResponseDTO> getMyOrders(int page,
                                              int size,
                                              String sortBy,
                                              String direction) {

        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository
                .findByUserEmail(userEmail, pageable)
                .map(this::convertToDTO);
    }

    // =========================================
    // ADMIN - Update Order Status
    // =========================================
    public OrderResponseDTO updateOrderStatusDTO(Long orderId,
                                                 OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        order.setStatus(newStatus);

        return convertToDTO(orderRepository.save(order));
    }// =========================================
    // ADMIN - Dashboard Stats
    // =========================================
    public DashboardResponseDTO getDashboardData(String filter, String from, String to){

        List<Order> orders = orderRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        // ===== CUSTOM =====
        if("custom".equals(filter) && from != null && !from.isEmpty() && to != null && !to.isEmpty()){

            LocalDateTime fromDate = LocalDate.parse(from).atStartOfDay();
            LocalDateTime toDate = LocalDate.parse(to).atTime(23,59,59);

            orders = orders.stream()
                    .filter(o -> !o.getOrderDate().isBefore(fromDate)
                            && !o.getOrderDate().isAfter(toDate))
                    .toList();
        }

        // ===== TODAY =====
        else if("today".equals(filter)){
            orders = orders.stream()
                    .filter(o -> o.getOrderDate().toLocalDate().equals(now.toLocalDate()))
                    .toList();
        }

        // ===== WEEK =====
        else if("week".equals(filter)){
            orders = orders.stream()
                    .filter(o -> o.getOrderDate().isAfter(now.minusDays(7)))
                    .toList();
        }

        // ===== MONTH =====
        else if("month".equals(filter)){
            orders = orders.stream()
                    .filter(o -> o.getOrderDate().isAfter(now.minusDays(30)))
                    .toList();
        }

        // ===== ALL (DEFAULT) =====
        // if "all" or anything else → no filtering

        long totalOrders = orders.size();

        long pending = orders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING_PAYMENT).count();
        long preparing = orders.stream().filter(o -> o.getStatus() == OrderStatus.PREPARING).count();
        long outForDelivery = orders.stream().filter(o -> o.getStatus() == OrderStatus.OUT_FOR_DELIVERY).count();
        long delivered = orders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long completed = orders.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).count();
        long cancelled = orders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        double revenue = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED
                        || o.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotalPrice)
                .sum();

        return new DashboardResponseDTO(
                totalOrders,
                pending,
                0,
                preparing,
                outForDelivery,
                delivered,
                completed,
                cancelled,
                revenue
        );
    }
    // =========================================
    // DTO Mapper
    // =========================================
    private OrderResponseDTO convertToDTO(Order order) {
        String customerName = order.getCustomerName() != null
                ? order.getCustomerName()
                : order.getUserEmail();
        String cakeName = order.getCake() != null ? order.getCake().getName() : "N/A";
        String imageUrl = order.getCake() != null ? order.getCake().getImageUrl() : "";

        String status = order.getStatus() != null
                ? order.getStatus().name()
                : "N/A";

        String paymentMethod = order.getPaymentMethod() != null
                ? order.getPaymentMethod().name()
                : "N/A";

        return new OrderResponseDTO(
                order.getId(),
                order.getCake() != null ? order.getCake().getId() : null,
                cakeName,
                order.getOrderedKg(),
                order.getTotalPrice(),
                status,
                paymentMethod,
                order.getDeliveryAddress(),
                order.getPhoneNumber(),
                order.getOrderDate(),
                order.getEggType(),
                order.getDeliveryDate(),
                order.getDeliverySlot(),
                imageUrl,
                order.getCakeMessage(),
                order.getCakeShape(),
                customerName,
                order.getCakePrice(),
                order.getDeliveryCharge(),
                order.getDeliveryBoyId(),
                order.getDeliveryBoyName(),
                order.getDeliveryBoyPhone()
        );
    }
    public long getPendingOrderCount(){

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return orderRepository
                .countByUserEmailAndStatusNot(email, OrderStatus.COMPLETED);
    }
    public List<OrderResponseDTO> getOrdersByUser(String email) {

        List<Order> orders = orderRepository.findByUserEmail(email);

        return orders.stream()
                .map(this::convertToDTO)
                .toList();
    }
    public int getPendingCountByEmail(String email){

        return orderRepository
                .countByUserEmailAndStatusNot(email, OrderStatus.DELIVERED);

    }
    public Page<OrderResponseDTO> getFilteredOrders(String date, String status,
                                                    String from, String to,
                                                    int page, int size,
                                                    String sortBy, String direction){

        List<Order> orders = orderRepository.findAll();

        LocalDateTime now = LocalDateTime.now();

        // ===== CUSTOM DATE FILTER  =====
        if(date.equals("custom") && from != null && to != null){

            LocalDateTime fromDate = LocalDate.parse(from).atStartOfDay();
            LocalDateTime toDate = LocalDate.parse(to).atTime(23,59,59);

            orders = orders.stream()
                    .filter(o -> !o.getOrderDate().isBefore(fromDate)
                            && !o.getOrderDate().isAfter(toDate))
                    .toList();
        }

        // ===== NORMAL DATE FILTER =====
        else if(date.equals("today")){
            orders = orders.stream()
                    .filter(o -> o.getOrderDate().toLocalDate().equals(now.toLocalDate()))
                    .toList();
        }
        else if(date.equals("week")){
            orders = orders.stream()
                    .filter(o -> o.getOrderDate().isAfter(now.minusDays(7)))
                    .toList();
        }
        else if(date.equals("month")){
            orders = orders.stream()
                    .filter(o -> o.getOrderDate().isAfter(now.minusDays(30)))
                    .toList();
        }

        // ===== STATUS =====
        if(!status.equals("all")){
            orders = orders.stream()
                    .filter(o -> o.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        // ===== SORT =====
        if(direction.equalsIgnoreCase("desc")){
            orders = orders.stream()
                    .sorted((a,b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                    .toList();
        } else {
            orders = orders.stream()
                    .sorted((a,b) -> a.getOrderDate().compareTo(b.getOrderDate()))
                    .toList();
        }

        // ===== PAGINATION =====
        int start = page * size;

        if(start > orders.size()){
            start = 0;
        }

        int end = Math.min(start + size, orders.size());

        List<OrderResponseDTO> dtoList = orders.subList(start, end)
                .stream()
                .map(this::convertToDTO)
                .toList();

        return new PageImpl<>(dtoList, PageRequest.of(page, size), orders.size());
    }
    public OrderResponseDTO getOrderById(Long id){

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Order not found"));

        return convertToDTO(order);
    }
    public OrderResponseDTO markOutForDelivery(Long orderId, Long deliveryBoyId) {

        DeliveryBoy deliveryBoy = deliveryBoyService.getById(deliveryBoyId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        if(order.getDeliveryBoyId() != null &&
                !order.getDeliveryBoyId().equals(deliveryBoyId)){
            throw new BusinessException("Order already taken by another delivery boy");
        }

        order.setDeliveryBoyId(deliveryBoy.getId());
        order.setDeliveryBoyName(deliveryBoy.getName());
        order.setDeliveryBoyPhone(deliveryBoy.getPhone());
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        return convertToDTO(orderRepository.save(order));
    }
}