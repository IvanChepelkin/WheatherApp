package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class CoordinatesCity extends Activity {

    private final static String TAG = "LOCATION";
    private final static String MSG_NO_DATA = "No data";
    public static String mAddress = null;
    private LocationManager mLocManager = null;
    private LocationListener mLocListener = null;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initUi();
        getCoordinates();
    }

    @SuppressLint("MissingPermission")
    private void getCoordinates() {
        mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location loc;
        //записываем координаты в переменную loc через менеджер
        loc = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        loc = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        // Request address by location
        if (loc != null) {
           // mAddress.setText(getAddressByLoc(loc));
        }
    }

    private final class LocListener implements LocationListener {

        /**
         *  Called when the location has changed.
         * */
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
