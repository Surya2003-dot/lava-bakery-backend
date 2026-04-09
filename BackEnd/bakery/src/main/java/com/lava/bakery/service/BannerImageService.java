package com.lava.bakery.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lava.bakery.entity.BannerImage;
import com.lava.bakery.entity.BannerType;
import com.lava.bakery.repository.BannerImageRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class BannerImageService {

    private final BannerImageRepository repo;
    private final Cloudinary cloudinary;

    public BannerImageService(BannerImageRepository repo, Cloudinary cloudinary){
        this.repo = repo;
        this.cloudinary = cloudinary;
    }

    //  Upload
    public BannerImage uploadImage(MultipartFile file, String type, int position, String link) throws Exception {

        if(file == null || file.isEmpty()){
            throw new RuntimeException("Image missing ❌");
        }

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        String imageUrl = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString(); // 🔥 ADD THIS

        BannerType bannerType = BannerType.valueOf(type.toUpperCase());

        //  Only 1 HERO
        if(bannerType == BannerType.HERO){
            List<BannerImage> heroes =
                    repo.findByTypeAndActiveTrueOrderByPositionAsc(BannerType.HERO);

            heroes.forEach(h -> {
                h.setActive(false);
                repo.save(h);
            });
        }

        BannerImage img = new BannerImage();
        img.setImageUrl(imageUrl);
        img.setPublicId(publicId);
        img.setType(bannerType);
        img.setPosition(position);
        img.setActive(true);
        img.setLink(link);

        return repo.save(img);
    }

    public List<BannerImage> getHero(){
        return repo.findByTypeAndActiveTrueOrderByPositionAsc(BannerType.HERO);
    }

    public List<BannerImage> getPromo(){
        return repo.findByTypeAndActiveTrueOrderByPositionAsc(BannerType.PROMO);
    }

    public List<BannerImage> getAll(){
        return repo.findAll();
    }

    public void delete(Long id){

        BannerImage img = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found"));

        try {
            if(img.getPublicId() != null){

                cloudinary.uploader().destroy(
                        img.getPublicId(),
                        ObjectUtils.emptyMap()
                );

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        repo.deleteById(id);
    }

    public BannerImage toggle(Long id){
        BannerImage img = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        img.setActive(!img.isActive());
        return repo.save(img);
    }

    public BannerImage updatePosition(Long id, int position){
        BannerImage img = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        img.setPosition(position);
        return repo.save(img);
    }
}