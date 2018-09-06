package com.example.ivanchepelkin.wheatherapp;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences shareP;
    final static String SAVED_CHECK_BOX1 = "saved_chek_box1";
    final static String SAVED_CHECK_BOX2 = "saved_chek_box2";
    final static String SAVED_CHECK_BOX3 = "saved_chek_box3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCitiesListFragment();
        loadCheckBoxPosition();
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
        transaction.addToBackStack(null);
        transaction.commit();
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
            getSupportFragmentManager().popBackStack();
        }
    }
 // сораняет позиции чекбоксов
    public void saveChekBoxPosition(boolean position, String SAVED_KEY) {
        shareP = getPreferences(MODE_PRIVATE);//Константа MODE_PRIVATE используется для настройки доступа
        SharedPreferences.Editor ed = shareP.edit(); //чтобы редактировать данные, необходим объект Editor

        if (SAVED_KEY == SAVED_CHECK_BOX1) {
            ed.putBoolean(SAVED_CHECK_BOX1, position);
        } else if (SAVED_KEY == SAVED_CHECK_BOX2) {
            ed.putBoolean(SAVED_CHECK_BOX2, position);
        } else if (SAVED_KEY == SAVED_CHECK_BOX3) {
            ed.putBoolean(SAVED_CHECK_BOX3, position);
        }
        ed.apply();
    }
// загружает позиции чекбоксов
    void loadCheckBoxPosition() {
        shareP = getPreferences(MODE_PRIVATE);
        String[] arrSAVED_CHECk_BOX = {SAVED_CHECK_BOX1, SAVED_CHECK_BOX2, SAVED_CHECK_BOX3};
        for (int i = 0; i < arrSAVED_CHECk_BOX.length; i++) {
            Boolean savedCheckBoxPosition = shareP.getBoolean(arrSAVED_CHECk_BOX[i], false);

            if (!savedCheckBoxPosition.equals(false) && arrSAVED_CHECk_BOX[i].equals(SAVED_CHECK_BOX1)) {
                WeatherController.getInstance().setPressureStatus(true);
            } else if (!savedCheckBoxPosition.equals(false) && arrSAVED_CHECk_BOX[i].equals(SAVED_CHECK_BOX2)) {
                WeatherController.getInstance().setWeatherDayStatus(true);
            } else if (!savedCheckBoxPosition.equals(false) && arrSAVED_CHECk_BOX[i].equals(SAVED_CHECK_BOX3)) {
                WeatherController.getInstance().setWeatherWeekStatus(true);
            }
        }
    }
}
