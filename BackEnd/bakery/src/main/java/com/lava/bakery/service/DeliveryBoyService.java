package com.lava.bakery.service;

import com.lava.bakery.entity.DeliveryBoy;
import com.lava.bakery.repository.DeliveryBoyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryBoyService {

    @Autowired
    private DeliveryBoyRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ADD (Admin create)
    public DeliveryBoy add(DeliveryBoy d){
        d.setPassword(passwordEncoder.encode(d.getPassword())); // 🔐 encrypt
        return repo.save(d);
    }

    //  GET ALL
    public List<DeliveryBoy> getAll(){
        return repo.findAll();
    }

    //  DELETE
    public void delete(Long id){
        repo.deleteById(id);
    }

    // UPDATE
    public DeliveryBoy update(Long id, DeliveryBoy updated){
        DeliveryBoy d = repo.findById(id).orElseThrow();

        d.setName(updated.getName());
        d.setPhone(updated.getPhone());
        d.setUsername(updated.getUsername());

        if(updated.getPassword() != null && !updated.getPassword().isEmpty()){
            d.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        return repo.save(d);
    }


    //  LOGIN
    public DeliveryBoy login(String username, String password){

        DeliveryBoy d = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(password, d.getPassword())){
            return d;
        } else {
            throw new RuntimeException("Invalid Password");
        }
    }
    public DeliveryBoy getByUsername(String username){
        return repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Delivery boy not found"));
    }

    public DeliveryBoy getById(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery boy not found"));
    }
}