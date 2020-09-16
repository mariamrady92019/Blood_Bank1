package com.example.bloodbank.activities;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodbank.R;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.map.MyLocationGetter;
import com.example.bloodbank.map.MyLocationProvider;
import com.example.bloodbank.map.Permissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Permissions implements OnMapReadyCallback, LocationListener {

    View view ;
    MapView map ;
    private MyLocationGetter locationGetter ;

    Location myLocation ;
    Marker userMarker ;
    private static final  int PERMESSION_REQUESTLOCATION_CODE =1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 600;



    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_map, container, false);
        return  view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        map = view.findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);

        if(isLocationPermissionAllowed()) {//permission granted
            //call your function
            getUserLocation();
        }else { //request runtime permission
            requestLocationPermission();
        }
        //sync map lifecycle with fragment life cycle
        map.getMapAsync(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }


    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }


    GoogleMap googleMap ;


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap  ;
          drawUserMarker();
        //markUserLocation(myLocation,R.drawable.available ,userMarker,
               // "im here" , this.googleMap);


    }



    public  void access(){

        if(isPermissionGranted(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION)==true)
        {
            Toast.makeText(getActivity(),"can access your location",Toast.LENGTH_SHORT).show();
            getUserLocation();
        }
        else{
            needRequestPermession(Manifest.permission.ACCESS_FINE_LOCATION ,
                    "need permission to access your location !",
                    getActivity(),PERMESSION_REQUESTLOCATION_CODE);
        }




           /*if(isPermissionGranted()==true ){
               Toast.makeText(this,"can access your location",Toast.LENGTH_SHORT).show();
               getUserLocation();
           } else{
               needRequestPermession();
           }*/
    }




    MyLocationProvider myLocationProvider;
   // Location location ;

    public void getUserLocation(){
        if(myLocationProvider==null)
            myLocationProvider = new MyLocationProvider(getActivity());

        myLocation = myLocationProvider.getCurrentLocation(this);

    }


    @Override
    public void onLocationChanged(Location location) {
      //  location = this.myLocation ;
        //markUserLocation(location , R.drawable.available, userMarker , "im here" ,
               // this.googleMap);
        drawUserMarker();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }


    @Override
    public void onProviderDisabled(String s) {

    }


    public void drawUserMarker(){
        if(myLocation==null||map==null){
            return;
        }

        Log.e("user_location",myLocation.getLatitude()+ "  "+myLocation.getLongitude());
        if(userMarker==null)
            userMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()))
                    .title("iam "+Constants.currentUser.getId() +"here"));
       // Constants.currentUser.getName()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.available)));
        else userMarker.setPosition(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
       googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()), 15));

    }


   /** private void markUserLocation(Location location , int drawable , Marker marker , String message , GoogleMap map ) {

        if( map == null||location == null )
        {

           Toast.makeText(getActivity(),"please turn on GPS",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(marker== null){
            Toast.makeText(getActivity(),"marker null",Toast.LENGTH_SHORT).show();

            Log.e("user_location",location.getLatitude()+ "  "+location.getLongitude());
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(),location.getLongitude()))
                    .title(message)
                  .icon(BitmapDescriptorFactory.fromResource(drawable)));



            map.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));
        }else{

            marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
            map.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

        }

    }**/




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMESSION_REQUESTLOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getUserLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
Toast.makeText(getActivity(),"can't access your location ! ",Toast.LENGTH_SHORT).show();


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    private void requestLocationPermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            //show message
            showMessage("this app wants your location to find nearby drivers",
                    "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);

                        }
                    },true);

        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    public boolean isLocationPermissionAllowed(){
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    public String getAdress(Location myLocation){
         Geocoder geocoder = new Geocoder(getContext(),Locale.getDefault());
    List<Address> addresses = null;

    // Here 1 represent max location result to returned,
    // by documents it recommended 1 to 5

    {
        try {
            addresses = geocoder.getFromLocation(myLocation.getLatitude()
                    ,myLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }}

    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName();
    return  city+"_"+"_"+postalCode+"_"+knownName;

}}

