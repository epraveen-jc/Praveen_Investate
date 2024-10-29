// src/main/java/com/backend/investate/controller/AuthController.java
package com.backend.investate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.investate.model.Profile;
import com.backend.investate.services.ProfileService;
/**
 * @author E Praveen Kumar
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Profile profile) {
        try {
            profileService.registerProfile(profile);
            return ResponseEntity.status(201).body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Profile profile) {
        Profile loggedInProfile = profileService.login(profile.getName(), profile.getPassword());
        if (loggedInProfile != null) {
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    @GetMapping("/profile/{name}")
    public ResponseEntity<Profile> getProfile(@PathVariable String name) {
        Profile profile = profileService.findByName(name);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.status(404).body(null); // User not found
    }
}
