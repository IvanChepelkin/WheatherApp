package com.example.ivanchepelkin.wheatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class ListRecycler extends RecyclerView.Adapter<ListRecycler.WeatherRecyclerViewHolder> {

    String[] cityStringArr = WeatherApplication.context.getResources().getStringArray(R.array.cityGroup);
    int length = cityStringArr.length;// количество выводимых строк и количесвто экземпляров Weather[]
    private WeakReference<Activity> activity;
    ListRecycler(WeakReference<Activity> activity ){
        this.activity = activity;
    }
    @NonNull
    @Override

// Привязка layouta category_list_item к RV
    public WeatherRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Создаем вью из нашего layout , где мы прописывали category_list_item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item, viewGroup, false);
        return new WeatherRecyclerViewHolder(view);// создаем новый экземпляр класса и передаем ему созданную view
    }
    @Override
    // в этом методе происходит замена старых данных на новые при прокрутке и появлении новых строк RV
    public void onBindViewHolder(@NonNull WeatherRecyclerViewHolder weatherRecyclerViewHolder, final int position) {
        Weather[] weathersForCitiesArr = new Weather[length];
        weathersForCitiesArr = Weather.setWeathers(weathersForCitiesArr);
        String category = weathersForCitiesArr[position].getCity();
        weatherRecyclerViewHolder.categoryNameTextView.setText(category);
        weatherRecyclerViewHolder.categoryNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNailActivity(position);
            }
        });
    }

    @Override
    //метод возвращает количество эл-ов в списке
    public int getItemCount() {

        return length;
    }
    private void showNailActivity(int position){
        if (activity.get() !=null){
            Intent intent = new Intent(activity.get(),DisplayWheatherActivity.class);
            intent.putExtra(DisplayWheatherActivity.textInputKey,position);
            activity.get().startActivity(intent);
        }
    }

    class WeatherRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTextView; // тексВью для нашего списка

        WeatherRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_name_text_view);
            // вешаем слушать на itemView, который пришел на вход
        }


    }
}
