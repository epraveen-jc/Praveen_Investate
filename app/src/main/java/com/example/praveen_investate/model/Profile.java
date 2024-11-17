package com.example.praveen_investate.model;

import android.util.Base64;

import java.io.Serializable;

public class Profile implements Serializable {
    public   String name;
    private String password;
    private String email;
    public   String profileType;
    private String phoneNumber;
    private String profileImage;
    private String state;
    private String district;
    private String streetOrColony;

    public Profile() {
        // Default constructor
    }

    public Profile(String name, String password, String email, String profileType, String phoneNumber,String profileImage, String state, String district, String streetOrColony) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileType = profileType;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.state = state;
        this.district = district;
        this.streetOrColony = streetOrColony;
    }

    // Convert byte array to Base64 string
    public String getProfileImage() {
        return profileImage ;    }

    // Convert Base64 string back to byte array
    public void setProfileImage(String profileImagee) {
        this.profileImage = profileImagee;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getProfileType() { return profileType; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getState() { return state; }
    public String getDistrict() { return district; }
    public String getStreetOrColony() { return streetOrColony; }

    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setProfileType(String profileType) { this.profileType = profileType; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setState(String state) { this.state = state; }
    public void setDistrict(String district) { this.district = district; }
    public void setStreetOrColony(String streetOrColony) { this.streetOrColony = streetOrColony; }
}
