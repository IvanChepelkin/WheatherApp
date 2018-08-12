package com.example.ivanchepelkin.wheatherapp;

import android.content.Context;

public class CitySpec {
    //context получаем доступ к стоковым ресурсам
    static String getCity (Context context, int position   ){
        String [] setWeather = context.getResources().getStringArray(R.array.weather);
        return setWeather[position];

    }
}
