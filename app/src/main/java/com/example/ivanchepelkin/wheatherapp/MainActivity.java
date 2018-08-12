package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Объявляем наши вью
    private TextView getWeather;
    private Spinner setCity;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //указываем, какой макет отображать
        setContentView(R.layout.activity_main);
        initViews();
        initSpiner();
        setResultBtnClick();


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
            }


        });
   }
}
