package com.lava.bakery.controller;

import com.lava.bakery.dto.CartResponseDTO;
import com.lava.bakery.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    //  Add to cart (weight optional)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public void addToCart(
            @RequestParam Long cakeId,
            @RequestParam(required = false) Double weight){

        if(weight == null){
            weight = 1.0;
        }

        cartService.addToCart(cakeId, weight);
    }

    //  Get cart items
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CartResponseDTO> getCart(){
        return cartService.getCart();
    }

    // Remove cart item
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void removeCart(@PathVariable Long id){
        cartService.removeCart(id);
    }

}