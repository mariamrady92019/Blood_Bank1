package com.example.bloodbank.fireStoreDataBase;

import com.example.bloodbank.constant.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreBuilder {

    private static FirebaseFirestore myDataBase;

    myDataBase = FirebaseFirestore.getInstance();







    public static FirebaseFirestore getFireStoreInstance(){
        if(myDataBase==null|| Constants.firebaseUser!=null)
            myDataBase = FirebaseFirestore.getInstance();
        return myDataBase;
    }
}
