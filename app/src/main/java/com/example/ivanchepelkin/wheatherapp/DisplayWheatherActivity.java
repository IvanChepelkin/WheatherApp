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
    TextView textView;
    Button button;
    String textWheather;
    static final String keySendResult = "keySendResult";
    static int cnt = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "запуск onCreate DisplayWheatherActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wheather);
        initViews();
        setOnClickListeners();
        displayText();
        //   shareResult();
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
        //делае cnt типом String
        String cntString = String.valueOf(cnt);
        Intent intent = new Intent();
        intent.putExtra("name",cntString);
        //передаем RESULT OK - константа успешного вызова и наш intent
        setResult(RESULT_OK,intent);
        //закрываем активность
        finish();
    }


    private void initViews() {
        textView = findViewById(R.id.dispayWheather);
        button = findViewById(R.id.button_send_Message);
    }

    private void setOnClickListeners() {
        button.setOnClickListener(this);
    }

    // Вывод прогноза в textVeiw
    private void displayText() {
        Intent intent = getIntent();
        if (intent != null) {
            // находим по ключу наш текст
            textWheather = intent.getStringExtra(MainActivity.textInputKey);
            // Задаем текст
            textView.setText(textWheather);
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_send_Message) {
            // задаем неявный интент для отправки сообщением
            Intent intent = new Intent(Intent.ACTION_SEND);
            //задаем тип передаваемых данных
            String sendResult = textView.getText().toString();
            intent.setType("text/plain");
            // Помещаем в Intent строку putExtra(ключs значение)
            intent.putExtra(keySendResult, sendResult);
            // Пишем заголовок для окна выбора
            String chooserTitle = getString(R.string.chooser_title);
            // Создаем chooser
            Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
            //способ обработки метода startActivity() неявного Intent,
            // который позволит не закрываться аварийно вашему приложению
            // при отсутствии в системе Activity, имеющей нужный Action.
            try {
                startActivity(chosenIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(DisplayWheatherActivity.this, R.string.no_app, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
