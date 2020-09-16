package com.example.bloodbank.events;

public class SearchEvent {
    String city;
    String bloodType;

    public SearchEvent(String city, String bloodType) {
        this.city = city;
        this.bloodType = bloodType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
