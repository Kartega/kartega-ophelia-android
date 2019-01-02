package com.ahmetkilic.eaframework;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ahmetkilic.ophelia.EABaseActivity;

public class MainActivity extends EABaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_spinner).setOnClickListener(this);
        findViewById(R.id.btn_recycler).setOnClickListener(this);
        findViewById(R.id.btn_progress).setOnClickListener(this);
        findViewById(R.id.btn_utilities).setOnClickListener(this);
        findViewById(R.id.btn_slider).setOnClickListener(this);
        findViewById(R.id.btn_networking).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (isClickDisabled())
            return;
        disableClickTemporarily();
        switch (v.getId()) {
            case R.id.btn_spinner:
                startActivity(new Intent(this, SpinnerActivity.class));
                break;
            case R.id.btn_recycler:
                startActivity(new Intent(this, RecyclerActivity.class));
                break;
            case R.id.btn_progress:
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                    }
                }, 3000);
                break;
            case R.id.btn_utilities:
                startActivity(new Intent(this, UtilitiesActivity.class));
                break;
            case R.id.btn_slider:
                startActivity(new Intent(this, SliderActivity.class));
                break;
            case R.id.btn_networking:
                startActivity(new Intent(this, NetworkingActivity.class));
                break;
            default:
                enableClick();
                super.onClick(v);
                break;

        }
    }

}
