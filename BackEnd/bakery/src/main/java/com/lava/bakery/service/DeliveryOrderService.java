package com.lava.bakery.service;

import com.lava.bakery.dto.DeliveryOrderDTO;
import com.lava.bakery.entity.DeliveryOrder;
import com.lava.bakery.entity.Order;
import com.lava.bakery.repository.DeliveryOrderRepository;
import com.lava.bakery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class DeliveryOrderService {

    @Autowired
    private DeliveryOrderRepository repo;

    @Autowired
    private OrderRepository orderRepo;

    //  SAVE WHEN DELIVERED
    public void saveDelivery(Long orderId, Long deliveryBoyId){

        DeliveryOrder d = new DeliveryOrder();

        d.setOrderId(orderId);
        d.setDeliveryBoyId(deliveryBoyId);
        d.setDeliveredAt(LocalDateTime.now());

        repo.save(d);
    }

    //  HISTORY
    public List<DeliveryOrderDTO> getByDeliveryBoy(Long id){

        List<DeliveryOrder> list = repo.findByDeliveryBoyId(id);

        return list.stream()
                .map(d -> {

                    Order o = orderRepo.findById(d.getOrderId()).orElse(null);

                    if(o == null) return null;
                    return new DeliveryOrderDTO(
                            o.getId(),
                            o.getUserEmail(),
                            o.getDeliveryAddress(),
                            o.getTotalPrice(),
                            d.getDeliveredAt(),
                            o.getCakePrice(),
                            o.getDeliveryCharge()

                    );

                })
                .filter(dto -> dto != null)
                .toList();
    }
}