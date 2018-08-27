package com.example.ivanchepelkin.wheatherapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayWheatherActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    String[] cityStringArr = WeatherApplication.context.getResources().getStringArray(R.array.cityGroup);
    int length = cityStringArr.length;// количество выводимых строк и количесвто экземпляров Weather[]
    TextView textView;
    TextView displayPressure;
    TextView displayWeatherDay;
    TextView displayWeatherWeek;

    Button button;

    int position;

    String textWheather;
    String textPressure;
    String textWeatherDay;
    String textWeatherWeek;
    static final String keySendResultPerson = "keySendResult";
    static final String textInputKey = "textInputKey";
    static int cnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "запуск onCreate DisplayWheatherActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wheather);
        initViews();
        setOnClickListeners();
        displayText();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "запуск onStart DisplayWheatherActivity");
    }

    @Override
     protected void onResume() {
        super.onResume();
        Log.i(TAG, "запуск onResume DisplayWheatherActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "запуск onPause DisplayWheatherActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "запуск onStop DisplayWheatherActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "запуск onDestroy DisplayWheatherActivity");
    }
    // переопределяем метод для кнопки "Назад"
    @Override
    public  void onBackPressed(){
        cnt = cnt +1;
        String cntString = String.valueOf(cnt);
        Intent intent = new Intent();
        intent.putExtra("name",cntString);
        setResult(RESULT_OK,intent); //передаем RESULT OK - константа успешного вызова и наш intent
        finish();//закрываем активность
    }


    private void initViews() {
        textView = findViewById(R.id.dispayWheather);
        button = findViewById(R.id.button_send_Message);
        displayPressure = findViewById(R.id.dispayPressure);
        displayWeatherDay = findViewById(R.id.dispayWeatherDay);
        displayWeatherWeek = findViewById(R.id.dispayWeatherWeek);
    }

    private void setOnClickListeners() {
        button.setOnClickListener(this);
    }

    // Вывод прогноза в textVeiw
    private void displayText() {
        Intent intent = getIntent();
        if (intent != null) {
            Weather[] weathersForCitiesArr = new Weather[length];
            weathersForCitiesArr = Weather.setWeathers(weathersForCitiesArr);

            position = intent.getIntExtra(textInputKey,1); // находим по ключу наш текст
            String category = weathersForCitiesArr[position].getWeatherCity();

            textView.setText(category);

            textPressure = intent.getStringExtra(MainActivity.keyPressure);

            if (textPressure !=null ) {
                displayPressure.setText(getString(R.string.давление) + getString(R.string.двоеточие) + textPressure);
            }

            textWeatherDay = intent.getStringExtra(MainActivity.keyWeatherDay);

            if (textWeatherDay != null ){
                displayWeatherDay.setText(getString(R.string.погодаНаЗавтра) + getString(R.string.двоеточие)  +textWeatherDay);
            }

            textWeatherWeek = intent.getStringExtra(MainActivity.keyWeatherWeek);

            if (textWeatherWeek != null ){
                displayWeatherWeek.setText(getString(R.string.ПогодаНаНеделю) + getString(R.string.двоеточие)  +textWeatherWeek);
            }

        }
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
                Toast.makeText(DisplayWheatherActivity.this, R.string.no_app, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
