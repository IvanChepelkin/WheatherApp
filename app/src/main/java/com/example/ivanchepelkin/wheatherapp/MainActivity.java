package com.example.ivanchepelkin.wheatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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
        super.onCreate(savedInstanceState);
        //указываем, какой макет отображать
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickListeners();
        chekBundle(savedInstanceState); //метож проверки bundke
        loadText();  //загружаем последний выбор спинер
    }
    // переопределяем метод сохранение нашего Bundle
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        outState.putString(KEY_inputCount,inputCount); //вносим  по ключу переменнюю
//        super.onSaveInstanceState(outState, outPersistentState);//сохраняем
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_inputCount,inputCount); //вносим  по ключу переменнюю
        outState.putInt(KEY_POSITION_setCity,setCity.getSelectedItemPosition()); //

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
            inputCount = savedInstanceState.getString(KEY_inputCount); //извлекаем данные
            countText.setText(inputCount);
            setCity.setSelection(savedInstanceState.getInt(KEY_POSITION_setCity));

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
