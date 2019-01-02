package com.ahmetkilic.ophelia.ea_recycler.holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;
import com.ahmetkilic.ophelia.ea_recycler.objects.ProgressObject;

import static com.ahmetkilic.ophelia.ea_recycler.EARecyclerHelper.PROGRESS_HORIZONTAL;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ProgressViewHolderHorizontal extends BaseHolder {

    private LinearLayout holder;

    public ProgressViewHolderHorizontal(View itemView) {
        super(itemView);
        holder = itemView.findViewById(R.id.ly_progress);
    }

    @Override
    public void loadItems(Context context, Object object, int position, EARecyclerClickListener eaRecyclerClickListener) {
        int progressViewResId = 0;
        if (object != null) {
            ProgressObject progressObject = (ProgressObject) object;
            progressViewResId = progressObject.getProgressViewLayoutId();
        }

        View progress = LayoutInflater.from(context).inflate(progressViewResId == 0 ? R.layout.ea_progress_item : progressViewResId, holder, false);

        holder.removeAllViews();
        holder.addView(progress);
    }

    @Override
    public int getLayoutID() {
        return R.layout.ea_recycler_progress_row_horizontal;
    }

    @Override
    public int getViewType() {
        return PROGRESS_HORIZONTAL;
    }

}