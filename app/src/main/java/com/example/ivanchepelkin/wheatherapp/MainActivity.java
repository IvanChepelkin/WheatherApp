package com.example.ivanchepelkin.wheatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    // Объявляем наши вью
    private Spinner setCity;
    private Button button;
    private SharedPreferences shareP;
    private TextView countText;
    final String SAVED_TEXT = "saved_text";

    static final String textInputKey = "textInputKey";
    private static final String KEY_POSITION_setCity = "KEY_POSITION_setCity";

    private static final String KEY_inputCount = "KEY_inputCount";
    private String inputCount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "запуск onCreate");
        super.onCreate(savedInstanceState);
        //указываем, какой макет отображать
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickListeners();
        chekBundle(savedInstanceState); //метож проверки bundke
        loadText();  //загружаем последний выбор спинер
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

    // переопределяем метод сохранение нашего Bundle
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
    }

    private void setOnClickListeners(){
        button.setOnClickListener(MainActivity.this);
    }

    private void chekBundle(Bundle savedInstanceState){
        if (savedInstanceState != null){ //activity равно null только при первом запуске приложения
            // или когда activity уничтожалась
            inputCount = savedInstanceState.getString(KEY_inputCount); //извлекаем данные счетчика
            countText.setText(inputCount);
            setCity.setSelection(savedInstanceState.getInt(KEY_POSITION_setCity)); // извлекаю данные позицию спинера

        }
    }
    // сохранение данных
    void saveText(int position) {
        //делаем позицию типом String, иначе метод putString не воспринимает
        String pos = String.valueOf(position);
        //Константа MODE_PRIVATE используется для настройки доступа и означает,
        // что после сохранения, данные будут видны только этому приложению
        shareP = getPreferences(MODE_PRIVATE);
        //чтобы редактировать данные, необходим объект Editor
        SharedPreferences.Editor ed = shareP.edit();
        //В метод putString указываем наименование переменной – это константа SAVED_TEXT,
        // и значение – последняя позиция спинера
        ed.putString(SAVED_TEXT, pos);
        //сохраняем данныеZ
        ed.apply();
    }
    //вывод сохраненных данных
    void loadText() {
        shareP = getPreferences(MODE_PRIVATE);
        int pos;
        //Читаем с помощью метода getString – в параметрах указываем константу
        // - это имя, и значение по умолчанию (пустая строка)
        String savedPos = shareP.getString(SAVED_TEXT, "");
        //переделываем сохраненную pos обратно в int
        if (savedPos != "") {
            pos = Integer.parseInt(savedPos);
            //задаем ее в спинере
            setCity.setSelection(pos);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_show_weather) {
                String text = CitySpec.getCity(MainActivity.this, setCity.getSelectedItemPosition());
                saveText(setCity.getSelectedItemPosition()); //отправляем на сохранение позицию
                // Объявляем intent и в его конструктор передаем Context и Activity, которую хотим открыть
                Intent intent = new Intent(MainActivity.this, DisplayWheatherActivity.class);
                // Кладем в Intent строку putExtra(ключ, значение)
                intent.putExtra(textInputKey, text);
                //Запускаем вызов DisplayWheatherActivity
                startActivityForResult(intent,1);
            }
    }
    //метод ожидает ответ от 2 экрана, он переопределенный
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            inputCount = data.getStringExtra("name");
            //выводи наши данные
            countText.setText(inputCount);
        }
    }
}
