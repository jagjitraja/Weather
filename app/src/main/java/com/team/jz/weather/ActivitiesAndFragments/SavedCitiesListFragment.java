package com.team.jz.weather.ActivitiesAndFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team.jz.weather.R;

import java.util.ArrayList;
import java.util.List;

public class SavedCitiesListFragment extends Fragment {


    public SavedCitiesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_cities_list, container, false);
    }


    private class CitiesAdapter extends ArrayAdapter<String>{


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
            return convertView;
        }
    }


}
