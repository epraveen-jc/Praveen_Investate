package com.backend.investate.model;

import com.backend.investate.enums.ProfileType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/**
 * @author E Praveen Kumar
 */
@Data
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private ProfileType profileType;  // e.g., AGENT, CLIENT, OWNER
    private String phoneNumber;
    
    private String profileImage; // Changed to byte array
    private String state;
    private String district;
    private String streetOrColony;

    
    public String getProfileImage() {
        return profileImage ;
    }
    public void setProfileImage(String Image) {
       this.profileImage = Image;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String p) {
        this.password = p;
    }
    public Profile(){}
    // Getters and setters
}


