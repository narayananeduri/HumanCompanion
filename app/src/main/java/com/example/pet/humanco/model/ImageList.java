package com.example.pet.humanco.model;

import java.util.List;

/**
 * Created by NARAYANA on 03-05-2018.
 */

public class ImageList {
    private String id;
    private String image;
    private String type;
    private String breed;
    private String age;
    private String location;



    public ImageList(String id, String image, String type, String breed, String age, String location) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
