package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Объявляем наши вью
    private Spinner setCity;
    private Button button;
    private SharedPreferences shareP;
    final String SAVED_TEXT = "saved_text";
    static final String textInputKey = "textInputKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //указываем, какой макет отображать
        setContentView(R.layout.activity_main);
        initViews();
        setResultBtnClick();
        loadText();  //загружаем последний выбор спинера
    }

    // метод инициаизирует вьюшки через id
    private void initViews() {
        setCity = findViewById(R.id.spinnerForCities);
        button = findViewById(R.id.button_show_weather);
    }

    private void setResultBtnClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = CitySpec.getCity(MainActivity.this, setCity.getSelectedItemPosition());
                saveText(setCity.getSelectedItemPosition()); //отправляем на сохранение позицию
                // Объявляем intent и в его конструктор передаем Context и Activity, которую хотим открыть
                Intent intent = new Intent(MainActivity.this, DisplayWheatherActivity.class);
                // Кладем в Intent строку putExtra(ключ, значение)
                intent.putExtra(textInputKey,text);
                //Запускаем вызов DisplayWheatherActivity
                startActivity(intent);
            }
        });
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
        ed.commit();
    }

    //вывод сохраненных данных
    void loadText() {
        shareP = getPreferences(MODE_PRIVATE);
        int pos;
        //Читаем с помощью метода getString – в параметрах указываем константу
        // - это имя, и значение по умолчанию (пустая строка)
        String savedPos = shareP.getString(SAVED_TEXT, "");
        //переделываем сохраненную pos обратно в int
        if(savedPos != "") {
            pos = Integer.parseInt(savedPos);
            //задаем ее в спинере
            setCity.setSelection(pos);

        }
    }
}
