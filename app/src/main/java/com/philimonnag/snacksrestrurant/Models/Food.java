package com.philimonnag.snacksrestrurant.Models;

public class Food {
    String name,Description,FoodImg,Price;

    public Food(String name, String description, String foodImg, String price) {
        this.name = name;
        Description = description;
        FoodImg = foodImg;
        Price = price;
    }

    public Food() {
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getFoodImg() {
        return FoodImg;
    }

    public void setFoodImg(String foodImg) {
        FoodImg = foodImg;
    }
}
