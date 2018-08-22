package com.example.ivanchepelkin.wheatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private Spinner setCity;
    private Button button;
    private SharedPreferences shareP;
    private CheckBox pressureChek;
    private CheckBox weatherDayChek;
    private CheckBox weatherWeekChek;
    private TextView countText;
    final String SAVED_TEXT = "saved_text";
    final String SAVED_CHECK_BOX1 = "saved_chek_box1";
    final String SAVED_CHECK_BOX2 = "saved_chek_box2";
    final String SAVED_CHECK_BOX3 = "saved_chek_box3";

    static final String textInputKey = "textInputKey";
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
        chekBundle(savedInstanceState); //метод проверки bundle
        loadText();  //загружаем последний выбор спинера
        loadCheckBoxPosition();
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

    // Метод сохранения данных при пересоздании Activity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_inputCount,inputCount); //сохраняю по ключу переменнуюю count
        outState.putInt(KEY_POSITION_setCity,setCity.getSelectedItemPosition()); //сохраняю по ключу позицию спинера
    }

    // метод инициаизирует вьюшки через id
    private void initViews() {
        setCity = findViewById(R.id.spinnerForCities);
        button = findViewById(R.id.button_show_weather);
        countText = findViewById(R.id.count);
        pressureChek = findViewById(R.id.pressureCheck);
        weatherDayChek = findViewById(R.id.weatherDayCheck);
        weatherWeekChek = findViewById(R.id.pressureCheck);
    }

    private void setOnClickListeners(){
        button.setOnClickListener(MainActivity.this);
        pressureChek.setOnClickListener(MainActivity.this);
        weatherWeekChek.setOnClickListener(MainActivity.this);
        weatherDayChek.setOnClickListener(MainActivity.this);
    }
 // метод вывода данных после сохранения при пересоздании Activity
    private void chekBundle(Bundle savedInstanceState){
        if (savedInstanceState != null){
            inputCount = savedInstanceState.getString(KEY_inputCount); //извлекаем данные счетчика
            countText.setText(inputCount);
            setCity.setSelection(savedInstanceState.getInt(KEY_POSITION_setCity)); // извлекаю данные позицию спинера
        }
    }
    // сохранение данных
    void saveText(int position) {
        String pos = String.valueOf(position);
        shareP = getPreferences(MODE_PRIVATE);//Константа MODE_PRIVATE используется для настройки доступа
        SharedPreferences.Editor ed = shareP.edit(); //чтобы редактировать данные, необходим объект Editor
        ed.putString(SAVED_TEXT, pos);
        ed.apply();
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
    //вывод сохраненных данных
    void loadText() {
        shareP = getPreferences(MODE_PRIVATE);
        int pos;
        String savedPos = shareP.getString(SAVED_TEXT, "");
        if (!savedPos.equals("")) {
            pos = Integer.parseInt(savedPos);
            setCity.setSelection(pos);
        }
    }
    void loadCheckBoxPosition() {
        shareP = getPreferences(MODE_PRIVATE);
        Boolean savedCheckBoxPosition = shareP.getBoolean(SAVED_CHECK_BOX1,true);
        if (!savedCheckBoxPosition) {
            pressureChek.setChecked(false);
        }
    }
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(MainActivity.this, DisplayWheatherActivity.class);

        if (v.getId() == R.id.button_show_weather) {

            if (pressureChek.isChecked()){
                String pressure = CitySpec.getPressure(MainActivity.this,setCity.getSelectedItemPosition() );
                intent.putExtra(keyPressure,pressure );
                saveChekBoxPosition(pressureChek.isChecked(),pressureChek);
            }
            if (weatherDayChek.isChecked()){
                String weatherDay = CitySpec.getWeatherDay(MainActivity.this,setCity.getSelectedItemPosition());
                intent.putExtra(keyWeatherDay,weatherDay);
                saveChekBoxPosition(pressureChek.isChecked(),weatherDayChek);
            }
            if (weatherWeekChek.isChecked()){
                String weatherWeek = CitySpec.getWeatherWeek(MainActivity.this,setCity.getSelectedItemPosition());
                intent.putExtra(keyWeatherWeek,weatherWeek);
                saveChekBoxPosition(pressureChek.isChecked(),weatherWeekChek);
            }

            String text = CitySpec.getCity(MainActivity.this, setCity.getSelectedItemPosition());
            saveText(setCity.getSelectedItemPosition()); //отправляем на сохранение позицию
            intent.putExtra(textInputKey, text); // Кладем в Intent строку putExtra(ключ, значение)
            startActivityForResult(intent, 1);

        }
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
