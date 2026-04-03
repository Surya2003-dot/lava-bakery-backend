package com.lava.bakery.controller;

import com.lava.bakery.dto.CakeResponseDTO;
import com.lava.bakery.entity.Cake;
import com.lava.bakery.service.CakeService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/api/cakes")
public class CakeController {

    private final CakeService cakeService;

    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    // =========================================
    // ADMIN - Add Cake
    // =========================================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public CakeResponseDTO addCake(@RequestBody Cake cake) {
        return cakeService.addCake(cake);
    }

        @GetMapping("/search")
    public List<CakeResponseDTO> searchCakes(

            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String flavour,

            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean available) {

        return cakeService.searchCakes(
                sortBy,
                direction,
                name,
                category,
                flavour,
                minPrice,
                maxPrice,
                available
        );
    }

    // =========================================
// ADMIN - Update Cake
// =========================================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CakeResponseDTO updateCake(@PathVariable Long id,
                                      @RequestBody Cake cake) {

        return cakeService.updateCake(id, cake);
    }

    // =========================================
// ADMIN - Get All Cakes
// =========================================
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<CakeResponseDTO> getAllCakes() {
        return cakeService.getAllCakes();
    }

    // =========================================
    // USER + ADMIN - View Cake By ID
    // =========================================

    @GetMapping("/{id}")
    public CakeResponseDTO getCakeById(@PathVariable Long id) {
        return cakeService.getCakeById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCake(@PathVariable Long id) {
        cakeService.deleteCakeWithImage(id);
    }
    @GetMapping("/flavour/{flavour}")
    public List<CakeResponseDTO> getByFlavour(@PathVariable String flavour) {

        return cakeService.getCakesByFlavour(flavour);

    }

    @PostMapping(value = "/add-with-image", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public CakeResponseDTO addCakeWithImage(

            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam String flavour,
            @RequestParam String eggType,
            @RequestParam double pricePerKg,
            @RequestParam(required = false) Double oldPrice,
            @RequestParam(required = false) Integer offerPercentage,
            @RequestParam(required = false) String badge,
            @RequestParam boolean available,
            @RequestParam boolean featured,
            @RequestParam int preparationTimeHours,
            @RequestParam double rating,
            @RequestParam int reviewCount,
            @RequestParam String weights,
            @RequestParam String shapes,
            @RequestParam(required = false) Double dailyCapacityKg,
            @RequestParam(required = false) Double minOrderKg,
            @RequestParam(required = false) Double maxOrderKg,
            @RequestParam(value = "image") MultipartFile image


    ) {

        try {

            System.out.println(" API HIT");

            //  image check
            if (image == null || image.isEmpty()) {
                throw new RuntimeException("Image missing");
            }

            //  file name
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            //  folder path
            java.nio.file.Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

            //  create folder
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            System.out.println("Image Name: " + image.getOriginalFilename());
            System.out.println("Image Size: " + image.getSize());

            java.nio.file.Path filePath = uploadPath.resolve(fileName);

//  save
            Files.copy(
                    image.getInputStream(),
                    filePath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

//  confirm save
            System.out.println("Saved at: " + filePath.toAbsolutePath());

            Cake cake = new Cake();

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
            cake.setFeatured(featured);
            cake.setPreparationTimeHours(preparationTimeHours);
            cake.setRating(rating);
            cake.setReviewCount(reviewCount);
            cake.setWeights(weights);
            cake.setShapes(shapes);
            cake.setDailyCapacityKg(dailyCapacityKg);
            cake.setMinOrderKg(minOrderKg);
            cake.setMaxOrderKg(maxOrderKg);

            cake.setImageUrl("/uploads/" + fileName);

            return cakeService.addCake(cake);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-with-image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CakeResponseDTO updateCakeWithImage(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam String flavour,
            @RequestParam String eggType,
            @RequestParam double pricePerKg,
            @RequestParam(required = false) Double oldPrice,
            @RequestParam(required = false) Integer offerPercentage,
            @RequestParam(required = false) String badge,
            @RequestParam String available,
            @RequestParam boolean featured,
            @RequestParam int preparationTimeHours,
            @RequestParam double rating,
            @RequestParam int reviewCount,
            @RequestParam String weights,
            @RequestParam String shapes,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) Double dailyCapacityKg,
            @RequestParam(required = false) Double minOrderKg,
            @RequestParam(required = false) Double maxOrderKg
    ) throws Exception {

        return cakeService.updateCakeWithImage(
                id,
                name,
                description,
                category,
                flavour,
                eggType,
                pricePerKg,
                oldPrice,
                offerPercentage,
                badge,
                Boolean.parseBoolean(available),
                featured,
                preparationTimeHours,
                rating,
                reviewCount,
                weights,
                shapes, dailyCapacityKg,
                minOrderKg,
                maxOrderKg,
                image
        );
    }

    @GetMapping("/featured")
    public List<CakeResponseDTO> getFeaturedCakes() {
        return cakeService.getFeaturedCakes();
    }

    @GetMapping("/filter")
    public List<CakeResponseDTO> filterCakes(
            @RequestParam(required = false) String flavour,
            @RequestParam(required = false) Boolean available) {

        return cakeService.filterCakes(flavour, available);
    }
}