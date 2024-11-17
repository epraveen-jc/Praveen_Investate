package com.example.praveen_investate.model;

import android.os.Build;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Post implements Serializable {
    private Long id;
    private String brokerName;
    private String phoneNumber;
    private String title;
    private String image;
    private String streetOrColony;
    private String state;
    private String district;
    private String geolocation;
    private String description;
    private Double pricePerSqrFeet;
    private Double totalSqrFeet;
    private Double totalPrice;
    private Boolean isForSale;
    private Boolean isSold;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private String keyWords;
    private String propertyType; // New field for property type

    // New field for tracking sold status
    private LocalDateTime createdAt;

    public String getBrokerName() {
        return brokerName;
    }



    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStreetOrColony(String streetOrColony) {
        this.streetOrColony = streetOrColony;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPricePerSqrFeet(Double pricePerSqrFeet) {
        this.pricePerSqrFeet = pricePerSqrFeet;
    }

    public void setTotalSqrFeet(Double totalSqrFeet) {
        this.totalSqrFeet = totalSqrFeet;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setIsForSale(Boolean forSale) {
        isForSale = forSale;
    }

    public void setIsSold(Boolean sold) {
        isSold = sold;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getStreetOrColony() {
        return streetOrColony;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public String getDescription() {
        return description;
    }

    public Double getPricePerSqrFeet() {
        return pricePerSqrFeet;
    }

    public Double getTotalSqrFeet() {
        return totalSqrFeet;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Boolean getForSale() {
        return isForSale;
    }

    public Boolean getSold() {
        return isSold;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Post(Long id, String brokerName,String phoneNumber, String title, String image, String streetOrColony, String state, String district, String geolocation, String description, Double pricePerSqrFeet, Double totalSqrFeet, Double totalPrice, Boolean isForSale, Boolean isSold, String keyWords, String propertyType) {
        this.id = id;
        this.brokerName = brokerName;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.image = image;
        this.streetOrColony = streetOrColony;
        this.state = state;
        this.district = district;
        this.geolocation = geolocation;
        this.description = description;
        this.pricePerSqrFeet = pricePerSqrFeet;
        this.totalSqrFeet = totalSqrFeet;
        this.totalPrice = totalPrice;
        this.isForSale = isForSale;
        this.isSold = isSold;
        this.keyWords = keyWords;
        this.propertyType = propertyType;

    }

    public Post() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createdAt = LocalDateTime.now();
        }
        this.isForSale = true; // Default to true for new posts
        this.isSold = false;   // Default to false for new posts
    }
}
