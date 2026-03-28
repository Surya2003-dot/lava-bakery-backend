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

    // 1️ Only available cakes
    Page<Cake> findByAvailableTrue(Pageable pageable);

    // 2 Available + Category
    Page<Cake> findByAvailableTrueAndCategory(
            String category,
            Pageable pageable
    );

    // 3️ Available + Flavour
    Page<Cake> findByAvailableTrueAndFlavour(
            String flavour,
            Pageable pageable
    );

    // 4️ Available + Category + Flavour
    Page<Cake> findByAvailableTrueAndCategoryAndFlavour(
            String category,
            String flavour,
            Pageable pageable
    );

    // 5️ Available + Price Range
    Page<Cake> findByAvailableTrueAndPricePerKgBetween(
            double minPrice,
            double maxPrice,
            Pageable pageable
    );

    // 6️ Available + Category + Flavour + Price Range (FULL COMBO)
    Page<Cake> findByAvailableTrueAndCategoryAndFlavourAndPricePerKgBetween(
            String category,
            String flavour,
            double minPrice,
            double maxPrice,
            Pageable pageable
    );
    Page<Cake> findByAvailableTrueAndNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
    @Query("""
SELECT c FROM Cake c
WHERE c.available = true
AND REPLACE(LOWER(c.name),' ','')
LIKE CONCAT('%', :name, '%')
""")
    Page<Cake> searchByName(
            @Param("name") String name,
            Pageable pageable
    );
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
            Pageable pageable
    );
    @Query("""
SELECT c FROM Cake c
WHERE REPLACE(LOWER(c.name),' ','') 
LIKE CONCAT('%', REPLACE(LOWER(:name),' ',''), '%')
AND (:maxPrice IS NULL OR c.pricePerKg <= :maxPrice)
AND (:minPrice IS NULL OR c.pricePerKg >= :minPrice)
""")
    Page<Cake> searchCakes(
            String name,
            String category,
            String flavour,
            Double minPrice,
            Double maxPrice,
            Pageable pageable);
    Page<Cake> findByAvailableTrueAndPricePerKgLessThanEqual(
            Double maxPrice,
            Pageable pageable
    );

    Page<Cake> findByAvailableTrueAndPricePerKgGreaterThanEqual(
            Double minPrice,
            Pageable pageable
    );
    Page<Cake> findByAvailableTrueAndNameContainingIgnoreCaseAndPricePerKgLessThanEqual(
            String name,
            Double maxPrice,
            Pageable pageable
    );
    @Query("""
SELECT c FROM Cake c
WHERE c.available = true
AND REPLACE(LOWER(c.name),' ','')
LIKE CONCAT('%', REPLACE(LOWER(:name),' ',''), '%')
AND (:minPrice IS NULL OR c.pricePerKg >= :minPrice)
AND (:maxPrice IS NULL OR c.pricePerKg <= :maxPrice)
""")
    Page<Cake> smartSearch(
            String name,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );
    List<Cake> findByAvailableTrueAndFeaturedTrue();
    // FILTER (Flavour + Available)
    List<Cake> findByFlavourAndAvailable(String flavour, boolean available);

    List<Cake> findByAvailable(boolean available);

    List<Cake> findByAvailableTrueAndFlavour(String flavour);


    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cake.id = :cakeId")
    void deleteByCakeId(@Param("cakeId") Long cakeId);

}
