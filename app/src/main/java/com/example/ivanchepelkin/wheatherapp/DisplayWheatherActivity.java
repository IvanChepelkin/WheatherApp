package com.example.ivanchepelkin.wheatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayWheatherActivity extends AppCompatActivity{
    TextView textView;
    Button button;
    String textWheather;
    static private String keySendResult = "keySendResult";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wheather);
        initViews();
        displayText();
        shareResult();
    }

    private void initViews(){
        textView = findViewById(R.id.dispayWheather);
        button = findViewById(R.id.button_send_Message);
    }
// Вывод прогноза в textVeiw
    private void displayText(){
        Intent intent = getIntent();
        if(intent != null){
            // находим по ключу наш текст
          textWheather = intent.getStringExtra(MainActivity.textInputKey );
            // Задаем текст
            textView.setText(textWheather);
        }
    }
    //Отправка результата другу
    private void shareResult(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // задаем неявный интент для отправки сообщением
                Intent intent = new Intent(Intent.ACTION_SEND);
                //задаем тип передаваемых данных
                intent.setType("text/plain");
                // Помещаем в Intent строку putExtra(ключ, значение)
                intent.putExtra(keySendResult, textWheather);
                // Пишем заголовок для окна выбора
                String chooserTitle = getString(R.string.chooser_title);
                // Создаем chooser
                Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
                startActivity(chosenIntent);
            }
        });
    }
}
