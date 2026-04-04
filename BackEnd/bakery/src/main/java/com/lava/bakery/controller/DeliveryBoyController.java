package com.lava.bakery.controller;

import com.lava.bakery.dto.DeliveryLoginRequest;
import com.lava.bakery.dto.DeliveryOrderDTO;
import com.lava.bakery.dto.LoginRequest;
import com.lava.bakery.entity.DeliveryBoy;
import com.lava.bakery.service.DeliveryBoyService;
import com.lava.bakery.service.DeliveryOrderService;
import com.lava.bakery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lava.bakery.security.JwtUtil;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api")
public class DeliveryBoyController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DeliveryBoyService service;
    @Autowired
    private DeliveryOrderService deliveryOrderService;
    @Autowired
    private OrderService orderService;
    //  Admin create
    @PostMapping("/admin/delivery")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DeliveryBoy add(@RequestBody DeliveryBoy d){
        return service.add(d);
    }

    //  Admin view
    @GetMapping("/admin/delivery")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY')")
    public List<DeliveryBoy> getAll(){
        return service.getAll();
    }

    //  Delete
    @DeleteMapping("/admin/delivery/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    // Update
    @PutMapping("/admin/delivery/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY')")
    public DeliveryBoy update(@PathVariable Long id,
                              @RequestBody DeliveryBoy d){
        return service.update(id, d);
    }

    //  Login
    @PostMapping("/delivery/login")
    public ResponseEntity<?> login(@RequestBody DeliveryLoginRequest request){

        DeliveryBoy boy = service.login(
                request.getUsername(),
                request.getPassword()
        );

        if(boy == null){
            return ResponseEntity.status(401).body("Invalid username or password ❌");
        }

        String token = jwtUtil.generateToken(boy.getUsername(), "ROLE_DELIVERY");

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", boy.getId()
        ));
    }
    @GetMapping("/admin/delivery/{id}/history")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<DeliveryOrderDTO> getHistory(@PathVariable Long id){
        return deliveryOrderService.getByDeliveryBoy(id);
    }
    @PostMapping("/delivery/save")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY')")
    public ResponseEntity<?> saveDelivery(@RequestParam Long orderId,
                                          @RequestParam Long deliveryBoyId){

        deliveryOrderService.saveDelivery(orderId, deliveryBoyId);
        return ResponseEntity.ok("Saved");
    }
    @PutMapping("/delivery/out-for-delivery/{orderId}/{deliveryBoyId}")
    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY')")
    public ResponseEntity<?> outForDelivery(@PathVariable Long orderId,
                                            @PathVariable Long deliveryBoyId){

        return ResponseEntity.ok(
                orderService.markOutForDelivery(orderId, deliveryBoyId)
        );
    }
}