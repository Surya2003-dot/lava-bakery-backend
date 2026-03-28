package com.lava.bakery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private String otp;
    // Constructors
    public User() {}

    public User(String name, String email, String password, Role role, String otp) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.otp= otp;


    }

    // Getters & Setters
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}