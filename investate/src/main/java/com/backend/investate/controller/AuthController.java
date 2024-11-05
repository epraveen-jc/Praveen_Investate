package com.backend.investate.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.investate.model.Profile;
import com.backend.investate.services.ProfileService;

@RestController
@CrossOrigin(origins = "http://10.0.2.2:1010") // Allow requests from this origin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Profile profile) {
        Map<String, String> response = new HashMap<>();
        try {
            profileService.registerProfile(profile);
            response.put("message", "User registered successfully.");
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            response.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/user/exist-or-not/{name}")
    public ResponseEntity<Map<String, String>> checkUserExistence(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();
        if (profileService.isUserExist(name)) {
            response.put("message", "User exists.");
            return ResponseEntity.status(200).body(response);
        } else {
            response.put("error", "User not found.");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Profile profile) {
        Map<String, String> response = new HashMap<>();
        Profile loggedInProfile = profileService.login(profile.getName(), profile.getPassword());
        if (loggedInProfile != null) {
            response.put("message", "Login successful!");
            return ResponseEntity.ok(response);
        }
        response.put("error", "Invalid credentials.");
        return ResponseEntity.status(401).body(response);
    }

    @GetMapping("/profile/{name}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        Profile profile = profileService.findByName(name);
        if (profile != null) {
            response.put("profile", profile);
            return ResponseEntity.ok(response);
        }
        response.put("error", "User not found.");
        return ResponseEntity.status(404).body(response);
    }

    @PostMapping("/profile/{name}/{newPassword}")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable String name, @PathVariable String newPassword) {
        Map<String, String> response = new HashMap<>();
        try {
            profileService.resetPassword(name, newPassword);
            response.put("message", "Password reset successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Password reset failed: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @PutMapping("/profile/{name}/update-username")
    public ResponseEntity<Map<String, String>> updateUsername(@PathVariable String name, @RequestParam String newUsername) {
        Map<String, String> response = new HashMap<>();
        try {
            profileService.updateUsername(name, newUsername);
            response.put("message", "Username updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Username update failed: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @PutMapping("/profile/{name}/update-profile-image")
    public ResponseEntity<Map<String, String>> updateProfileImage(@PathVariable String name, @RequestParam String imageUrl) {
        Map<String, String> response = new HashMap<>();
        try {
            profileService.updateProfileImage(name, imageUrl);
            response.put("message", "Profile image updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Profile image update failed: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @DeleteMapping("/profile/{name}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();
        try {
            profileService.deleteProfile(name);
            response.put("message", "Account deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("error", "Account not found.");
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to delete account: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getProfileByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        return ResponseEntity.of(profileService.getProfileByEmail(email)
            .map(profile -> {
                response.put("profile", profile);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("error", "Profile not found.");
                return ResponseEntity.status(404).body(response);
            }));
    }
}
