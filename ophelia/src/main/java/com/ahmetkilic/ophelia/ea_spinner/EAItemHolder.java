package com.ahmetkilic.ophelia.ea_spinner;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_recycler.holders.BaseHolder;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EAItemHolder extends BaseHolder {

    static final int EA_SPINNER_ITEM = 72836;
    private View layout,container;
    private TextView textView;
    private CheckBox checkBox;

    public EAItemHolder(View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.row_ea_spinner);
        container = itemView.findViewById(R.id.row_container);
        textView = itemView.findViewById(R.id.tv_row_ea);
        checkBox = itemView.findViewById(R.id.cb_row_ea);
    }

    @Override
    public void loadItems(Context context, final Object object, int position, final EARecyclerClickListener eaRecyclerClickListener) {

        if (eaRecyclerClickListener != null) {
            final int final_pos = position;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eaRecyclerClickListener.onEARecyclerItemClick(object, final_pos);
                }
            });
        }

        if (object != null) {
            textView.setText(object.toString());
            if (object instanceof EASpinnerItem) {
                if (((EASpinnerItem) object).isChecked()){
                    container.setBackgroundColor(ContextCompat.getColor(context,R.color.ea_spinner_selected_item_background_color));
                }else{
                    container.setBackgroundColor(0);
                }
                checkBox.setChecked(((EASpinnerItem) object).isChecked());
                checkBox.setVisibility(((EASpinnerItem) object).isShowCheckBox() ? View.VISIBLE : View.GONE);
            }
        }
    }


    @Override
    public int getLayoutID() {
        return R.layout.ea_spinner_row;
    }

    @Override
    public int getViewType() {
        return EA_SPINNER_ITEM;
    }
}
