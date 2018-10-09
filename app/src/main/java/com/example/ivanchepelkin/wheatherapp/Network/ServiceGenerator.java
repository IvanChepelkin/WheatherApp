package com.example.ivanchepelkin.wheatherapp.Network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    public static String OPEN_WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=Moscow&units=metric";
    // метод построения http клиента
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    // подключаем GSON для серриализации
    private static Retrofit.Builder sBuilder = new Retrofit.Builder().baseUrl(OPEN_WEATHER_API_URL).
            addConverterFactory(GsonConverterFactory.create());
    // создаем сервис
    public static <S> S createService(Class<S> ServiceClass){
        // подключаем логгирование
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);
        // передаем интерфейсы, кот реализуют запросы ретрофита
        Retrofit retrofit = sBuilder.client(httpClient.build()).build();
        return retrofit.create(ServiceClass);
    }
}

