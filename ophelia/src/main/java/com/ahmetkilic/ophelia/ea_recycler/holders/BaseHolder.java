package com.ahmetkilic.ophelia.ea_recycler.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder {

    /**
     * Constructor of base holder
     *
     * @param itemView view of recycler child item
     */
    public BaseHolder(View itemView) {
        super(itemView);
    }

    /**
     * Use this to load your values to your view
     *
     * @param context                 context
     * @param object                  object instance of your type of object class
     * @param position                item position in recycler view
     * @param eaRecyclerClickListener click listener
     */
    public abstract void loadItems(Context context, Object object, int position, EARecyclerClickListener eaRecyclerClickListener);

    /**
     * return the layout id for this holder type
     */
    public abstract int getLayoutID();

    /**
     * @return Return the view type value same with the object class's view type value to match the holder and object.
     */
    public abstract int getViewType();
}
