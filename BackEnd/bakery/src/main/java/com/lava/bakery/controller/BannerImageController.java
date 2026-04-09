package com.lava.bakery.controller;

import com.lava.bakery.entity.BannerImage;
import com.lava.bakery.service.BannerImageService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/banner-images")
@CrossOrigin
public class BannerImageController {

    private final BannerImageService service;

    public BannerImageController(BannerImageService service){
        this.service = service;
    }

    // 🔥 Upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public BannerImage upload(

            @RequestParam("image") MultipartFile file,
            @RequestParam String type,
            @RequestParam int position,
            @RequestParam(required = false) String link

    ) throws Exception {

        return service.uploadImage(file, type, position, link);
    }

    // 🔥 GET HERO
    @GetMapping("/hero")
    public List<BannerImage> getHero(){
        return service.getHero();
    }

    // 🔥 GET PROMO
    @GetMapping("/promo")
    public List<BannerImage> getPromo(){
        return service.getPromo();
    }

    // 🔥 ADMIN LIST
    @GetMapping
    public List<BannerImage> getAll(){
        return service.getAll();
    }

    // 🔥 DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    // 🔥 TOGGLE
    @PutMapping("/toggle/{id}")
    public BannerImage toggle(@PathVariable Long id){
        return service.toggle(id);
    }

    // 🔥 POSITION UPDATE
    @PutMapping("/position/{id}")
    public BannerImage updatePosition(
            @PathVariable Long id,
            @RequestParam int position
    ){
        return service.updatePosition(id, position);
    }
}