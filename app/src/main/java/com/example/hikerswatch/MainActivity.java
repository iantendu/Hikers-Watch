package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            }
        }
    }
    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);

            }
        };
        if (Build.VERSION.SDK_INT>23){
             } else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation!=null){
                    updateLocationInfo(lastLocation);
                }
            }
        }



    }
    public void updateLocationInfo(Location location){
        Log.i("Location",location.toString());
        TextView latitude,longitude,accuracy,altitude,address;
        latitude=findViewById(R.id.LatitudeTextView);
        longitude=findViewById(R.id.LongitudeTextView);
        accuracy=findViewById(R.id.accuracyTextView);
        altitude=findViewById(R.id.altitudeTextView7);
        address=findViewById(R.id.addressTextView);
        latitude.setText("Latitude: "+Double.toString(location.getLatitude()));
        longitude.setText("Longitude: "+Double.toString(location.getLongitude()));
        accuracy.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altitude.setText("Altitude: "+Double.toString(location.getAltitude()));
        String addressy="Could not find address:(";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (addressList!=null && addressList.size()>0){
                addressy="Address:\n";
                if(addressList.get(0).getThoroughfare()!=null){
                    addressy+=addressList.get(0).getThoroughfare()+ "\n";
                }
                if(addressList.get(0).getLocality()!=null){
                    addressy+=addressList.get(0).getLocality()+ " ";
                }
                if(addressList.get(0).getPostalCode()!=null){
                    addressy+=addressList.get(0).getPostalCode()+ " ";
                }
                if(addressList.get(0).getAdminArea()!=null){
                    addressy+=addressList.get(0).getAdminArea();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        address.setText(addressy);
    }

}