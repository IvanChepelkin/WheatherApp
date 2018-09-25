package com.example.ivanchepelkin.wheatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
    private static final int PERMISSION_REQUEST_CODE = 123;
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

    // Метож инициализации  меню Drawler
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
// Инициализация фрагмента с текстом погоды
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

        //записываем координаты в переменную lc через менеджер

        if (hasPermissions()) {
           makeFolder();
        } else {
            requestPermissionWithRationale();
        }

        // Request address by location

        return mAddress;
    }



    // Проверяем, есть ли наше разрешение
    private boolean hasPermissions() {
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET};

        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            // PackageManager.PERMISSION_GRANTED в случае если разрешение есть
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }
    @SuppressLint("MissingPermission")
    private void makeFolder(){
        mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        loc = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        loc = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (loc != null) {
            mAddress = getAddressyBLoc(loc);
        }
    }

    private String getAddressyBLoc(Location loc) {
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

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            makeFolder();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }

    }



    public void showNoStoragePermissionSnackbar() {
//        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), "Storage permission isn't granted" , Snackbar.LENGTH_LONG)
//                .setAction("SETTINGS", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        openApplicationSettings();
//
//                        Toast.makeText(getApplicationContext(),
//                                "Open Permissions and grant the Storage permission",
//                                Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                })
//                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            makeFolder();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            final String message = "Storage permission is needed to show files count";
//            Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), message, Snackbar.LENGTH_LONG)
//                    .setAction("GRANT", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            requestPerms();
//                        }
//                    })
//                    .show();
        } else {
            requestPerms();
        }
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
