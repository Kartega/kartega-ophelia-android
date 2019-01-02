package com.ahmetkilic.eaframework;

import android.os.Bundle;
import android.view.View;


import com.ahmetkilic.eaframework.object.Album;
import com.ahmetkilic.ophelia.EABaseActivity;
import com.ahmetkilic.ophelia.ea_networking.EARequestBuilder;
import com.ahmetkilic.ophelia.ea_networking.EAVolleyHelper;
import com.ahmetkilic.ophelia.ea_spinner.EASpinner;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerMultipleListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerOperationsListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerSingleListener;

import java.util.ArrayList;
import java.util.List;

public class SpinnerActivity extends EABaseActivity implements EASpinnerOperationsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        List<Album> items = new ArrayList<>();
        for (int i = 1; i < 45; i++) {
            items.add(new Album(i, "Test Object " + String.valueOf(i + 1)));
        }

        String url = "https://jsonplaceholder.typicode.com/albums";

        EASpinner<Album> spinner_single_local = findViewById(R.id.eaSpinner_single_local);
        spinner_single_local.setLayoutID(R.layout.custom_spinner_dialog);

        EASpinner<Album> spinner_single_online = findViewById(R.id.eaSpinner_single_online);
        EASpinner<Album> spinner_multiple_local = findViewById(R.id.eaSpinner_multiple_local);
        EASpinner<Album> spinner_multiple_online = findViewById(R.id.eaSpinner_multiple_online);
        EASpinner<Album> spinner_no_data_local = findViewById(R.id.eaSpinner_no_data_local);

        EASpinner<Album> spinner_no_data_online = findViewById(R.id.eaSpinner_no_data_online);
        spinner_no_data_online.setNoArrow(true);

        spinner_single_local.initLocalSingle(items, new EASpinnerSingleListener<Album>() {
            @Override
            public void onItemSelected(View spinner, Album selection) {

            }
        });

        spinner_single_online.initOnlineSingle(url, Album.class, new EASpinnerSingleListener<Album>() {
            @Override
            public void onItemSelected(View spinner, Album selection) {

            }
        });

        spinner_multiple_local.initLocalMultiple(items, new EASpinnerMultipleListener<Album>() {
            @Override
            public void onItemsSelected(View spinner, List<Album> selections) {

            }
        });

        spinner_multiple_online.initOnlineMultiple(url, Album.class, new EASpinnerMultipleListener<Album>() {
            @Override
            public void onItemsSelected(View spinner, List<Album> selections) {

            }
        });

        spinner_no_data_local.initLocalMultiple(null, new EASpinnerMultipleListener<Album>() {
            @Override
            public void onItemsSelected(View spinner, List<Album> selections) {

            }
        });


        spinner_no_data_online.initOnlineMultiple(url + "qwe", Album.class, new EASpinnerMultipleListener<Album>() {
            @Override
            public void onItemsSelected(View spinner, List<Album> selections) {

            }
        });
    }

    @Override
    public EARequestBuilder getRequestBuilder() {
        return new EARequestBuilder().setHelper(new EAVolleyHelper(this));
    }

    @Override
    public void handleServerError(Object error) {

    }
}
