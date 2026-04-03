package com.lava.bakery.repository;

import com.lava.bakery.entity.Cake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CakeRepository extends JpaRepository<Cake, Long> {

    Page<Cake> findByAvailableTrue(Pageable pageable);

    Page<Cake> findByAvailableTrueAndCategory(
            String category, Pageable pageable);

    Page<Cake> findByAvailableTrueAndFlavour(
            String flavour, Pageable pageable);

    Page<Cake> findByAvailableTrueAndCategoryAndFlavour(
            String category, String flavour, Pageable pageable);

    Page<Cake> findByAvailableTrueAndPricePerKgBetween(
            double minPrice, double maxPrice, Pageable pageable);

    Page<Cake> findByAvailableTrueAndCategoryAndFlavourAndPricePerKgBetween(
            String category, String flavour,
            double minPrice, double maxPrice, Pageable pageable);

    Page<Cake> findByAvailableTrueAndNameContainingIgnoreCase(
            String name, Pageable pageable);

    @Query("""
    SELECT c FROM Cake c
    WHERE c.available = true
    AND REPLACE(LOWER(c.name),' ','')
    LIKE CONCAT('%', :name, '%')
    """)
    Page<Cake> searchByName(
            @Param("name") String name,
            Pageable pageable);

    @Query("""
    SELECT c FROM Cake c
    WHERE c.available = true
    AND REPLACE(LOWER(c.name),' ','')
    LIKE CONCAT('%', :name, '%')
    AND c.pricePerKg <= :maxPrice
    """)
    Page<Cake> searchByNameAndPrice(
            @Param("name") String name,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    // ✅ FIXED - added :category and :flavour to query
    @Query("""
    SELECT c FROM Cake c
    WHERE REPLACE(LOWER(c.name),' ','')
    LIKE CONCAT('%', REPLACE(LOWER(:name),' ',''), '%')
    AND (:category IS NULL OR c.category = :category)
    AND (:flavour IS NULL OR c.flavour = :flavour)
    AND (:maxPrice IS NULL OR c.pricePerKg <= :maxPrice)
    AND (:minPrice IS NULL OR c.pricePerKg >= :minPrice)
    """)
    Page<Cake> searchCakes(
            @Param("name") String name,
            @Param("category") String category,
            @Param("flavour") String flavour,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    Page<Cake> findByAvailableTrueAndPricePerKgLessThanEqual(
            Double maxPrice, Pageable pageable);

    Page<Cake> findByAvailableTrueAndPricePerKgGreaterThanEqual(
            Double minPrice, Pageable pageable);

    Page<Cake> findByAvailableTrueAndNameContainingIgnoreCaseAndPricePerKgLessThanEqual(
            String name, Double maxPrice, Pageable pageable);

    // ✅ FIXED - added @Param annotations
    @Query("""
    SELECT c FROM Cake c
    WHERE c.available = true
    AND REPLACE(LOWER(c.name),' ','')
    LIKE CONCAT('%', REPLACE(LOWER(:name),' ',''), '%')
    AND (:minPrice IS NULL OR c.pricePerKg >= :minPrice)
    AND (:maxPrice IS NULL OR c.pricePerKg <= :maxPrice)
    """)
    Page<Cake> smartSearch(
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    List<Cake> findByAvailableTrueAndFeaturedTrue();

    List<Cake> findByFlavourAndAvailable(String flavour, boolean available);

    List<Cake> findByAvailable(boolean available);

    List<Cake> findByAvailableTrueAndFlavour(String flavour);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cake.id = :cakeId")
    void deleteByCakeId(@Param("cakeId") Long cakeId);
}