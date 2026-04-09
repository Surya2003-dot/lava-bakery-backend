package com.lava.bakery.repository;

import com.lava.bakery.entity.BannerImage;
import com.lava.bakery.entity.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerImageRepository extends JpaRepository<BannerImage, Long> {

    List<BannerImage> findByTypeAndActiveTrueOrderByPositionAsc(BannerType type);

}