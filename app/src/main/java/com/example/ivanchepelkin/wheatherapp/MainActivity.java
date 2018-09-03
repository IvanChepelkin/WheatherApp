package com.example.ivanchepelkin.wheatherapp;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static final int cnt_requestCode = 1;
    private int position;
    private SharedPreferences shareP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCitiesListFragment();
        //loadCheckBoxPosition();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  outState.putString(KEY_inputCount,inputCount); //сохраняю по ключу переменнуюю count
    }
    // метод инициализирует фрагмент
    public void initDetailFragment(int position){
        // создаём фрагмент
        DisplayWeatherFragment displayWeatherFragment = new DisplayWeatherFragment();
        // передаем во фрагмент position, номер города изи массива городов weathersForCitiesArr
        displayWeatherFragment.setWeatherDispay(position);
        // Начинаем транзакцию фрагмента через SupportFragmentManage

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // указываем, в какой контейнер хотим поместить наш фрагмент
        transaction.add(R.id.fragmentContainer,displayWeatherFragment);
        transaction.commit();
    }


   public void initCitiesListFragment(){
        // создаём фрагмент
        CitiesListFragment citiesListFragment = new CitiesListFragment();
        // Начинаем транзакцию фрагмента через SupportFragmentManager
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // указываем, в какой контейнер хотим поместить наш фрагмент
        transaction.add(R.id.fragmentContainer,citiesListFragment);
        transaction.commit();
    }
//    void saveChekBoxPosition(boolean position, CheckBox checkBox){
//        shareP = getPreferences(MODE_PRIVATE);//Константа MODE_PRIVATE используется для настройки доступа
//        SharedPreferences.Editor ed = shareP.edit(); //чтобы редактировать данные, необходим объект Editor
//
//        if (checkBox == pressureChek) {
//            ed.putBoolean(SAVED_CHECK_BOX1, position);
//        } else if (checkBox == weatherDayChek) {
//            ed.putBoolean(SAVED_CHECK_BOX2, position);
//        } else if (checkBox == weatherWeekChek) {
//            ed.putBoolean(SAVED_CHECK_BOX3, position);
//        }
//        ed.apply();
//    }
//    void loadCheckBoxPosition() {
//        shareP = getPreferences(MODE_PRIVATE);
//
//        CheckBox [] arrCheckBox = {pressureChek,weatherDayChek,weatherWeekChek};
//        String [] arrSAVED_CHECk_BOX = {SAVED_CHECK_BOX1,SAVED_CHECK_BOX2,SAVED_CHECK_BOX3};
//        for (int i = 0; i < arrSAVED_CHECk_BOX.length ; i++) {
//
//            Boolean savedCheckBoxPosition = shareP.getBoolean(arrSAVED_CHECk_BOX[i], false);
//            if (!savedCheckBoxPosition.equals(false) && arrCheckBox[i].equals(pressureChek)) {
//                arrCheckBox[i].setChecked(true);
//                WeatherController.getInstance().setPressureStatus(true);
//            } else if (!savedCheckBoxPosition.equals(false) && arrCheckBox[i].equals(weatherDayChek)) {
//                arrCheckBox[i].setChecked(true);
//                WeatherController.getInstance().setWeatherDayStatus(true);
//            } else if (!savedCheckBoxPosition.equals(false) && arrCheckBox[i].equals(weatherWeekChek)) {
//                arrCheckBox[i].setChecked(true);
//                WeatherController.getInstance().setWeatherWeekStatus(true);
//            }
//        }
//    }
    //метод ожидает ответ от 2 экрана, он переопределенный
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == cnt_requestCode) {
//            if (resultCode == RESULT_OK) {
//                inputCount = data.getStringExtra("name");
//                countText.setText(inputCount);
//            }
//        }
//    }
}
