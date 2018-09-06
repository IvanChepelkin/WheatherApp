package com.example.ivanchepelkin.wheatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class WeatherDetailsFragment extends Fragment implements View.OnClickListener {
    private CheckBox pressureChek;
    private CheckBox weatherDayChek;
    public CheckBox weatherWeekChek;
    private TextView displayPressure;
    private TextView displayWeatherDay;
    private TextView displayWeatherWeek;
    private int position;
    int length = Weather.getLength();// количество выводимых строк и количесвто экземпляров Weather[]

    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        initViews(view);
        loadChekboxStatus();
        setOnClickListeners();
        displayText(position);
        return view;
    }
    // метод инициаизирует вьюшки через id
    private void initViews(View view) {
        pressureChek = view.findViewById(R.id.pressureCheck);
        weatherDayChek = view.findViewById(R.id.weatherDayCheck);
        weatherWeekChek = view.findViewById(R.id.weatherWeekCheck);
        displayPressure = view.findViewById(R.id.dispayPressure);
        displayWeatherDay = view.findViewById(R.id.dispayWeatherDay);
        displayWeatherWeek = view.findViewById(R.id.dispayWeatherWeek);
    }

        private void setOnClickListeners() {
        pressureChek.setOnClickListener(WeatherDetailsFragment.this);
        weatherWeekChek.setOnClickListener(WeatherDetailsFragment.this);
        weatherDayChek.setOnClickListener(WeatherDetailsFragment.this);
    }
    // метод загружает при открытии фргагмента стостояния checkBoxes

    private void loadChekboxStatus(){
        pressureChek.setChecked(WeatherController.getInstance().isPressureStatus());
        weatherDayChek.setChecked(WeatherController.getInstance().isWeatherDayStatus());
        weatherWeekChek.setChecked(WeatherController.getInstance().isWeatherWeekStatus());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        Weather[] weathersForCitiesArr = new Weather[length];
        weathersForCitiesArr = Weather.setWeathers(weathersForCitiesArr);
        if (pressureChek.isChecked()) {
            WeatherController.getInstance().setPressureStatus(pressureChek.isChecked());
            //сохраняем состояние checkBox
            displayPressure.setText(getString(R.string.давление) + getString(R.string.двоеточие) +
                    weathersForCitiesArr[position].getPressure());
            ((MainActivity)getActivity()).saveChekBoxPosition(pressureChek.isChecked(), MainActivity.SAVED_CHECK_BOX1);

        } else if (!pressureChek.isChecked()) {
            WeatherController.getInstance().setPressureStatus(pressureChek.isChecked());
            displayPressure.setText("");
            ((MainActivity)getActivity()).saveChekBoxPosition(pressureChek.isChecked(), MainActivity.SAVED_CHECK_BOX1);
        }
        if (weatherDayChek.isChecked()) {
            WeatherController.getInstance().setWeatherDayStatus(weatherDayChek.isChecked());
            displayWeatherDay.setText(getString(R.string.погодаНаЗавтра) + getString(R.string.двоеточие)  +
                    weathersForCitiesArr[position].getWeatherDay());
            ((MainActivity)getActivity()).saveChekBoxPosition(weatherDayChek.isChecked(), MainActivity.SAVED_CHECK_BOX2);

        } else if (!weatherDayChek.isChecked()) {
            WeatherController.getInstance().setWeatherDayStatus(weatherDayChek.isChecked());
            displayWeatherDay.setText("");
            ((MainActivity)getActivity()).saveChekBoxPosition(weatherDayChek.isChecked(), MainActivity.SAVED_CHECK_BOX2);
        }
        if (weatherWeekChek.isChecked()) {
            WeatherController.getInstance().setWeatherWeekStatus(weatherWeekChek.isChecked());
            displayWeatherWeek.setText(getString(R.string.ПогодаНаНеделю) + getString(R.string.двоеточие)  +
                    weathersForCitiesArr[position].getWetherWeek());
            ((MainActivity)getActivity()).saveChekBoxPosition(weatherWeekChek.isChecked(), MainActivity.SAVED_CHECK_BOX3);

        } else if (!weatherWeekChek.isChecked()) {
            WeatherController.getInstance().setWeatherWeekStatus(weatherWeekChek.isChecked());
            displayWeatherWeek.setText("");
            ((MainActivity)getActivity()).saveChekBoxPosition(weatherWeekChek.isChecked(), MainActivity.SAVED_CHECK_BOX3);
        }
    }

    // метод загружает даннные относительно сохраненных позиций чекбоксов
    @SuppressLint("SetTextI18n")
    private void displayText(int position) {
        Weather[] weathersForCitiesArr = new Weather[length];
        weathersForCitiesArr = Weather.setWeathers(weathersForCitiesArr);
        boolean checkPressure = WeatherController.getInstance().isPressureStatus();
        boolean checkWeatherDay = WeatherController.getInstance().isWeatherDayStatus();
        boolean checkWetherWeek = WeatherController.getInstance().isWeatherWeekStatus();

        if (checkPressure) {
            displayPressure.setText(getString(R.string.давление) + getString(R.string.двоеточие) +
                    weathersForCitiesArr[position].getPressure());
        }
        if (checkWeatherDay ) {
            displayWeatherDay.setText(getString(R.string.погодаНаЗавтра) + getString(R.string.двоеточие)  +
                    weathersForCitiesArr[position].getWeatherDay());
        }
        if (checkWetherWeek) {
            displayWeatherWeek.setText(getString(R.string.ПогодаНаНеделю) + getString(R.string.двоеточие)  +
                    weathersForCitiesArr[position].getWetherWeek());
        }
    }
}
