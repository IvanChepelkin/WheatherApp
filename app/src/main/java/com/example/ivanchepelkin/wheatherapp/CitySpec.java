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
    static String getPressure (Context context, int position){
        String [] setPressure = context.getResources().getStringArray(R.array.pressure);
        return setPressure[position];
    }
    static String getWeatherDay (Context context, int position){
        String [] setWeatherDay = context.getResources().getStringArray(R.array.weatherDay);
        return setWeatherDay[position];
    }
    static String getWeatherWeek (Context context, int position){
        String [] setWeatherWeek = context.getResources().getStringArray(R.array.weatherWeek);
        return setWeatherWeek[position];
    }
}
