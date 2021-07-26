package com.philimonnag.snacksrestrurant.Models;

public class Restaurant {
    String restaurantId,name,mobile,email,description,postalAddress,latitude,longitude,restaurantImg;
    public Restaurant() {
    }

    public Restaurant(String restaurantId, String name, String mobile, String email, String description, String postalAddress) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.description = description;
        this.postalAddress = postalAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getRestaurantImg() {
        return restaurantImg;
    }

    public void setRestaurantImg(String restaurantImg) {
        this.restaurantImg = restaurantImg;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
