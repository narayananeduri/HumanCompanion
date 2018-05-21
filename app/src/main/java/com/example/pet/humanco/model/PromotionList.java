package com.example.pet.humanco.model;

/**
 * Created by NARAYANA on 04-05-2018.
 */

public class PromotionList {
    private String id;
    private String shop_name;
    private String mobile_number;
    private String location;
    private String image;
    private String rating;

    public PromotionList(String id, String shop_name, String mobile_number, String location, String image, String rating)
    {
        this.id = id;
        this.shop_name = shop_name;
        this.mobile_number = mobile_number;
        this.location = location;
        this.image = image;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
