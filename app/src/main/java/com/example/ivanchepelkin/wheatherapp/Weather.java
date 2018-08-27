package com.example.ivanchepelkin.wheatherapp;

public class Weather {
    private String city;
    private String weatherCity;
    private String pressure;
    private String weatherDay;
    private String wetherWeek;


    public Weather(String city, String weatherCity, String pressure, String weatherDay, String wetherWeek) {
        this.city = city;
        this.weatherCity = weatherCity;
        this.pressure = pressure;
        this.weatherDay = weatherDay;
        this.wetherWeek = wetherWeek;
    }

    public static Weather[] setWeathers(Weather weathersForCitiesArr[]) {
        String[] cityStringArr = WeatherApplication.context.getResources().getStringArray(R.array.cityGroup);
        String[] weatherStringArr = WeatherApplication.context.getResources().getStringArray(R.array.weather);
        String[] preassureStringArr = WeatherApplication.context.getResources().getStringArray(R.array.pressure);
        String[] weatherDayStringArr = WeatherApplication.context.getResources().getStringArray(R.array.weatherDay);
        String[] weatherWeekStringArr = WeatherApplication.context.getResources().getStringArray(R.array.weatherWeek);

        for (int i = 0; i < cityStringArr.length; i++) {
            weathersForCitiesArr[i] = new Weather(cityStringArr[i], weatherStringArr[i], preassureStringArr[i], weatherDayStringArr[i], weatherWeekStringArr[i]);
        }
        return weathersForCitiesArr;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherCity() {
        return weatherCity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public String getWetherWeek() {
        return wetherWeek;
    }

}
