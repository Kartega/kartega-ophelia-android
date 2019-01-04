package com.ahmetkilic.ophelia.ea_spinner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerMultipleListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerMultipleSelectionListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerOperationsListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerSingleListener;
import com.ahmetkilic.ophelia.ea_spinner.interfaces.EASpinnerSingleSelectionListener;
import com.ahmetkilic.ophelia.ea_utilities.tools.Calculator;
import com.ahmetkilic.ophelia.ea_utilities.tools.ObjectUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.StringUtils;
import com.ahmetkilic.ophelia.ea_utilities.views.DrawableTintTextView;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EASpinner<T> extends DrawableTintTextView implements
        View.OnClickListener,
        EASpinnerSingleSelectionListener<T>,
        EASpinnerMultipleSelectionListener<T> {

    private int layoutID;

    private EASpinnerOperationsListener operationsListener;
    private HashMap<String, String> params;
    private String URL;
    private int method;
    private String searchKey;
    private Class clazz;

    private EASpinnerSingleListener<T> singleListener;
    private EASpinnerMultipleListener<T> multipleListener;

    private List<T> initialItems;
    private List<T> selectedItems;
    private T selectedItem;

    private String defaultText;

    private AttributeSet attrs;
    private int defStyleAttr;
    private boolean noArrow;

    public EASpinner(Context context) {
        super(context);
        this.attrs = null;
        this.defStyleAttr = 0;
        init(context);
    }

    public EASpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        this.defStyleAttr = 0;
        init(context);
        checkDrawable(context);
    }

    public EASpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        init(context);
        checkDrawable(context);
    }

    private void checkDrawable(Context context) {
        Drawable arrow = getCompoundDrawables()[2];
        if (noArrow)
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else if (arrow == null) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
            setCompoundDrawablePadding((int) Calculator.dpFromPx(context, context.getResources().getDimension(R.dimen.ea_spinner_arrow_padding)));
            loadTint(context, attrs, defStyleAttr);
        }
    }

    private void init(Context context) {
        if (context instanceof EASpinnerOperationsListener)
            this.operationsListener = (EASpinnerOperationsListener) context;

        selectedItems = new ArrayList<>();
        initialItems = new ArrayList<>();
        selectedItem = null;
        params = new HashMap<>();
        defaultText = getContext().getString(R.string.ea_spinner_select);
        setText(defaultText);
        setClickable(true);
        setFocusable(true);
        setOnClickListener(this);
        setSearchKey("q");
        setMethod(Request.Method.GET);
    }

    /**
     * Todo There is a known bug exists in this function!
     *
     * @param noArrow If true, arrow won't be shown
     */
    public void setNoArrow(boolean noArrow) {
        this.noArrow = noArrow;
        checkDrawable(getContext());
    }

    /**
     * Set a custom layout for spinner dialog with same ids in default layout.
     *
     * @param layoutID Layout resource id
     */
    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    /**
     * Set the default text shown when there is no item is selected.
     *
     * @param defaultText string to show
     */
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    /**
     * OperationsListener is required for online spinners. It can be used from context or manually set with this function
     *
     * @param operationsListener listener interface
     */
    public void setOperationsListener(EASpinnerOperationsListener operationsListener) {
        this.operationsListener = operationsListener;
    }

    /**
     * All parameters for online spinners can be set.
     *
     * @param params online spinner parameters
     */
    public void setParams(HashMap<String, String> params) {
        this.params.clear();
        this.params.putAll(params);
    }

    /**
     * All parameters for online spinners can be cleared.
     */
    public void clearParams() {
        params.clear();
    }

    /**
     * Add a single parameter for online spinners.
     *
     * @param key   parameter key
     * @param value parameter value
     */
    public void addParam(String key, String value) {
        params.put(key, value);
    }

    /**
     * Online spinners request method. Default method is 'GET'.
     *
     * @param method Request.Method.Value
     */
    public void setMethod(int method) {
        this.method = method;
    }

    /**
     * Search key parameter for online spinners.
     *
     * @param searchKey Search key string.
     */
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == getId()) {
            showSpinnerDialog();
        }
    }

    @Override
    public void onItemsSelected(List<EASpinnerItem<T>> eaSpinnerItems) {
        selectedItems.clear();
        setText(defaultText);

        if (ObjectUtils.arrayIsNotEmpty(eaSpinnerItems)) {
            StringBuilder string = new StringBuilder();
            for (EASpinnerItem<T> eaSpinnerItem : eaSpinnerItems) {
                string.append(eaSpinnerItem.toString()).append(", ");
                selectedItems.add(eaSpinnerItem.getObject());
            }
            string = new StringBuilder(StringUtils.removeLastTwoCharacter(string.toString()));
            setText(string.toString());
        }

        multipleListener.onItemsSelected(EASpinner.this, selectedItems);
    }

    @Override
    public void onItemSelected(EASpinnerItem<T> eaSpinnerItem) {
        if (eaSpinnerItem != null && eaSpinnerItem.getObject() != null) {
            selectedItem = eaSpinnerItem.getObject();
            setText(eaSpinnerItem.toString());
        } else {
            selectedItem = null;
            setText(defaultText);
        }
        singleListener.onItemSelected(EASpinner.this, selectedItem);
    }

    /**
     * Initialize spinner for single selection and with a list of items.
     *
     * @param initialItems   spinner's item list
     * @param singleListener item select listener
     */
    public void initLocalSingle(List<T> initialItems, EASpinnerSingleListener<T> singleListener) {
        this.singleListener = singleListener;
        this.initialItems.clear();
        if (initialItems != null)
            this.initialItems.addAll(initialItems);
    }

    /**
     * Initialize spinner for multiple selection and with a list of items.
     *
     * @param initialItems     spinner's item list
     * @param multipleListener item select listener
     */
    public void initLocalMultiple(List<T> initialItems, EASpinnerMultipleListener<T> multipleListener) {
        this.multipleListener = multipleListener;
        this.initialItems.clear();
        if (initialItems != null)
            this.initialItems.addAll(initialItems);
    }

    /**
     * Initialize spinner for single selection and online data.
     *
     * @param URL            request address for data
     * @param clazz          class of item
     * @param singleListener item select listener
     */
    public void initOnlineSingle(String URL, Class clazz, EASpinnerSingleListener<T> singleListener) {
        this.singleListener = singleListener;
        this.clazz = clazz;
        this.URL = URL;
    }

    /**
     * Initialize spinner for multiple selection and online data.
     *
     * @param URL              request address for data
     * @param clazz            class of item
     * @param multipleListener item select listener
     */
    public void initOnlineMultiple(String URL, Class clazz, EASpinnerMultipleListener<T> multipleListener) {
        this.multipleListener = multipleListener;
        this.clazz = clazz;
        this.URL = URL;
    }

    private void showSpinnerDialog() {
        EASpinnerDialog spinnerDialog = null;
        if (!StringUtils.isEmptyString(URL) && clazz != null) {
            if (singleListener != null) {
                spinnerDialog = new EASpinnerDialog(getContext(), URL, clazz, (EASpinnerSingleSelectionListener) this);
            } else if (multipleListener != null) {
                spinnerDialog = new EASpinnerDialog(getContext(), URL, clazz, (EASpinnerMultipleSelectionListener) this);
            }
        } else {
            List<EASpinnerItem> eaSpinnerItems = new ArrayList<>();
            if (ObjectUtils.arrayIsNotEmpty(initialItems)) {
                for (T item : initialItems) {
                    eaSpinnerItems.add(new EASpinnerItem<>(item));
                }
            }
            if (singleListener != null) {
                spinnerDialog = new EASpinnerDialog(getContext(), eaSpinnerItems, (EASpinnerSingleSelectionListener) this);
            } else if (multipleListener != null) {
                spinnerDialog = new EASpinnerDialog(getContext(), eaSpinnerItems, (EASpinnerMultipleSelectionListener) this);
            }
        }

        if (spinnerDialog != null) {
            List<EASpinnerItem> items = new ArrayList<>();
            if (singleListener == null) {
                for (T item : selectedItems) {
                    EASpinnerItem eaSpinnerItem = new EASpinnerItem<>(item);
                    eaSpinnerItem.setChecked(true);
                    eaSpinnerItem.setShowCheckBox(true);
                    items.add(eaSpinnerItem);
                }
            } else {
                if (selectedItem != null) {
                    EASpinnerItem eaSpinnerItem = new EASpinnerItem<>(selectedItem);
                    eaSpinnerItem.setChecked(true);
                    items.add(eaSpinnerItem);
                }
            }
            spinnerDialog.setSelectedItems(items);
            spinnerDialog.setOperationsListener(operationsListener);
            spinnerDialog.setMethod(method);
            spinnerDialog.setParams(params);
            spinnerDialog.setSearchKey(searchKey);
            spinnerDialog.setLayoutID(layoutID);
            spinnerDialog.show();
        }
    }

}
