package com.example.ivanchepelkin.wheatherapp;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivanchepelkin.wheatherapp.DateBase.NotesTable;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;

public class WeatherShowFragment extends Fragment implements View.OnClickListener {
    private final Handler handler = new Handler();
    private final static String LOG_TAG = WeatherShowFragment.class.getSimpleName();
    private TextView cityTextView;
    private TextView updatedTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIconTextView;

    private CheckBox pressureCheck;
    private CheckBox weatherCloudyChek;
    private CheckBox weatherHomidityChek;
    private TextView displayPressure;
    private TextView displayCloudy;
    private TextView displayHomidity;

    public String textPressure;
    public String textCloudy;
    public String textHomidity;
    public String changeCity;
    private String keyChangeCity = "keyChangeCity";
    private SQLiteDatabase dateBase;

    public void setDateBase() {
        this.dateBase = MainActivity.dateBase;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_show, container, false);
        initViews(rootView);
        setOnClickListeners();
        loadInstanceState(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        setDateBase();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(keyChangeCity,changeCity);
    }

    private void initViews(View rootview) {
        cityTextView = rootview.findViewById(R.id.city_field);
        updatedTextView = rootview.findViewById(R.id.updated_field);
        currentTemperatureTextView = rootview.findViewById(R.id.current_temperature_field);
        weatherIconTextView = rootview.findViewById(R.id.weather_icon);
        pressureCheck = rootview.findViewById(R.id.pressureCheck);
        weatherCloudyChek = rootview.findViewById(R.id.weatherCloudy);
        weatherHomidityChek = rootview.findViewById(R.id.weatherHumidity);
        displayPressure = rootview.findViewById(R.id.dispayPressure);
        displayCloudy = rootview.findViewById(R.id.dispayCloudyDay);
        displayHomidity = rootview.findViewById(R.id.dispayHumidityWeek);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_weather_show_fragmen, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showInputDialog();
        return true;
    }

    private void setOnClickListeners() {
        pressureCheck.setOnClickListener(WeatherShowFragment.this);
        weatherCloudyChek.setOnClickListener(WeatherShowFragment.this);
        weatherHomidityChek.setOnClickListener(WeatherShowFragment.this);
    }

    private void loadInstanceState( Bundle savedInstanceState){
        if (savedInstanceState == null) {
            changeCity = "Moscow";
            updateWeatherData(changeCity);
        } else {
            changeCity = savedInstanceState.getString(keyChangeCity, "");
        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_city);

        final EditText input = new EditText(getActivity());
        changeCity = input.getText().toString();
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateWeatherData(input.getText().toString());
            }
        });
        builder.show();
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getActivity().getApplicationContext(), city);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.place_not_found, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            setPlaceName(jsonObject);
            setDetails(details, main);
            displayText();
            setCurrentTemp(main);
            setUpdatedText(jsonObject);
            setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = "\u2600";
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        weatherIconTextView.setText(icon);
    }

    private void setUpdatedText(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        String updatedText = "Last update: " + updateOn;
        updatedTextView.setText(updatedText);
    }

    private void setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format("%.2f", main.getDouble("temp")) + "\u2103";
        currentTemperatureTextView.setText(currentTextText);
    }

    private void setDetails(JSONObject details, JSONObject main) throws JSONException {
        textPressure = main.getString("pressure") + "hPa";
        textCloudy = details.getString ("description").toUpperCase();
        textHomidity = main.getString("humidity") + "%";
    }

    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        NotesTable.addNote(cityText,dateBase);
        cityTextView.setText(cityText);
    }

    @Override
    public void onClick(View view) {
        if (pressureCheck.isChecked()) {
            WeatherController.getInstance().setPressureStatus(pressureCheck.isChecked());
            //сохраняем состояние checkBox
            displayPressure.setText(textPressure);
            ((MainActivity) getActivity()).saveChekBoxPosition(pressureCheck.isChecked(), MainActivity.SAVED_CHECK_BOX1);

        } else if (!pressureCheck.isChecked()) {
            WeatherController.getInstance().setPressureStatus(pressureCheck.isChecked());
            displayPressure.setText("");
            ((MainActivity) getActivity()).saveChekBoxPosition(pressureCheck.isChecked(), MainActivity.SAVED_CHECK_BOX1);
        }
        if (weatherCloudyChek.isChecked()) {
            WeatherController.getInstance().setWeatherDayStatus(weatherCloudyChek.isChecked());
            displayCloudy.setText(textCloudy);
            ((MainActivity) getActivity()).saveChekBoxPosition(weatherCloudyChek.isChecked(), MainActivity.SAVED_CHECK_BOX2);

        } else if (!weatherCloudyChek.isChecked()) {
            WeatherController.getInstance().setWeatherDayStatus(weatherCloudyChek.isChecked());
            displayCloudy.setText("");
            ((MainActivity) getActivity()).saveChekBoxPosition(weatherCloudyChek.isChecked(), MainActivity.SAVED_CHECK_BOX2);
        }
        if (weatherHomidityChek.isChecked()) {
            WeatherController.getInstance().setWeatherWeekStatus(weatherHomidityChek.isChecked());
            displayHomidity.setText(textHomidity);
            ((MainActivity) getActivity()).saveChekBoxPosition(weatherHomidityChek.isChecked(), MainActivity.SAVED_CHECK_BOX3);

        } else if (!weatherHomidityChek.isChecked()) {
            WeatherController.getInstance().setWeatherWeekStatus(weatherHomidityChek.isChecked());
            displayHomidity.setText("");
            ((MainActivity) getActivity()).saveChekBoxPosition(weatherHomidityChek.isChecked(), MainActivity.SAVED_CHECK_BOX3);
        }
    }

    private void displayText() {
        boolean checkPressure = WeatherController.getInstance().isPressureStatus();
        boolean checkCloudy = WeatherController.getInstance().isWeatherDayStatus();
        boolean checkHomidity = WeatherController.getInstance().isWeatherWeekStatus();

        if (checkPressure) {
            pressureCheck.setChecked(true);
            displayPressure.setText(textPressure);
        }
        if (checkCloudy ) {
            weatherCloudyChek.setChecked(true);
            displayCloudy.setText(textCloudy);
        }
        if (checkHomidity) {
            weatherHomidityChek.setChecked(true);
            displayHomidity.setText(textHomidity);
        }
    }
}

