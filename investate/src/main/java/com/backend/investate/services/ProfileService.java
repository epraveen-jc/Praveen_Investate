// src/main/java/com/backend/investate/service/ProfileService.java
package com.backend.investate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.investate.EncryptionNDecryption.Decryption;
import com.backend.investate.EncryptionNDecryption.Encryption;

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
        profile.setPassword(new Encryption(profile.getPassword()).getEncryptedString());
        return profileRepository.save(profile);
    }

    public Profile login(String name, String password) {
        Profile profile = profileRepository.findByName(name);

        if (profile != null && new Decryption(profile.getPassword()).getDecryptedString().equals(password)) {
            return profile; // Check hashed passwords in a real application
        }
        return null; // Invalid credentials
    }

    public Profile findByName(String name) {
        return profileRepository.findByName(name);
    }
    public void resetPassword(String name, String newPassword) {
        Profile profile = findByName(name);
        if (profile != null) {
            profile.setPassword(new Encryption(newPassword).getEncryptedString()); // Ensure to hash the password
            profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public void updateUsername(String currentName, String newUsername) {
        Profile profile = findByName(currentName);
        if (profile != null) {
            profile.setName(newUsername);
            profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public void updateProfileImage(String name, String imageUrl) {
        Profile profile = findByName(name);
        if (profile != null) {
            profile.setProfileImage(imageUrl);
            profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

}
