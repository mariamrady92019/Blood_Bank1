package com.example.bloodbank.hospitals;

import java.io.Serializable;
import java.util.List;

public class HospitalModel implements Serializable {


    private String id;
    private String hospName;
    private String hospAdress;
    private String imageUri;
    private String city;
    private String area;
    private List<String> requests;

    public HospitalModel() {
    }

    public HospitalModel(String id, String hospName, String hospAdress, String imageUri, String city, String area, List<String> requests) {
        this.id = id;
        this.hospName = hospName;
        this.hospAdress = hospAdress;
        this.imageUri = imageUri;
        this.city = city;
        this.area = area;
        this.requests = requests;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public String getHospAdress() {
        return hospAdress;
    }

    public void setHospAdress(String hospAdress) {
        this.hospAdress = hospAdress;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }
}
