package com.example.bloodbank.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.List;

public class MyLocationGetter {

  LocationManager mylocationManager;
   Location location ;
   static  final int MINIMUM_TIME_BETWEEN_UPDATES = 5000;
    static final int MINIMUM_DISTANCE_BETWEEN_UPDATES=10;

    public MyLocationGetter(Context context) {
        this.mylocationManager = ((LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE));
        this.location = null;
    }



  @SuppressLint("MissingPermission")
  public Location getUserLocation ( LocationListener locationListener){

        if(canGetLocation()==true){

            String provider =mylocationManager.GPS_PROVIDER;

            if(!(mylocationManager.isProviderEnabled(mylocationManager.GPS_PROVIDER)))
           {
           provider = mylocationManager.NETWORK_PROVIDER;
           }

            location=mylocationManager.getLastKnownLocation(provider);

      /** if(location==null){
            location = getBestLasLocation();
        }**/

            if(locationListener!=null){
                mylocationManager.requestLocationUpdates(provider,
                        MINIMUM_TIME_BETWEEN_UPDATES,MINIMUM_DISTANCE_BETWEEN_UPDATES,
                        locationListener);
            }
           return location;
        }


            return  null ;
     }

    @SuppressLint("MissingPermission")
    private Location getBestLasLocation() {

        Location bestLocation = null ;
        List<String> providers = mylocationManager.getAllProviders();

        for(String provider :providers ){
            Location last = mylocationManager.getLastKnownLocation(provider);
            if(last == null)continue;
            if(bestLocation==null) bestLocation= last;
            else{

                if(last.getAccuracy()>bestLocation.getAccuracy()){

                    bestLocation = last ;
                }
        }
                                       }



     return  bestLocation;
    }



    boolean canGetLocation(){

        boolean GBSEnabled = mylocationManager.isProviderEnabled(
                mylocationManager.GPS_PROVIDER);
        boolean NetWorkEnabled = mylocationManager.isProviderEnabled(mylocationManager.NETWORK_PROVIDER);
        return GBSEnabled||NetWorkEnabled;
    }

}
