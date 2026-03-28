package com.lava.bakery.controller;

import com.lava.bakery.dto.DashboardResponseDTO;
import com.lava.bakery.dto.OrderResponseDTO;
import com.lava.bakery.entity.OrderStatus;
import com.lava.bakery.entity.PaymentMethod;
import com.lava.bakery.service.CakeService;
import com.lava.bakery.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CakeService cakeService;

    public OrderController(OrderService orderService, CakeService cakeService) {
        this.orderService = orderService;
        this.cakeService = cakeService;
    }

    // =====================================
    // USER - Place Order
    // =====================================
    @PostMapping("/place")
    public OrderResponseDTO placeOrder(
            @RequestParam Long cakeId,
            @RequestParam double kg,
            @RequestParam String deliveryAddress,
            @RequestParam String phoneNumber,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam String eggType,
            @RequestParam String cakeMessage,
            @RequestParam String deliveryDate,
            @RequestParam String deliverySlot,
            @RequestParam String cakeShape,
            @RequestParam double cakePrice,
            @RequestParam double deliveryCharge
    ) {

        return orderService.placeOrderDTO(
                cakeId,
                kg,
                deliveryAddress,
                phoneNumber,
                paymentMethod,
                eggType,
                cakeMessage,
                LocalDate.parse(deliveryDate),
                deliverySlot,
                cakeShape,
                cakePrice,
                deliveryCharge
        );
    }
    // =====================================
    // USER - View Own Orders (Pagination + Sorting)
    // =====================================
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public Page<OrderResponseDTO> myOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return orderService.getMyOrders(page, size, sortBy, direction);
    }

    // =====================================
    // USER - Cancel Own Order
    // =====================================
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long id) {
        return orderService.cancelMyOrder(id);
    }

    // =====================================
    // ADMIN - View All Orders (Pagination + Sorting)
    // =====================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public Page<OrderResponseDTO> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return orderService.getAllOrdersDTO(page, size, sortBy, direction);
    }

    // =====================================
    // ADMIN - Update Order Status
    // =====================================
//    @PreAuthorize("hasRole('ADMIN','DELIVERY')")
    @PutMapping("/{id}/status")
    public OrderResponseDTO updateStatus(@PathVariable Long id,
                                         @RequestParam OrderStatus status) {
        return orderService.updateOrderStatusDTO(id, status);
    }

    // =====================================
    // ADMIN - Dashboard Analytics
    // =====================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public DashboardResponseDTO getDashboard(
            @RequestParam(defaultValue = "today") String filter,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to){

        return orderService.getDashboardData(filter, from, to);
    }
    // USER - Pay for Order
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/pay")
    public OrderResponseDTO payOrder(@PathVariable Long id) {
        return orderService.payForOrder(id);
    }

    @GetMapping("/my-orders")
    public List<OrderResponseDTO> getMyOrders(Authentication authentication) {

        String email = authentication.getName();

        return orderService.getOrdersByUser(email);

    }
    @GetMapping("/pending-count")
    public Map<String,Integer> getPendingCount(Authentication authentication){

        String email = authentication.getName();

        int count = orderService.getPendingCountByEmail(email);

        return Map.of("count",count);
    }


    @GetMapping("/delivery-slots")
    public Map<String, List<String>> getSlots(@RequestParam int prepTime){
        return cakeService.getDeliverySlots(prepTime);
    }
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }
}