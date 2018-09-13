package com.example.ivanchepelkin.wheatherapp;

public class Weather {
    private String city;
    private String weatherCity;
    private String pressure;
    private String weatherDay;
    private String wetherWeek;
    private int imageCity;
    private static String[] cityStringArr = WeatherApplication.context.
            getResources().getStringArray(R.array.cityGroup);
    private static String[] weatherStringArr = WeatherApplication.context.
            getResources().getStringArray(R.array.weather);
    private static String[] pressureStringArr = WeatherApplication.context.
            getResources().getStringArray(R.array.pressure);
    private static String[] weatherDayStringArr = WeatherApplication.context.
            getResources().getStringArray(R.array.weatherDay);
    private static String[] weatherWeekStringArr = WeatherApplication.context.
            getResources().getStringArray(R.array.weatherWeek);
    private static int [] imageList = {R.drawable.moscow,R.drawable.spb,
            R.drawable.ekb,R.drawable.kazan,R.drawable.krasnodar};

    private Weather(String city, String weatherCity, String pressure, String weatherDay, String wetherWeek,int imageCity) {
        this.city = city;
        this.weatherCity = weatherCity;
        this.pressure = pressure;
        this.weatherDay = weatherDay;
        this.wetherWeek = wetherWeek;
        this.imageCity = imageCity;
    }

    public static Weather[] setWeathers(Weather weathersForCitiesArr[]) {
        for (int i = 0; i < cityStringArr.length; i++) {
            weathersForCitiesArr[i] = new Weather(cityStringArr[i], weatherStringArr[i], pressureStringArr[i],
                    weatherDayStringArr[i], weatherWeekStringArr[i],imageList[i]);
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

    public int getImageCity() {
        return imageCity;
    }

    public static int getLength() {
        return cityStringArr.length;
    }

}
