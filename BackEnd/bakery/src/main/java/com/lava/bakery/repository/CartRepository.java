package com.lava.bakery.repository;

import com.lava.bakery.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart,Long>{

    List<Cart> findByUserEmail(String userEmail);

    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cake.id = :cakeId")
    void deleteByCakeId(@Param("cakeId") Long cakeId);

}