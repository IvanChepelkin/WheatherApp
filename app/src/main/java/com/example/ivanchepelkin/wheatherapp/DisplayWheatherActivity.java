package com.example.ivanchepelkin.wheatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayWheatherActivity extends AppCompatActivity {
    TextView textView;
    String textWheather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wheather);
        initViews();
        displayText();
    }

    private void initViews(){
        textView = findViewById(R.id.dispayWheather);
    }
    private void displayText(){
        Intent intent = getIntent();
        if(intent != null){
            // находим по ключу наш текст
          textWheather = intent.getStringExtra(MainActivity.textInputKey );
            // Задаем текст
            textView.setText(textWheather);
        }
    }
}
