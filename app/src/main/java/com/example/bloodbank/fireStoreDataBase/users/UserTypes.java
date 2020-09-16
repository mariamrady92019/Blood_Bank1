package com.example.bloodbank.fireStoreDataBase.users;

/**
 * Created By Mohamed El Banna On 7/11/2020
 **/
public enum UserTypes {

    USER_TYPE_ADMIN("0"),
    USER_TYPE_NORMAL("1"),
    USER_TYPE_BLOOD_BANK("2");

    private final String mType;

    UserTypes(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}
