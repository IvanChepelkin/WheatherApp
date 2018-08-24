package com.example.ivanchepelkin.wheatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListRecycler extends RecyclerView.Adapter<ListRecycler.WeatherRecyclerViewHolder> {
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
    public void onBindViewHolder(@NonNull WeatherRecyclerViewHolder weatherRecyclerViewHolder, int position) {
        String category = "Санкт-Петербург";
        weatherRecyclerViewHolder.categoryNameTextView.setText(category);

    }

    @Override
    //метод возвращает количество эл-ов в списке
    public int getItemCount() {
        return 1;
    }

    private void showNailActivity(int position){}



    class WeatherRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTextView; // тексВью для нашего списка

        WeatherRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_name_text_view);
            // вешаем слушать на itemView, который пришел на вход
        }


    }
}
