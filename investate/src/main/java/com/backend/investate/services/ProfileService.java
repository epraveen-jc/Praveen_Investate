// src/main/java/com/backend/investate/service/ProfileService.java
package com.backend.investate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.investate.model.Profile;
import com.backend.investate.repository.ProfileRepository;

@Service
public class ProfileService {
    /**
     * @author E Praveen Kumar
     */
    @Autowired
    private ProfileRepository profileRepository;

    public Profile registerProfile(Profile profile) {
        // Ideally, hash the password before saving
        return profileRepository.save(profile);
    }

    public Profile login(String name, String password) {
        Profile profile = profileRepository.findByName(name);
        if (profile != null && profile.getPassword().equals(password)) {
            return profile; // Check hashed passwords in a real application
        }
        return null; // Invalid credentials
    }

    public Profile findByName(String name) {
        return profileRepository.findByName(name);
    }

}
