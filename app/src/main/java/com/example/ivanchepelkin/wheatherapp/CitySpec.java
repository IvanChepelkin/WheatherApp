package com.example.ivanchepelkin.wheatherapp;

import android.content.Context;

public class CitySpec {
    //context получаем доступ к стоковым ресурсам
    static String getCity (Context context, int position   ){
        //вносим в массив список погоды на наши города
        String [] setWeather = context.getResources().getStringArray(R.array.weather);
        //отправяем погоду относительно позиции спиннера
        return setWeather[position];

    }
}
