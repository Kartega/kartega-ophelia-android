package com.ahmetkilic.eaframework.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ahmetkilic.eaframework.R;
import com.ahmetkilic.ophelia.ea_recycler.holders.BaseHolder;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;


public class TempHolder extends BaseHolder {

    private TextView textView;

    public TempHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_row);
    }

    @Override
    public void loadItems(Context context, Object object, int position, EARecyclerClickListener eARecyclerClickListener) {
        textView.setText(object.toString());
    }

    @Override
    public int getLayoutID() {
        return R.layout.row_temp;
    }

    @Override
    public int getViewType() {
        return 10;
    }
}
