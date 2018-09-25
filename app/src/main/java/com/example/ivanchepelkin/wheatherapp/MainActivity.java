package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.ivanchepelkin.wheatherapp.DateBase.DateBaseHelper;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "LOCATION";
    private final String MSG_NO_DATA = "No data";
    public static String mAddress;
    private LocationManager mLocManager = null;
    public Location loc;
    // private CoordinatesCity.LocListener mLocListener = null;

    View view;
    private SharedPreferences shareP;
    static SQLiteDatabase dateBase;
    final static String SAVED_CHECK_BOX1 = "saved_chek_box1";
    final static String SAVED_CHECK_BOX2 = "saved_chek_box2";
    final static String SAVED_CHECK_BOX3 = "saved_chek_box3";
    final static String KeyDetailFragment = "KeyDetailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //исп toolbar взамен ActionBar
        setSupportActionBar(toolbar);
        initWeatherShowFragment();
        initDrawlerMenu(toolbar);
        initDB();
        loadCheckBoxPosition();
        getCoordinates();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_for_drawler, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.info:
                Toast.makeText(MainActivity.this, getString(R.string.info), Toast.LENGTH_LONG).show();
                break;
            case R.id.settings:
                Toast.makeText(MainActivity.this, getString(R.string.settings), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.aboutCreator:
                AboutCreatorFragment aboutCreatorFragment = new AboutCreatorFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(KeyDetailFragment);
                transaction.replace(R.id.fragmentContainer, aboutCreatorFragment);
                transaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawlerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initWeatherShowFragment() {
        FrameLayout container = findViewById(R.id.fragmentContainer);
        if (container.getTag().equals("usual_display")) {
            WeatherShowFragment weatherShowFragment = new WeatherShowFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, weatherShowFragment);
            transaction.commit();
        }
    }

    public void initDrawlerMenu(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawlerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // инициализируем  базу данных
    public void initDB() {
        dateBase = new DateBaseHelper(getApplicationContext()).getWritableDatabase();
    }

    public void onBackPressed() {
        super.onBackPressed();
        // проверяем, что backStack не пустой
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().findFragmentByTag(KeyDetailFragment);
            // getSupportFragmentManager().popBackStack();
        }
    }

    // сораняет позиции чекбоксов
    public void saveChekBoxPosition(boolean position, String SAVED_KEY) {
        shareP = getPreferences(MODE_PRIVATE);//Константа MODE_PRIVATE используется для настройки доступа
        SharedPreferences.Editor ed = shareP.edit(); //чтобы редактировать данные, необходим объект Editor

        switch (SAVED_KEY) {
            case SAVED_CHECK_BOX1:
                ed.putBoolean(SAVED_CHECK_BOX1, position);
                break;
            case SAVED_CHECK_BOX2:
                ed.putBoolean(SAVED_CHECK_BOX2, position);
                break;
            case SAVED_CHECK_BOX3:
                ed.putBoolean(SAVED_CHECK_BOX3, position);
                break;
        }
        ed.apply();
    }

    // загружает позиции чекбоксов
    void loadCheckBoxPosition() {
        shareP = getPreferences(MODE_PRIVATE);
        String[] arrSavedCheckboxes = {SAVED_CHECK_BOX1, SAVED_CHECK_BOX2, SAVED_CHECK_BOX3};
        for (String savedCheckBox : arrSavedCheckboxes
                ) {
            Boolean savedCheckBoxPosition = shareP.getBoolean(savedCheckBox, false);

            if (!savedCheckBoxPosition.equals(false) && savedCheckBox.equals(SAVED_CHECK_BOX1)) {
                WeatherController.getInstance().setPressureStatus(true);
            } else if (!savedCheckBoxPosition.equals(false) && savedCheckBox.equals(SAVED_CHECK_BOX2)) {
                WeatherController.getInstance().setWeatherDayStatus(true);
            } else if (!savedCheckBoxPosition.equals(false) && savedCheckBox.equals(SAVED_CHECK_BOX3)) {
                WeatherController.getInstance().setWeatherWeekStatus(true);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public String getCoordinates() {
        System.out.println("здесь норм");
        mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //записываем координаты в переменную loc через менеджер
        loc = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
     //   loc = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
     //   loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
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

//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onResume() {
//        // Invoke a parent method, at first
//        super.onResume();
//        // Create Location Listener object (if needed)
//        if (mLocListener == null) mLocListener = new CoordinatesCity.LocListener();
//        // Setting up Location Listener
//        // min time - 3 seconds
//        // min distance - 1 meter
//        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                3000L, 1.0F, mLocListener);
//    }
//
//    @Override
//    protected void onPause() {
//        // Remove Location Listener
//        if (mLocListener != null) mLocManager.removeUpdates(mLocListener);
//        // Invoke a parent method
//        super.onPause();
//    }

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
