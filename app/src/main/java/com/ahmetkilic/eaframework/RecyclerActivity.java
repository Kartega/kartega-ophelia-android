package com.ahmetkilic.eaframework;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import com.ahmetkilic.eaframework.holder.TempHolder;
import com.ahmetkilic.eaframework.object.TempObject;
import com.ahmetkilic.ophelia.EABaseActivity;
import com.ahmetkilic.ophelia.ea_recycler.EARecyclerHelper;
import com.ahmetkilic.ophelia.ea_recycler.EARecyclerView;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerOperationsListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends EABaseActivity implements EARecyclerOperationsListener {
    private EARecyclerHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        EARecyclerView recyclerView = findViewById(R.id.recycler_view);

        helper = new EARecyclerHelper(this, recyclerView);
        helper.addNewHolder(TempHolder.class);

        helper.insertAll(getList(), false);
    }

    private List<TempObject> getList() {
        List<TempObject> list = new ArrayList<>();
        for (int i = helper.getObjectList().size() - 1; i < helper.getObjectList().size() + 25; i++) {
            list.add(new TempObject("Item " + String.valueOf(i + 1)));
        }
        return list;
    }

    @Override
    public void onProgressRequired(boolean show) {

    }

    @Override
    public void onNoDataViewRequired(boolean show) {

    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                helper.insertAll(getList(), true);
            }
        }, 2000);
    }
}
