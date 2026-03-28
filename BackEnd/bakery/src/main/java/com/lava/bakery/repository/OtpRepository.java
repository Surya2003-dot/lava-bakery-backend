package com.lava.bakery.repository;

import com.lava.bakery.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification,Long> {

    Optional<OtpVerification> findTopByEmailOrderByIdDesc(String email);

}
