package com.lava.bakery.controller;

import com.lava.bakery.dto.DashboardResponseDTO;
import com.lava.bakery.dto.OrderResponseDTO;
import com.lava.bakery.entity.OrderStatus;
import com.lava.bakery.entity.User;
import com.lava.bakery.service.CakeService;
import com.lava.bakery.service.OrderService;
import com.lava.bakery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CakeService cakeService;

    @Autowired
    private UserService userService;
    @GetMapping("/dashboard")
    public DashboardResponseDTO getDashboard(
            @RequestParam(defaultValue = "today") String filter,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to){

        return orderService.getDashboardData(filter, from, to);
    }
    @GetMapping("/orders")
    public Page<OrderResponseDTO> getAllOrders(

            @RequestParam(defaultValue = "all") String date,
            @RequestParam(defaultValue = "all") String status,

            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        return orderService.getFilteredOrders(date, status, from, to, page, size, sortBy, direction);
    }
    @PutMapping("/orders/{orderId}/status")
    public OrderResponseDTO updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status){

        return orderService.updateOrderStatusDTO(orderId,status);
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

}