package com.example.bloodbank.firebaseStroge;

import com.example.bloodbank.constant.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.PrivateKey;

public class StrogeBuilder {
 private static FirebaseStorage storage ;
   private static StorageReference storageRef ;
    private static StorageReference imagesRef ;





    public  static FirebaseStorage getStorageInstance(){
        // Create a storage instance
       // if( Constants.currentUser!=null){
            //Do your thing'
            storage = FirebaseStorage.getInstance();

        //}

        return storage ;
     }


     public static StorageReference getStorageRef(){
         // Create a storage reference from our app

             if (storageRef==null){
         storageRef = getStorageInstance().getReference();}

         return  storageRef ;
     }


     public static StorageReference getImagesRef() {
         // Create a child reference
           // imagesRef now points to "images"
     imagesRef = getStorageRef().child("images");

         // Child references can also take paths
         // spaceRef now points to "images/space.jpg
          // imagesRef still points to "images"

         return imagesRef ;


     }




}




