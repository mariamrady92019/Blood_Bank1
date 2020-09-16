package com.example.bloodbank.map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bloodbank.Base.BaseActivity;


public class Permissions extends BaseActivity {

 public boolean isPermissionGranted(Context context , String permission ){
      if (ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED) {
         return true;
      }
      return false;
  }

 public void needRequestPermession(final String permission , String message , final Activity activity , final int requestCode){

     if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
            permission)) {

         showMessage( message , "ok",
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int i) {
                     dialog.dismiss();
                     requestPermission(activity,permission,requestCode);
                     }
                 },true);

     } else {
         // No explanation needed; request the permission


        requestPermission(activity,permission ,requestCode);

         // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
         // app-defined int constant. The callback method gets the
         // result of the request.
     }
 }

    public void requestPermission(Activity activity , String permission , int requestCode){

        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                requestCode );

    }


}





