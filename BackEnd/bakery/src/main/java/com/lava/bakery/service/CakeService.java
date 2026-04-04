package com.lava.bakery.service;

import com.lava.bakery.dto.CakeResponseDTO;
import com.lava.bakery.entity.Cake;
import com.lava.bakery.exception.BusinessException;
import com.lava.bakery.repository.CakeRepository;
import com.lava.bakery.repository.CartRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.*;

@Service
public class CakeService {
    private final CartRepository cartRepository;
    private final CakeRepository cakeRepository;

    public CakeService(CakeRepository cakeRepository, CartRepository cartRepository) {
        this.cakeRepository = cakeRepository;
        this.cartRepository = cartRepository;
    }

    // =========================================
    // ADMIN - Add Cake
    // =========================================
    public CakeResponseDTO addCake(Cake cake) {

        Cake savedCake = cakeRepository.save(cake);

        return convertToDTO(savedCake);
    }

    public List<CakeResponseDTO> searchCakes(
            String sortBy,
            String direction,
            String name,
            String category,
            String flavour,
            Double minPrice,
            Double maxPrice,
            Boolean available) {

        List<Cake> filtered = cakeRepository.findAll();

        //  SEARCH (CASE + SPACE INSENSITIVE)
        if (name != null && !name.trim().isEmpty()) {

            String searchName = name.toLowerCase().replaceAll("\\s+", "");

            filtered = filtered.stream()
                    .filter(c ->
                            c.getName() != null &&
                                    c.getName()
                                            .toLowerCase()
                                            .replaceAll("\\s+", "")
                                            .contains(searchName)
                    )
                    .toList();
        }

        //  FLAVOUR
        if (flavour != null && !flavour.isEmpty()) {
            filtered = filtered.stream()
                    .filter(c ->
                            c.getFlavour() != null &&
                                    c.getFlavour().equalsIgnoreCase(flavour)
                    )
                    .toList();
        }

        //  AVAILABLE
        if (available != null) {
            filtered = filtered.stream()
                    .filter(c -> c.isAvailable() == available)
                    .toList();
        }

        return filtered.stream()
                .map(this::convertToDTO)
                .toList();
    }
    public CakeResponseDTO getCakeById(Long id) {

        Cake cake = cakeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cake not found"));

        return convertToDTO(cake);
    }
    // =========================================
    // ADMIN - Soft Delete Cake
    // =========================================
    public void deleteCake(Long id) {

        Cake cake = cakeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cake not found"));

        cake.setAvailable(false);

        cakeRepository.save(cake);
        cartRepository.deleteByCakeId(id);
    }

    // =========================================
    // PRIVATE - Convert Entity → DTO
    // =========================================
    private CakeResponseDTO convertToDTO(Cake cake) {

        return new CakeResponseDTO(
                cake.getId(),
                cake.getName(),
                cake.getDescription(),
                cake.getCategory(),
                cake.getFlavour(),
                cake.getEggType(),
                cake.getPricePerKg(),
                cake.getOldPrice(),
                cake.getOfferPercentage(),
                cake.getBadge(),
                cake.isAvailable(),
                cake.getImageUrl(),
                cake.isFeatured(),
                cake.getPreparationTimeHours(),
                cake.getRating(),
                cake.getReviewCount(),
                cake.getWeights(),
                cake.getShapes(),
                cake.getDailyCapacityKg(),
                cake.getMinOrderKg(),
                cake.getMaxOrderKg()




        );

    }
    public List<CakeResponseDTO> getCakesByFlavour(String flavour) {

        Page<Cake> cakes = cakeRepository.findByAvailableTrueAndFlavour(
                flavour,
                Pageable.unpaged()
        );

        return cakes.getContent()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    // =========================================
// ADMIN - Update Cake
// =========================================
    public CakeResponseDTO updateCake(Long id, Cake cake){

        Cake existing = cakeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cake not found"));

        existing.setName(cake.getName());
        existing.setDescription(cake.getDescription());
        existing.setCategory(cake.getCategory());
        existing.setFlavour(cake.getFlavour());
        existing.setEggType(cake.getEggType());
        existing.setPricePerKg(cake.getPricePerKg());
        existing.setOldPrice(cake.getOldPrice());
        existing.setOfferPercentage(cake.getOfferPercentage());
        existing.setBadge(cake.getBadge());
        existing.setAvailable(cake.isAvailable());
        existing.setImageUrl(cake.getImageUrl());
        existing.setFeatured(cake.isFeatured());
        existing.setPreparationTimeHours(cake.getPreparationTimeHours());
        existing.setRating(cake.getRating());
        existing.setReviewCount(cake.getReviewCount());
        existing.setWeights(cake.getWeights());
        existing.setShapes(cake.getShapes());

        return convertToDTO(cakeRepository.save(existing));
    }
    // =========================================
// ADMIN - Get All Cakes
// =========================================
    public List<CakeResponseDTO> getAllCakes(){
        return cakeRepository.findAll()
                .stream()
                .filter(Cake::isAvailable)
                .map(this::convertToDTO)
                .toList();
    }
    public CakeResponseDTO updateCakeWithImage(Long id,
                                               String name,
                                               String description,
                                               String category,
                                               String flavour,
                                               String eggType,
                                               double pricePerKg,
                                               Double oldPrice,
                                               Integer offerPercentage,
                                               String badge,
                                               boolean available,
                                               boolean featured,
                                               int preparationTimeHours,
                                               double rating,
                                               int reviewCount,
                                               String weights,
                                               String shapes,
                                               Double dailyCapacityKg,
                                               Double minOrderKg,
                                               Double maxOrderKg,
                                               MultipartFile image) throws Exception {

        Cake cake = cakeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cake not found"));

        cake.setName(name);
        cake.setDescription(description);
        cake.setCategory(category);
        cake.setFlavour(flavour);
        cake.setEggType(eggType);
        cake.setPricePerKg(pricePerKg);
        cake.setOldPrice(oldPrice);
        cake.setOfferPercentage(offerPercentage);
        cake.setBadge(badge);
        cake.setAvailable(available);
        if(!available){

            cartRepository.deleteByCakeId(id);
        }
        cake.setFeatured(featured);
        cake.setPreparationTimeHours(preparationTimeHours);
        cake.setRating(rating);
        cake.setReviewCount(reviewCount);
        cake.setWeights(weights);
        cake.setShapes(shapes);
        cake.setDailyCapacityKg(dailyCapacityKg);
        cake.setMinOrderKg(minOrderKg);
        cake.setMaxOrderKg(maxOrderKg);

        // IMAGE UPDATE
        if(image != null && !image.isEmpty()){

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String path = "/app/uploads/" + fileName;

            Files.copy(image.getInputStream(), Paths.get(path));

            cake.setImageUrl("/uploads/" + fileName);
        }

        return convertToDTO(cakeRepository.save(cake));
    }
    public List<CakeResponseDTO> getFeaturedCakes(){

        return cakeRepository
                .findByAvailableTrueAndFeaturedTrue()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    public List<CakeResponseDTO> filterCakes(
            String flavour,
            Boolean available){

        List<Cake> cakes;

        if(flavour != null && !flavour.isEmpty() && available != null){
            cakes = cakeRepository.findByFlavourAndAvailable(flavour, available);
        }
        else if(flavour != null && !flavour.isEmpty()){
            cakes = cakeRepository.findByAvailableTrueAndFlavour(flavour);
        }
        else if(available != null){
            cakes = cakeRepository.findByAvailable(available);
        }
        else{
            cakes = cakeRepository.findAll();
        }

        return cakes.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Delivery time options and condition 8am - 8pm
    public Map<String, List<String>> getDeliverySlots(int prepHours){

        Map<String, List<String>> result = new HashMap<>();

        LocalTime now = LocalTime.now();
        LocalTime cutoff = LocalTime.of(20, 0);

        List<String> todaySlots = new ArrayList<>();
        List<String> tomorrowSlots = new ArrayList<>();

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(20, 0);

        // After 8 PM → only tomorrow
        if(now.isAfter(cutoff)){

            while(start.isBefore(end)){
                LocalTime slotEnd = start.plusHours(3);
                tomorrowSlots.add(start + " - " + slotEnd);
                start = slotEnd;
            }

            result.put("tomorrow", tomorrowSlots);
            return result;
        }

        //  Today logic
        LocalTime readyTime = now.plusHours(prepHours);

        start = LocalTime.of(8, 0);

        while(start.isBefore(end)){
            LocalTime slotEnd = start.plusHours(3);

            if(!start.isBefore(readyTime)){
                todaySlots.add(start + " - " + slotEnd);
            }

            start = slotEnd;
        }

        result.put("today", todaySlots);

        //  Tomorrow also send
        start = LocalTime.of(8, 0);
        while(start.isBefore(end)){
            LocalTime slotEnd = start.plusHours(3);
            tomorrowSlots.add(start + " - " + slotEnd);
            start = slotEnd;
        }

        result.put("tomorrow", tomorrowSlots);

        return result;
    }
    public void deleteCakeWithImage(Long id) {

        Cake cake = cakeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cake not found"));

        //  1. REMOVE FROM CART
        cartRepository.deleteByCakeId(id);

        //  2. DELETE IMAGE
        try {
            String imagePath = "/app/uploads/" + Paths.get(cake.getImageUrl()).getFileName();
            Files.deleteIfExists(Paths.get(imagePath));
        } catch (Exception e) {
            System.out.println("Image delete failed ❌");
        }

        //  3. DELETE CAKE
        cakeRepository.deleteById(id);
    }

}