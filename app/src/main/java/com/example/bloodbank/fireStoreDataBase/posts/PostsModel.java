package com.example.bloodbank.fireStoreDataBase.posts;

import java.io.Serializable;
import java.util.List;

public class PostsModel implements Serializable {
    public PostsModel() {
    }

    private String id;
    private String name;
    private String bloodType;
    private String address;
    private String city;
    private String area;
    private String imageloadedName;
    private List<String> requests;



    public PostsModel(String name, String bloodType, String address, String city, String area) {
        this.name = name;
        this.bloodType = bloodType;
        this.address = address;
        this.city = city;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImageloadedName() {
        return imageloadedName;
    }

    public void setImageloadedName(String imageloadedName) {
        this.imageloadedName = imageloadedName;
    }

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }
}
