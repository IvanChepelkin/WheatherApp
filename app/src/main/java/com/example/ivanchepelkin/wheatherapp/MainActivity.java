package com.example.ivanchepelkin.wheatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerCities;
    private SharedPreferences shareP;
    private CheckBox pressureChek;
    private CheckBox weatherDayChek;
    private CheckBox weatherWeekChek;
    private TextView countText;
    final String SAVED_TEXT = "saved_text";
    final String SAVED_CHECK_BOX1 = "saved_chek_box1";
    final String SAVED_CHECK_BOX2 = "saved_chek_box2";
    final String SAVED_CHECK_BOX3 = "saved_chek_box3";


    static final String keyPressure = "keyPressure";
    static final String keyWeatherDay = "keyWeatherDay";
    static final String keyWeatherWeek = "WeatherWeek";

    private static final String KEY_POSITION_setCity = "KEY_POSITION_setCity";

    private static final String KEY_inputCount = "KEY_inputCount";
    private String inputCount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "запуск onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickListeners();
        loadCheckBoxPosition();
        chekBundle(savedInstanceState); //метод проверки bundle
    }
    // Метод сохранения данных при пересоздании Activity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_inputCount,inputCount); //сохраняю по ключу переменнуюю count
    }
    // метод вывода данных после сохранения при пересоздании Activity
    private void chekBundle(Bundle savedInstanceState){
        if (savedInstanceState != null){
            inputCount = savedInstanceState.getString(KEY_inputCount); //извлекаем данные счетчика
            countText.setText(inputCount);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "запуск onStart MainActivity");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "запуск onResume MainActivity");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "запуск onPause MainActivity");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "запуск onStop MainActivity");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "запуск onDestroy MainActivity");
    }

    // метод инициаизирует вьюшки через id
    private void initViews() {
        recyclerCities = findViewById(R.id.recycler_Cities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerCities.setLayoutManager(linearLayoutManager);
        recyclerCities.setAdapter(new ListRecycler(new WeakReference(this)));

        countText = findViewById(R.id.count);
        pressureChek = findViewById(R.id.pressureCheck);
        weatherDayChek = findViewById(R.id.weatherDayCheck);
        weatherWeekChek = findViewById(R.id.weatherWeekCheck);
    }

    private void setOnClickListeners(){
        pressureChek.setOnClickListener(MainActivity.this);
        weatherWeekChek.setOnClickListener(MainActivity.this);
        weatherDayChek.setOnClickListener(MainActivity.this);
    }

    void saveChekBoxPosition(boolean position, CheckBox checkBox){
        shareP = getPreferences(MODE_PRIVATE);//Константа MODE_PRIVATE используется для настройки доступа
        SharedPreferences.Editor ed = shareP.edit(); //чтобы редактировать данные, необходим объект Editor

        if (checkBox == pressureChek) {
            ed.putBoolean(SAVED_CHECK_BOX1, position);
        } else if (checkBox == weatherDayChek) {
            ed.putBoolean(SAVED_CHECK_BOX2, position);
        } else if (checkBox == weatherWeekChek) {
            ed.putBoolean(SAVED_CHECK_BOX3, position);
        }
        ed.apply();
    }

    void loadCheckBoxPosition() {
        shareP = getPreferences(MODE_PRIVATE);

        CheckBox [] arrCheckBox = {pressureChek,weatherDayChek,weatherWeekChek};
        String [] arrSAVED_CHECk_BOX = {SAVED_CHECK_BOX1,SAVED_CHECK_BOX2,SAVED_CHECK_BOX3};

        for (int i = 0; i < arrSAVED_CHECk_BOX.length ; i++) {
            Boolean savedCheckBoxPosition = shareP.getBoolean(arrSAVED_CHECk_BOX[i],false);
            if (!savedCheckBoxPosition.equals(false)){
                arrCheckBox[i].setChecked(true);
            }
        }
    }
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(MainActivity.this, DisplayWheatherActivity.class);

            if (pressureChek.isChecked()){
                saveChekBoxPosition(pressureChek.isChecked(),pressureChek);
                intent.putExtra(keyPressure,pressureChek.isChecked());
            }
            else if (!pressureChek.isChecked()){
                saveChekBoxPosition(pressureChek.isChecked(),pressureChek);
                intent.putExtra(keyPressure,!pressureChek.isChecked());
            }

            if (weatherDayChek.isChecked()){
                saveChekBoxPosition(weatherDayChek.isChecked(),weatherDayChek);
                intent.putExtra(keyWeatherDay,weatherDayChek.isChecked());
            }
            else if (!weatherDayChek.isChecked()){
                saveChekBoxPosition(weatherDayChek.isChecked(),weatherDayChek);
                intent.putExtra(keyWeatherDay,!weatherDayChek.isChecked());
            }

            if (weatherWeekChek.isChecked()){
                saveChekBoxPosition(weatherWeekChek.isChecked(),weatherWeekChek);
                intent.putExtra(keyWeatherWeek,weatherWeekChek.isChecked());
            }
            else if (!weatherWeekChek.isChecked()){
                saveChekBoxPosition(weatherWeekChek.isChecked(),weatherWeekChek);
                intent.putExtra(keyWeatherWeek,!weatherWeekChek.isChecked());
            }

            startActivityForResult(intent, 1);

    }
    //метод ожидает ответ от 2 экрана, он переопределенный
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            inputCount = data.getStringExtra("name");
            countText.setText(inputCount);
        }
    }
}
