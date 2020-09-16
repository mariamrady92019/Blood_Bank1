package com.example.bloodbank.fireStoreDataBase.Donner;

import com.example.bloodbank.fireStoreDataBase.users.UserResponse;

import java.io.Serializable;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class DonnerResponse implements Serializable {
    private String id;
    private String name;
    private String address;
    private String bloodType;
    private String bloodBankId;
    private String createdAt;
    private boolean approved = true;

    public DonnerResponse() {
    }

    public DonnerResponse(UserResponse userResponse, String bloodBankId, String createdAt) {
        this.id = userResponse.getId();
        this.name = userResponse.getName();
        this.address = userResponse.getAddress();
        this.bloodType = userResponse.getBloodType();
        this.bloodBankId = bloodBankId;
        this.createdAt = createdAt;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodBankId() {
        return bloodBankId;
    }

    public void setBloodBankId(String bloodBankId) {
        this.bloodBankId = bloodBankId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
