package com.example.praveen_investate.model;

public class PropertyType {
    private String name;
    private String keywords;
    private int imageResId;

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public PropertyType(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}

