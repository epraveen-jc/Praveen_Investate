// src/main/java/com/backend/investate/controller/AuthController.java
package com.backend.investate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.investate.model.Profile;
import com.backend.investate.model.Post;
import com.backend.investate.services.ProfileService;
import com.backend.investate.services.PostService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PostService postService;

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

    @PutMapping("/profile/{name}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable String name, @RequestParam String newPassword) {
        try {
            profileService.resetPassword(name, newPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Password reset failed: " + e.getMessage());
        }
    }

    @PutMapping("/profile/{name}/update-username")
    public ResponseEntity<String> updateUsername(@PathVariable String name, @RequestParam String newUsername) {
        try {
            profileService.updateUsername(name, newUsername);
            return ResponseEntity.ok("Username updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Username update failed: " + e.getMessage());
        }
    }

    @PutMapping("/profile/{name}/update-profile-image")
    public ResponseEntity<String> updateProfileImage(@PathVariable String name, @RequestParam String imageUrl) {
        try {
            profileService.updateProfileImage(name, imageUrl);
            return ResponseEntity.ok("Profile image updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Profile image update failed: " + e.getMessage());
        }
    }

    
}
