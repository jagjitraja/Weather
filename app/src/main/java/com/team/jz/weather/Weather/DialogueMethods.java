package com.team.jz.weather.Weather;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.team.jz.weather.ActivitiesAndFragments.SplashActivity;
import com.team.jz.weather.NetworkConnections.DownloadCallback;
import com.team.jz.weather.NetworkConnections.FetchDataTask;
import com.team.jz.weather.R;

/**
 * Created by T00533766 on 11/23/2017.
 */

public class DialogueMethods {

    private Context context;
    private FetchDataTask fetchDataTask;
    public DialogueMethods(Context context,FetchDataTask fetchDataTask){
        this.context = context;
        this.fetchDataTask = fetchDataTask;
    }

    public void showExplanationDialogue(String message, final DownloadCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity)callback);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showSearchDialogue(callback);
            }
        });

        builder.create().show();
    }
    //SEARCH DIALOGUE IS ONLY SHOWN WHEN LOCATION SEARCH FAILED
    public void showSearchDialogue(final DownloadCallback callback) {

        final AlertDialog.Builder searchDialogBuilder = new AlertDialog.Builder((AppCompatActivity)callback);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.search_city_dialog, null);
        searchDialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.city_search);
        searchDialogBuilder.setPositiveButton(R.string.search, null);
        searchDialogBuilder.setCancelable(false);

        final AlertDialog searchDialog = searchDialogBuilder.create();

        //TO HANDLE THE SEARCH BUTTON CLICK EVENT
        searchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button searchButton = searchDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String c = editText.getText().toString();
                        if (c.length() > 0) {
                            if(fetchDataTask==null){
                                fetchDataTask = new FetchDataTask(context,callback);
                            }
                            Log.d(fetchDataTask+"  "+c, "onClick: ");
                            fetchDataTask.execute(Utilities.FORECAST_WEATHER, c);
                            searchDialog.dismiss();
                        } else {
                            AlertDialog.Builder errorBuilder = new AlertDialog.Builder(context);
                            errorBuilder.setTitle(R.string.search_prompt_text);
                            errorBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            errorBuilder.create().show();
                        }
                    }
                });
            }
        });
        searchDialog.show();
    }
}
