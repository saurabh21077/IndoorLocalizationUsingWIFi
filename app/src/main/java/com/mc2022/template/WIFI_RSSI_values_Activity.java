package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mc2022.template.database.UserLocationDatabase;
import com.mc2022.template.database.WardriveWifiLocationDatabase;
import com.mc2022.template.model.UserLocation;
import com.mc2022.template.model.WardriveWifiLocation;
import com.mc2022.template.model.WifiData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WIFI_RSSI_values_Activity extends AppCompatActivity {

    private TextView textView_latitude, textView_longitude, textView_wifiName, textView_getCurrentLocationByWardriving;
    private Button nearestWifiFetchButton, wardriveLocationButton, getLocationByWardivingButton;

    private EditText editText_wardriveLocationName;

    private int k;

    private WifiManager wifiManager;
    //private WifiReceiver wifiReceiver;
    private ListView listView_scannedWifis;
    private List<ScanResult> scannedWifiList;
    private WardriveWifiLocationDatabase wardriveWifiLocationDatabase;

    //Location related
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_rssi_values);

        listView_scannedWifis = findViewById(R.id.listView_scannedWifis);

        //Wi-Fi fields
        textView_wifiName = findViewById(R.id.nearesrtWifiName);
//        textView_latitude = findViewById(R.id.latitudeValue);
//        textView_longitude = findViewById(R.id.longitudeValue);
//        nearestWifiFetchButton = findViewById(R.id.button_nearestWifiFetch);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //wifiReceiver = new WifiReceiver();
        //registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

//        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
//        }
//        else{
//
//        }
        scanWifiFunc();
        String strongestWifiName = scannedWifiList.get(0).SSID;
        int strongestWifiRSSI = scannedWifiList.get(0).level;
        textView_wifiName.setText( strongestWifiName+" ("+ strongestWifiRSSI+")");

        //K for KNN
        k = scannedWifiList.size() > 5 ? 5 : scannedWifiList.size();
        wardriveWifiLocationDatabase = WardriveWifiLocationDatabase.getInstance(getApplicationContext());


        //K nearest Neighbour
        editText_wardriveLocationName = findViewById(R.id.editText_wardriveLocationName);
        wardriveLocationButton = findViewById(R.id.button_wardrive);
        wardriveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wardriveName = editText_wardriveLocationName.getText().toString();
                if(wardriveName.equalsIgnoreCase("") || wardriveName.equalsIgnoreCase("Enter the name of this location")){
                    Toast.makeText(WIFI_RSSI_values_Activity.this, "Enter a name to wardrive this location", Toast.LENGTH_LONG);
                }
                else{


                    int r = 0 ;
                    while( r < k){
                        WardriveWifiLocation wardriveData = new WardriveWifiLocation(scannedWifiList.get(r).SSID, scannedWifiList.get(r).level, wardriveName);
                        wardriveWifiLocationDatabase.wardriveWifiLocationDAO().insert(wardriveData);
                        r++;
                    }

                    editText_wardriveLocationName.setText("");
                    Toast.makeText(WIFI_RSSI_values_Activity.this, strongestWifiName+" : "+strongestWifiRSSI+"Location Wardrive Success", Toast.LENGTH_LONG);
                }
            }
        });


        getLocationByWardivingButton = findViewById(R.id.button_getLocationByWardriving);
        textView_getCurrentLocationByWardriving = findViewById(R.id.textView_currentLocationByWardriving);
        textView_getCurrentLocationByWardriving.setVisibility(View.GONE);
        getLocationByWardivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<WardriveWifiLocation> wardrivedLocations = wardriveWifiLocationDatabase.wardriveWifiLocationDAO().getList();
                WardriveWifiLocation nearestNeighbour = null;
                int minAvg = Integer.MAX_VALUE;

                int avg =0;
                //KNN algorithm
                for(int i = 0; i<k; i++){
                    int minRSSIdiff = Integer.MAX_VALUE;
                    int totalDiff = 0;
                    WardriveWifiLocation nearestLocationWithThisSSID = null;
                    for(WardriveWifiLocation location : wardrivedLocations){
//                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4444");
//                        System.out.println(scannedWifiList.get(i).SSID+" - "+location.getWifiName());
                        if(scannedWifiList.get(i).SSID.equals(location.getWifiName())){

                            int diff = scannedWifiList.get(i).level - location.getRSSI();
//                            System.out.println("Diff = "+diff);
                            if(diff<0){
                                diff *= -1;
//                                System.out.println("Diff Converted");
                            }
//                            System.out.println("Diff = "+diff+" ; MinRSSIdiff = "+minRSSIdiff);
                            if(diff<minRSSIdiff){
//                                System.out.println("MinRSSIdiff  replaced");
                                minRSSIdiff = diff;
                                nearestLocationWithThisSSID = location;
                            }
                        }
                    }
//                    System.out.println("minRSSIdiff = "+minRSSIdiff + " ; minAvg = "+minAvg);
                    if(minRSSIdiff < minAvg){
//                        System.out.println("min avg changed to "+minAvg);
                        minAvg = minRSSIdiff;
                        nearestNeighbour = nearestLocationWithThisSSID;
                    }
                }

                textView_getCurrentLocationByWardriving.setVisibility(View.VISIBLE);
                if(nearestNeighbour != null){
                    textView_getCurrentLocationByWardriving.setText("Your Current Location is : "+nearestNeighbour.getName());
                }
                else {
                    textView_getCurrentLocationByWardriving.setText("Could not find your current Location");
                }
            }
        });


        // Location Related
//        nearestWifiFetchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(WIFI_RSSI_values_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(WIFI_RSSI_values_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                {
//                    ActivityCompat.requestPermissions(WIFI_RSSI_values_Activity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
//                }
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                locationListener = new WIFI_RSSI_values_Activity.UserLocationFetch();
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);
//                Toast.makeText(WIFI_RSSI_values_Activity.this, "GPS Service Started..!", Toast.LENGTH_SHORT).show();
//
//                textView_wifiName.setText(scannedWifiList.get(0).SSID +" ("+ strongestWifiRSSI+")");
//            }
//        });


    }

    private void scanWifiFunc() {

        if (ContextCompat.checkSelfPermission(WIFI_RSSI_values_Activity.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WIFI_RSSI_values_Activity.this,new String[]{Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_WIFI_STATE},1);
        }
        else if (ContextCompat.checkSelfPermission(WIFI_RSSI_values_Activity.this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WIFI_RSSI_values_Activity.this,new String[]{Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE},1);
        }
        else {
            wifiManager.startScan();
            scannedWifiList = wifiManager.getScanResults();
            listView_scannedWifis.setAdapter(new WifiListAdapter(getApplicationContext(), scannedWifiList));
//            System.out.println("______________________________________________");
//            for(ScanResult sr : scannedWifiList){
//                System.out.println(sr.SSID);
//            }
        }
    }


//    class WifiReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        }
//
//
//    }

    class UserLocationFetch implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {

            UserLocationDatabase userLocationDatabase = UserLocationDatabase.getInstance(getApplicationContext());

            double lat = location.getLatitude();
            double lon = location.getLongitude();

//            System.out.println("==========================================================");
//            System.out.println(lat);
//            System.out.println(lon);

            textView_latitude.setText(String.valueOf(lat));
            textView_longitude.setText(String.valueOf(lon));

            Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geo.getFromLocation(lat, lon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Location:", lat+"  "+lon);

            String locationName = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();

            UserLocation userLocation = new UserLocation(lat, lon, locationName);
            userLocationDatabase.userLocationDAO().insert(userLocation);

            String Location = "";
            List<UserLocation> userLoc = userLocationDatabase.userLocationDAO().getList();
            for(UserLocation l : userLoc){
                Location = "Latitude: "+l.getLatitude() +  "Longitude: "+l.getLongitude() +  "Name: "+l.getName();
            }

            Log.i("LOCATION", Location);



        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }



}