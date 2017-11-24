package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;
import com.team.jz.weather.Weather.Utilities;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SavedCitiesListFragment extends Fragment {


    private ArrayList<String> cities;

    public SavedCitiesListFragment() {
        cities = new ArrayList<>();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cities = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View citiesListView = inflater.inflate(R.layout.fragment_saved_cities_list,container,false);

        final CitiesAdapter adapter = new CitiesAdapter(getContext(),R.layout.city_list_item,cities);
        final ListView citiesList = citiesListView.findViewById(R.id.saved_cities_list);
        citiesList.setAdapter(adapter);

        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(cities.get(i), "onItemClick: ");

                MainActivity main = (MainActivity) getActivity();
                FetchDataTask fetchDataTask = new FetchDataTask(getContext(),main);
                fetchDataTask.execute(Utilities.FORECAST_WEATHER,cities.get(i));
                main.goToWeatherDataFragment();
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) citiesListView.findViewById(R.id.add_city_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder addCityDialogueBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = getLayoutInflater(savedInstanceState);
                View v = layoutInflater.inflate(R.layout.search_city_dialog,null,false);
                addCityDialogueBuilder.setView(v);

                TextView textView = v.findViewById(R.id.prompt_in_dialogue);
                textView.setText(R.string.add_city);

                final EditText editText = (EditText) v.findViewById(R.id.city_search);
                addCityDialogueBuilder.setPositiveButton(R.string.add, null);
                addCityDialogueBuilder.setCancelable(false);
                final AlertDialog addDialogue = addCityDialogueBuilder.create();

                //TO HANDLE THE SEARCH BUTTON CLICK EVENT
                addDialogue.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button addButton = addDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!cities.contains(editText.getText().toString())) {
                                    cities.add(editText.getText().toString());
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(getContext(),"City already saved",Toast.LENGTH_SHORT).show();
                                }
                                addDialogue.dismiss();
                            }
                        });
                    }
                });
                addDialogue.show();
            }
        });

        return citiesListView;
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
