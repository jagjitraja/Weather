package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.DialogueMethods;
import com.team.jz.weather.Weather.Utilities;
import com.team.jz.weather.Weather.WeatherReading;

import java.util.ArrayList;
import java.util.List;

public class SavedCitiesListFragment extends Fragment implements DownloadCallback {

    private final int DELETE_MENU_ID = 1;
    private ArrayList<String> cities;
    private String cityClicked = null;
    CitiesAdapter adapter;
    public SavedCitiesListFragment() {
        cities = new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cities = new ArrayList<>();
        if(savedInstanceState!=null){
            cities = (ArrayList<String>) savedInstanceState.getSerializable(MainActivity.CITIES_ARRAY_LIST_KEY);
        }else{
            final MainActivity main = (MainActivity) getActivity();
            cities = main.getCities();
        }
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View citiesListViewParent = inflater.inflate(R.layout.fragment_saved_cities_list,container,false);
        adapter= new CitiesAdapter(getContext(),R.layout.city_list_item,cities);
        final ListView citiesList = citiesListViewParent.findViewById(R.id.saved_cities_list);
        citiesList.setAdapter(adapter);
        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FetchDataTask fetchDataTask = new FetchDataTask(getContext(),SavedCitiesListFragment.this);
                fetchDataTask.execute(Utilities.FORECAST_WEATHER,cities.get(i));
                cityClicked = cities.get(i);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) citiesListViewParent.findViewById(R.id.add_city_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogueMethods dialogueMethods = new DialogueMethods(getContext());
                dialogueMethods.showAddCityDialogue(adapter,cities);
            }
        });
        Log.d(cities+"aaa", "onCreateView: ");
        registerForContextMenu(citiesList);
        return citiesListViewParent;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,DELETE_MENU_ID,1, R.string.delete);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId()==DELETE_MENU_ID){
            AdapterView.AdapterContextMenuInfo contextInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int positionClicked = contextInfo.position;
            cities.remove(positionClicked);
            adapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MainActivity.CITIES_ARRAY_LIST_KEY,cities);
    }

    @Override
    public void finishedDownloading(ArrayList<WeatherReading> weatherReading) {
        MainActivity main = (MainActivity) getActivity();
        main.finishedDownloading(weatherReading);
        main.goToWeatherDataFragment();
        main.setCities(cities);
        main.setCurrentCity(cityClicked);
    }

    public class CitiesAdapter extends ArrayAdapter<String>{
        public CitiesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.city_list_item,parent,false);
            }
            TextView cityName = convertView.findViewById(R.id.city_name);
            cityName.setText(cities.get(position));
            return convertView;
        }
    }


}
