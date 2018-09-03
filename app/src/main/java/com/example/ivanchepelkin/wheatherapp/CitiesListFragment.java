package com.example.ivanchepelkin.wheatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class CitiesListFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerCities;
    private CheckBox pressureChek;
    private CheckBox weatherDayChek;
    private CheckBox weatherWeekChek;
    int length = Weather.getLength();// количество выводимых строк и количесвто экземпляров Weather[]
    private TextView countText;
    private String inputCount = "";
    final String SAVED_CHECK_BOX1 = "saved_chek_box1";
    final String SAVED_CHECK_BOX2 = "saved_chek_box2";
    final String SAVED_CHECK_BOX3 = "saved_chek_box3";
    private static final String KEY_inputCount = "KEY_inputCount";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_cities, container, false);
        initViews(rootView);
        setOnClickListeners();
        return rootView;
    }


    // метод инициаизирует вьюшки через id
    private void initViews(View rootView) {
        recyclerCities = rootView.findViewById(R.id.recycler_Cities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCities.setLayoutManager(linearLayoutManager);
        recyclerCities.setAdapter(new ListRecyclerAdapter());

        countText = rootView.findViewById(R.id.count);
        pressureChek = rootView.findViewById(R.id.pressureCheck);
        weatherDayChek = rootView.findViewById(R.id.weatherDayCheck);
        weatherWeekChek = rootView.findViewById(R.id.weatherWeekCheck);
    }

    private void setOnClickListeners() {
        pressureChek.setOnClickListener(CitiesListFragment.this);
        weatherWeekChek.setOnClickListener(CitiesListFragment.this);
        weatherDayChek.setOnClickListener(CitiesListFragment.this);
    }


    @Override
    public void onClick(View v) {

        if (pressureChek.isChecked()) {
            WeatherController.getInstance().setPressureStatus(pressureChek.isChecked());
            //saveChekBoxPosition(pressureChek.isChecked(),pressureChek);
        } else if (!pressureChek.isChecked()) {
            WeatherController.getInstance().setPressureStatus(pressureChek.isChecked());
            //saveChekBoxPosition(pressureChek.isChecked(),pressureChek);
        }

        if (weatherDayChek.isChecked()) {
            WeatherController.getInstance().setWeatherDayStatus(weatherDayChek.isChecked());
            //(weatherDayChek.isChecked(),weatherDayChek);
        } else if (!weatherDayChek.isChecked()) {
            WeatherController.getInstance().setWeatherDayStatus(weatherDayChek.isChecked());
            //saveChekBoxPosition(weatherDayChek.isChecked(),weatherDayChek);
        }

        if (weatherWeekChek.isChecked()) {
            WeatherController.getInstance().setWeatherWeekStatus(weatherWeekChek.isChecked());
            //  saveChekBoxPosition(weatherWeekChek.isChecked(),weatherWeekChek);
        } else if (!weatherWeekChek.isChecked()) {
            WeatherController.getInstance().setWeatherWeekStatus(weatherWeekChek.isChecked());
            // saveChekBoxPosition(weatherWeekChek.isChecked(),weatherWeekChek);
        }
    }

    class WeatherRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTextView; // тексВью для нашего списка

        WeatherRecyclerViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.category_list_item, parent, false));
            categoryNameTextView = itemView.findViewById(R.id.category_name_text_view);
            // вешаем слушать на itemView, который пришел на вход
        }
        void bind(final int position) {
                    Weather[] weathersForCitiesArr = new Weather[length];
        weathersForCitiesArr = Weather.setWeathers(weathersForCitiesArr);
        String category = weathersForCitiesArr[position].getCity();
        categoryNameTextView.setText(category);
        categoryNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((MainActivity) getActivity()).initDetailFragment(position);
                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });
        }
    }

    class ListRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerViewHolder> {

        @NonNull
        @Override

// Привязка layouta category_list_item к RV
        public WeatherRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Создаем вью из нашего layout , где мы прописывали category_list_item
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new WeatherRecyclerViewHolder(inflater,parent);// создаем новый экземпляр класса и передаем ему созданную view
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherRecyclerViewHolder weatherRecyclerViewHolder, int position) {
            weatherRecyclerViewHolder.bind(position);
        }
        @Override
        //метод возвращает количество эл-ов в массиве городов
        public int getItemCount() {
            return length;
        }


    }
}