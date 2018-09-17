package com.example.ivanchepelkin.wheatherapp;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    View view;
    private SharedPreferences shareP;
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
        initCitiesListFragment();
        initDrawlerMenu(toolbar);
        loadCheckBoxPosition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
                transaction.replace(R.id.fragmentContainer, aboutCreatorFragment);
                transaction.addToBackStack(KeyDetailFragment);
                transaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawlerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // метод инициализирует фрагмент
    public void initDetailFragment(int position) {
        // создаём фрагмент
        DisplayWeatherFragment displayWeatherFragment = new DisplayWeatherFragment();
        // передаем во фрагмент position, номер города изи массива городов weathersForCitiesArr
        displayWeatherFragment.setWeatherDispay(position);
        // Начинаем транзакцию фрагмента через SupportFragmentManage
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // указываем, в какой контейнер хотим поместить наш фрагмент
        transaction.replace(R.id.fragmentContainer, displayWeatherFragment);
        //Добавляем фрагмент в стек для возможности возврата
        transaction.addToBackStack(KeyDetailFragment);
        transaction.commit();
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

    public void initCitiesListFragment() {
        // создаём фрагмент
        FrameLayout container = findViewById(R.id.fragmentContainer);
        if (container.getTag().equals("usual_display")) {
            CitiesListFragment citiesListFragment = new CitiesListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, citiesListFragment);
            transaction.commit();
        }
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
}
