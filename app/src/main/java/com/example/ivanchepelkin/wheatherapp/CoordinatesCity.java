package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class CoordinatesCity extends Activity {

    public static CoordinatesCity instance = new CoordinatesCity();

    private final String TAG = "LOCATION";
    private final String MSG_NO_DATA = "No data";
    public String mAddress;
    private LocationManager mLocManager = null;
    private LocListener mLocListener = null;

//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        instance.getCoordinates();
//    }

    @SuppressLint("MissingPermission")
    public String getCoordinates(Location loc) {
        System.out.println("здесь норм");
        mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //записываем координаты в переменную loc через менеджер
       // loc = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
       // loc = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        assert mLocManager != null;
        loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        System.out.println("А здесь фигня");
        // Request address by location
        if (loc != null) {
            mAddress = getAddressByLoc(loc);
        }
        return mAddress;
    }

    private String getAddressByLoc(Location loc) {

        // Create geocoder
        final Geocoder geo = new Geocoder(this);
        // Try to get addresses list
        List<Address> list;
        try {
            list = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        // If list is empty, return "No data" string
        if (list.isEmpty()) {
            return MSG_NO_DATA;
        }
        // Get first element from List
        Address a = list.get(0);
        // Make address string
        return String.valueOf(a.getLocale());
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        // Invoke a parent method, at first
        super.onResume();
        // Create Location Listener object (if needed)
        if (mLocListener == null) mLocListener = new LocListener();
        // Setting up Location Listener
        // min time - 3 seconds
        // min distance - 1 meter
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000L, 1.0F, mLocListener);
    }

    @Override
    protected void onPause() {
        // Remove Location Listener
        if (mLocListener != null) mLocManager.removeUpdates(mLocListener);
        // Invoke a parent method
        super.onPause();
    }

    private final class LocListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + location.toString());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { /* Empty */ }

        @Override
        public void onProviderEnabled(String provider) { /* Empty */ }

        @Override
        public void onProviderDisabled(String provider) { /* Empty */ }
    }
}
