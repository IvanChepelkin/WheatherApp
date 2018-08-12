package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
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
    private TextView getWeather;
    private Spinner setCity;
    private Button button;
    private SharedPreferences shareP;
    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //указываем, какой макет отображать
        setContentView(R.layout.activity_main);
        initViews();
        initSpiner();
        setResultBtnClick();
        loadText();  //загружаем последний выбор спинера



    }
   // метод инициаизирует вьюшки через id
   private void initViews(){
        getWeather = findViewById(R.id.result);
        setCity = findViewById(R.id.spinnerForCities);
        button = findViewById(R.id.button_show_weather);
   }
   //инициализируем спинер

   private void initSpiner(){
        //final String [] cities = getResources().getStringArray(R.array.cityGroup); //в массив вносим города
       // из массива, что в ресурасах
       // метод задает город из выпадающего списка и выдает погоду
//       setCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//           @Override
//           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               String text = CitySpec.getCity(MainActivity.this,position); // получаем из класса CitySpec нашу погоду
//               getWeather.setText(text); // передаем ее на текстВью
//           }
//
//           @Override
//           public void onNothingSelected(AdapterView<?> parent) {
//
//           }
//       });

   }
   private void setResultBtnClick(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String text = CitySpec.getCity(MainActivity.this,setCity.getSelectedItemPosition());
                getWeather.setText(text);
                saveText(setCity.getSelectedItemPosition()); //отправляем на сохранение позицию
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
        // и значение – содержимое поля getWeather
        ed.putString(SAVED_TEXT, pos);
        //сохраняем данныеZ
        ed.commit();
        //выводи сообщение "Text saved"
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }
    void loadText() {
        shareP = getPreferences(MODE_PRIVATE);
        int pos;
        //Читаем с помощью метода getString – в параметрах указываем константу
        // - это имя, и значение по умолчанию (пустая строка)
        String savedPos = shareP.getString(SAVED_TEXT, "");
        //переделываем сохраненную pos обратно в int
        pos = Integer.parseInt(savedPos);
        //задаем ее в спинере
        setCity.setSelection(pos);
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }
}
