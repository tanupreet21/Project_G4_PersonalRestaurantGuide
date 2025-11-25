package com.example.project_g4_personalrestaurantguide;

public class Restaurant {
    private String name;
    private String location;
    private String [] tags;

    private float rating;
    private int imageResId; // Using drawable resource

    public Restaurant(String name, String location, String[] tags, float rating, int imageResId) {
        this.name = name;
        this.location = location;
        this.tags = tags;
        this.rating = rating;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
