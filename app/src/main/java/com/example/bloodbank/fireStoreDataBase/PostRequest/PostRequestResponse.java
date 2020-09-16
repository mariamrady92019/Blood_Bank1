package com.example.bloodbank.fireStoreDataBase.PostRequest;

import java.io.Serializable;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class PostRequestResponse implements Serializable {
    private String id;
    private String userId;
    private String bloodBankId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBloodBankId() {
        return bloodBankId;
    }

    public void setBloodBankId(String bloodBankId) {
        this.bloodBankId = bloodBankId;
    }
}
