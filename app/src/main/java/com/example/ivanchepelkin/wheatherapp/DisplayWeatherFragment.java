package com.example.ivanchepelkin.wheatherapp;

import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayWeatherFragment extends Fragment implements View.OnClickListener {
    private final String WEATHER_DETAILS_TAG = "ivanchepelkin.weatherapp.weatherDetailsFragment";
    private int length = Weather.getLength();// количество выводимых строк и количесвто экземпляров Weather[];
    private int position;
    private ImageView imageCity;
    private TextView textView;
    private Button button;
    static final String keySendResultPerson = "keySendResult";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // находим наж layout под фрагмент, создаем фрагмент
        return inflater.inflate(R.layout.fragment_display_wheather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);// инициализируем наши вьюшки
        setOnClickListeners();
        initDisplayWeatherDitails();
        displayText(position);
    }

    private void initViews(View view) {
        textView = view.findViewById(R.id.dispayWheather);
        button = view.findViewById(R.id.button_send_Message);
        imageCity = view.findViewById(R.id.imageCity);
        registerForContextMenu(imageCity);
        registerForContextMenu(textView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.imageCity:
                menu.add(0,1,0, "Информация о городе");
                break;
            case R.id.dispayWheather:
                menu.add(0,2,0,"Размер текста");
                menu.add(0,3,0,"Цвет текста");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 2:
                textView.setTextSize(10);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void setOnClickListeners() {
        button.setOnClickListener(this);
    }

    // Вывод прогноза в textVeiw
    private void displayText(int position) {
        Weather[] weathersForCitiesArr = new Weather[length];
        weathersForCitiesArr = Weather.setWeathers(weathersForCitiesArr);
        String category = weathersForCitiesArr[position].getWeatherCity();
        textView.setText(category);
        int image = weathersForCitiesArr[position].getImageCity();
        imageCity.setImageResource(image);

    }

    // метод инициализации вложенного фрагмента с деталями о погоде
    private void initDisplayWeatherDitails() {
        FragmentManager fragmentManager = getChildFragmentManager();
        WeatherDetailsFragment weatherDetailsfragment = (WeatherDetailsFragment) fragmentManager.
                findFragmentByTag(WEATHER_DETAILS_TAG);
        if (weatherDetailsfragment == null) {
            weatherDetailsfragment = new WeatherDetailsFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        weatherDetailsfragment.setPosition(position); // отправляем позицию
        transaction.replace(R.id.weatherDetailsContainer, weatherDetailsfragment, WEATHER_DETAILS_TAG);
        transaction.commit();

    }

    public void setWeatherDispay(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_send_Message) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String sendResult = textView.getText().toString();
            intent.setType("text/plain");//задаем тип передаваемых данных
            intent.putExtra(keySendResultPerson, sendResult);
            String chooserTitle = getString(R.string.chooser_title);
            Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
            try {
                startActivity(chosenIntent);
            } catch (ActivityNotFoundException e) {
                //Toast.makeText(DisplayWheatherFragment.this, R.string.no_app, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
