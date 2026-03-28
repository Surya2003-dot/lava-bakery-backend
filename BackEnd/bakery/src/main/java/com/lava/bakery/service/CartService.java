package com.lava.bakery.service;

import com.lava.bakery.dto.CartResponseDTO;
import com.lava.bakery.entity.Cart;
import com.lava.bakery.entity.Cake;
import com.lava.bakery.repository.CartRepository;
import com.lava.bakery.repository.CakeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CakeRepository cakeRepository;

    public CartService(CartRepository cartRepository, CakeRepository cakeRepository){
        this.cartRepository = cartRepository;
        this.cakeRepository = cakeRepository;
    }

    //  Add to cart with weight
    public void addToCart(Long cakeId, double weight){

        String email =
                SecurityContextHolder.getContext().getAuthentication().getName();

        Cake cake = cakeRepository.findById(cakeId).orElseThrow();

        Cart cart = new Cart();

        cart.setUserEmail(email);
        cart.setCake(cake);
        cart.setWeight(weight);

        double price = cake.getPricePerKg() * weight;

        cart.setPrice(price);
        cart.setQuantity(1);

        cartRepository.save(cart);
    }

    //  Get cart items
    public List<CartResponseDTO> getCart(){

        String email =
                SecurityContextHolder.getContext().getAuthentication().getName();

        return cartRepository.findByUserEmail(email)
                .stream()
                .map(c -> new CartResponseDTO(

                        c.getId(),
                        c.getCake().getId(),
                        c.getCake().getName(),
                        c.getWeight(),
                        c.getPrice(),
                        c.getCake().getImageUrl(),
                        c.getCake().getRating(),
                        c.getCake().getReviewCount(),
                        c.getCake().getShapes(),
                        c.getCake().getBadge(),
                        c.getCake().getPreparationTimeHours()



                ))
                .collect(Collectors.toList());
    }

    //  Remove item
    public void removeCart(Long id){
        cartRepository.deleteById(id);
    }

}