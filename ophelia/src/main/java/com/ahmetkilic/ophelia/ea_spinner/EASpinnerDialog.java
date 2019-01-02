package com.ahmetkilic.ophelia.ea_spinner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_networking.EAErrorListener;
import com.ahmetkilic.ophelia.ea_networking.EAListResponseListener;
import com.ahmetkilic.ophelia.ea_networking.EARequestBuilder;
import com.ahmetkilic.ophelia.ea_recycler.EARecyclerHelper;
import com.ahmetkilic.ophelia.ea_recycler.EARecyclerView;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerOperationsListener;
import com.ahmetkilic.ophelia.ea_recycler.objects.ProgressObject;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerMultipleSelectionListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerOperationsListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerSingleSelectionListener;
import com.ahmetkilic.ophelia.ea_utilities.tools.ObjectUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.ViewTools;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class EASpinnerDialog<T> extends Dialog implements
        EARecyclerOperationsListener,
        View.OnClickListener,
        EARecyclerClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private int layoutID;

    private boolean isLocal;
    private boolean multipleSelectionEnabled;
    private String URL;
    private int method;
    private HashMap<String, String> params;

    private String searchKey;
    private String lastQuery = "";

    private Class clazz;

    private Handler handler;
    private Runnable runnable;

    private SearchView searchView;
    private EARecyclerHelper helper;
    private EARecyclerView recyclerView;
    private View progressView, noDataView;
    private SwipeRefreshLayout refreshLayout;

    private EASpinnerSingleSelectionListener<T> singleSelectionListener;
    private EASpinnerMultipleSelectionListener<T> multipleSelectionListener;

    private EASpinnerOperationsListener operationsListener;

    private Button btnSelectAll;

    private List<EASpinnerItem<T>> initialItems;
    private List<EASpinnerItem<T>> selectedItems;

    EASpinnerDialog(@NonNull Context context, List<EASpinnerItem<T>> items, EASpinnerSingleSelectionListener<T> singleSelectionListener) {
        super(context);
        this.URL = "";
        this.isLocal = true;
        this.initialItems = new ArrayList<>(items);
        this.selectedItems = new ArrayList<>();
        this.singleSelectionListener = singleSelectionListener;
    }

    EASpinnerDialog(@NonNull Context context, List<EASpinnerItem<T>> items, EASpinnerMultipleSelectionListener<T> multipleSelectionListener) {
        super(context);
        this.URL = "";
        this.isLocal = true;
        this.multipleSelectionEnabled = true;
        this.multipleSelectionListener = multipleSelectionListener;
        this.initialItems = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
        for (EASpinnerItem<T> eaSpinnerItem : items) {
            eaSpinnerItem.setShowCheckBox(true);
            this.initialItems.add(eaSpinnerItem);
        }
    }

    EASpinnerDialog(@NonNull Context context, String url, Class clazz, EASpinnerSingleSelectionListener<T> singleSelectionListener) {
        super(context);
        this.URL = url;
        this.clazz = clazz;
        this.isLocal = false;
        this.initialItems = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
        this.singleSelectionListener = singleSelectionListener;
    }

    EASpinnerDialog(@NonNull Context context, String url, Class clazz, EASpinnerMultipleSelectionListener<T> multipleSelectionListener) {
        super(context);
        this.URL = url;
        this.clazz = clazz;
        this.isLocal = false;
        this.initialItems = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
        this.multipleSelectionEnabled = true;
        this.multipleSelectionListener = multipleSelectionListener;
    }

    void setSelectedItems(List<EASpinnerItem<T>> selectedItems) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
    }

    @SuppressWarnings("all")
    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutID == 0)
            layoutID = R.layout.ea_spinner_fragment;
        setContentView(layoutID);
        init();
        initSearch();
    }

    @Override
    public void show() {
        super.show();
        if (isLocal) {
            refreshLayout.setEnabled(false);
            loadLocalData();
        } else {
            getOnlineData(false);
        }
    }

    private void init() {
        Button btnClose = findViewById(R.id.btn_close);
        btnSelectAll = findViewById(R.id.btn_select_all);

        btnClose.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);

        method = Request.Method.GET;
        progressView = findViewById(R.id.progress_dialog);
        noDataView = findViewById(R.id.view_no_data);

        refreshLayout = findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.recycler_spinner_dialog);
        helper = new EARecyclerHelper(getContext(), recyclerView);
        helper.setEaRecyclerOperationsListener(this);
        helper.setEaRecyclerClickListener(this);
        helper.addNewHolder(EAItemHolder.class);

        if (multipleSelectionEnabled) {
            btnClose.setText(getContext().getString(R.string.ea_spinner_ok));
        } else {
            btnSelectAll.setText(getContext().getString(R.string.ea_spinner_clear));
            btnClose.setText(getContext().getString(R.string.ea_spinner_close));
        }
    }

    private void initSearch() {
        searchKey = "q";

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                search();
            }
        };

        searchView = findViewById(R.id.search_ea_spinner);
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals(lastQuery))
                    triggerSearch();
                else
                    handler.removeCallbacks(runnable);
                return false;
            }
        });
    }

    private void triggerSearch() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    private void search() {
        lastQuery = searchView.getQuery().toString();
        if (isLocal) {
            if (lastQuery.length() > 0) {
                List<EASpinnerItem> resultItems = new ArrayList<>();

                for (Object item : initialItems) {
                    if (item instanceof EASpinnerItem)
                        if (item.toString().toUpperCase().contains(lastQuery.toUpperCase()))
                            resultItems.add((EASpinnerItem) item);
                }

                helper.insertAll(resultItems, false);
            } else {
                helper.insertAll(initialItems, false);
            }
        } else {
            getOnlineData(false);
        }
    }

    private void getOnlineData(final boolean isLoadMore) {
        helper.prepare(isLoadMore);

        EARequestBuilder builder = operationsListener.getRequestBuilder()
                .setMethod(method)
                .setResponseClass(clazz)
                .setUrl(URL)
                .addParams("Page", String.valueOf(helper.getPage()))
                .addParams(searchKey, lastQuery);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParams(entry.getKey(), entry.getValue());
            }
        }

        builder.setListResponseListener(new EAListResponseListener() {
            @Override
            public void onResponse(ArrayList list) {
                if (ObjectUtils.arrayIsNotEmpty(list)) {
                    List<EASpinnerItem> resultList = new ArrayList<>();
                    for (Object object : list) {
                        EASpinnerItem eaSpinnerItem = new EASpinnerItem<>(object);
                        if (selectedItems.contains(eaSpinnerItem))
                            eaSpinnerItem.setChecked(true);
                        if (multipleSelectionEnabled)
                            eaSpinnerItem.setShowCheckBox(true);
                        resultList.add(eaSpinnerItem);
                    }
                    helper.insertAll(resultList, isLoadMore);
                    reloadCheckAllButton();
                }
            }
        }).setErrorListener(new EAErrorListener() {
            @Override
            public void onError(Object error) {
                operationsListener.handleServerError(error);
                helper.insertAll(null, isLoadMore);
            }
        }).fetch();
    }

    private void loadLocalData() {
        helper.disableLoadMore();
        if (ObjectUtils.arrayIsNotEmpty(selectedItems)) {
            for (EASpinnerItem eaSpinnerItem : selectedItems) {
                initialItems.get(initialItems.indexOf(eaSpinnerItem)).setChecked(true);
            }
        }
        helper.insertAll(initialItems, false);
        reloadCheckAllButton();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close) {
            if (multipleSelectionEnabled) {
                if (isLocal) {
                    List<EASpinnerItem<T>> selectedItems = new ArrayList<>();
                    for (EASpinnerItem<T> eaSpinnerItem : initialItems) {
                        if (eaSpinnerItem.isChecked())
                            selectedItems.add(eaSpinnerItem);
                    }
                    multipleSelectionListener.onItemsSelected(selectedItems);
                } else
                    multipleSelectionListener.onItemsSelected(selectedItems);
            }
            dismiss();
        } else if (v.getId() == R.id.btn_select_all) {
            if (multipleSelectionEnabled) {
                if (isNoDataVisible()) {
                    selectedItems.clear();
                    multipleSelectionListener.onItemsSelected(selectedItems);
                    dismiss();
                } else if (isLocal) {
                    if (initialItems.size() > 0) {
                        if (isAllChecked()) {
                            for (EASpinnerItem eaSpinnerItem : initialItems)
                                eaSpinnerItem.setChecked(false);
                            btnSelectAll.setText(getContext().getString(R.string.ea_spinner_select_all));
                        } else {
                            for (EASpinnerItem eaSpinnerItem : initialItems)
                                eaSpinnerItem.setChecked(true);
                            btnSelectAll.setText(getContext().getString(R.string.ea_spinner_deselect_all));
                        }
                        helper.validateItems();
                    }
                } else {
                    if (helper.getObjectList().size() > 0) {
                        if (isAllChecked()) {
                            for (Object object : helper.getObjectList()) {
                                if (object instanceof EASpinnerItem) {
                                    selectedItems.remove(object);
                                    ((EASpinnerItem) object).setChecked(false);
                                }
                            }
                            btnSelectAll.setText(getContext().getString(R.string.ea_spinner_select_all));
                        } else {
                            EASpinnerItem<T> tempInstance = new EASpinnerItem<>();
                            //Class<T> tClass = (Class<T>) ((ParameterizedType) tempInstance.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                            for (Object object : helper.getObjectList()) {
                                if (tempInstance.getClass().isInstance(object)){
                                    EASpinnerItem t = tempInstance.getClass().cast(object);
                                    if (!selectedItems.contains(t)) {
                                        selectedItems.add(t);
                                    }
                                    ((EASpinnerItem) object).setChecked(true);
                                }
                            }
                            btnSelectAll.setText(getContext().getString(R.string.ea_spinner_deselect_all));
                        }
                        helper.validateItems();
                    }
                }
            } else {
                if (singleSelectionListener != null)
                    singleSelectionListener.onItemSelected(null);
                dismiss();
            }
        }
    }

    private void reloadCheckAllButton() {
        if (multipleSelectionEnabled && !isNoDataVisible()) {
            if (isAllChecked()) {
                btnSelectAll.setText(getContext().getString(R.string.ea_spinner_deselect_all));
            } else {
                btnSelectAll.setText(getContext().getString(R.string.ea_spinner_select_all));
            }
        } else
            btnSelectAll.setText(getContext().getString(R.string.ea_spinner_clear));
    }

    private boolean isAllChecked() {
        boolean allChecked = true;
        if (isLocal) {
            for (EASpinnerItem eaSpinnerItem : initialItems) {
                if (!eaSpinnerItem.isChecked()) {
                    allChecked = false;
                    break;
                }
            }
        } else {
            for (Object object : helper.getObjectList()) {
                if (object instanceof EASpinnerItem && !((EASpinnerItem) object).isChecked()) {
                    allChecked = false;
                    break;
                }
            }
        }
        return allChecked;
    }

    @Override
    public void onEARecyclerItemClick(Object object, int position) {
        if (position < 0)
            return;
        if (object instanceof ProgressObject)
            return;

        if (multipleSelectionEnabled) {
            if (object instanceof EASpinnerItem) {
                if (((EASpinnerItem) object).isChecked())
                    selectedItems.remove(object);
                else
                    selectedItems.add((EASpinnerItem) object);

                ((EASpinnerItem) helper.getObjectList().get(position)).setChecked(!((EASpinnerItem) object).isChecked());
                helper.validateItem(position);
            }
            reloadCheckAllButton();
        } else {
            if (singleSelectionListener != null)
                singleSelectionListener.onItemSelected((EASpinnerItem) object);
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        ViewTools.hideKeyboardFrom(getContext(), searchView);
        super.dismiss();
    }

    void setOperationsListener(EASpinnerOperationsListener operationsListener) {
        this.operationsListener = operationsListener;
    }

    void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    void setMethod(int method) {
        this.method = method;
    }

    private boolean isNoDataVisible() {
        return noDataView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onProgressRequired(boolean show) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNoDataViewRequired(boolean required) {
        noDataView.setVisibility(required ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(required ? View.GONE : View.VISIBLE);
        reloadCheckAllButton();
    }

    @Override
    public void onLoadMore() {
        getOnlineData(true);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        getOnlineData(false);
    }
}
