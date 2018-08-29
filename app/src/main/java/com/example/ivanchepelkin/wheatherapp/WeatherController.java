package com.example.ivanchepelkin.wheatherapp;

public class WeatherController {
    private static WeatherController instance;
    private boolean pressureStatus;
    private boolean weatherDayStatus;
    private boolean weatherWeekStatus;

    public boolean isPressureStatus() {
        return pressureStatus;
    }

    public boolean isWeatherDayStatus() {
        return weatherDayStatus;
    }

    public boolean isWeatherWeekStatus() {
        return weatherWeekStatus;
    }

    public void setPressureStatus(boolean pressureStatus) {
        this.pressureStatus = pressureStatus;
    }

    public void setWeatherDayStatus(boolean weatherDayStatus) {
        this.weatherDayStatus = weatherDayStatus;
    }

    public void setWeatherWeekStatus(boolean weatherWeekStatus) {
        this.weatherWeekStatus = weatherWeekStatus;
    }
    public static WeatherController getInstance(){
        if (instance == null){
            instance = new WeatherController();
        }
        return instance;
    }


}
