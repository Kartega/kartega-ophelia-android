package com.ahmetkilic.ophelia.ea_recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_recycler.holders.BaseHolder;
import com.ahmetkilic.ophelia.ea_recycler.holders.ProgressViewHolderVertical;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EATypeInterface;
import com.ahmetkilic.ophelia.ea_recycler.objects.ProgressObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class EABaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private boolean VERTICAL;
    private List<Object> items;
    private List<Class<? extends BaseHolder>> classes;
    private int progressViewLayoutId;
    private EARecyclerClickListener eaRecyclerClickListener;

    EABaseAdapter(Context context, List<Object> items, int progressViewLayoutId) {
        this.context = context;
        this.progressViewLayoutId = progressViewLayoutId;
        this.items = items;
        classes = new ArrayList<>();
        VERTICAL = true;
        if (context instanceof EARecyclerClickListener)
            eaRecyclerClickListener = (EARecyclerClickListener) context;
    }

    void setEaRecyclerClickListener(EARecyclerClickListener eaRecyclerClickListener) {
        this.eaRecyclerClickListener = eaRecyclerClickListener;
    }

    void addClass(Class<? extends BaseHolder> clazz) {
        classes.add(clazz);
    }

    void setOrientation(boolean vertical) {
        this.VERTICAL = vertical;
    }

    void showLoadProgress() {
        if (VERTICAL)
            items.add(new ProgressObject(progressViewLayoutId, EARecyclerHelper.PROGRESS_VERTICAL));
        else
            items.add(new ProgressObject(progressViewLayoutId, EARecyclerHelper.PROGRESS_HORIZONTAL));

        notifyItemInserted(items.size() - 1);
    }

    void hideLoadProgress() {
        items.remove(items.size() - 1);
        notifyItemRemoved(items.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof EATypeInterface) {
            return ((EATypeInterface) (items.get(position))).getRecyclerType();
        } else
            return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        for (Class<? extends BaseHolder> clazz : classes) {
            try {
                BaseHolder object = clazz.asSubclass(BaseHolder.class).getConstructor(View.class).newInstance(new View(context));
                if (object.getViewType() == viewType) {
                    View view = inflater.inflate(object.getLayoutID(), parent, false);
                    return clazz.getConstructor(View.class).newInstance(view);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        Log.e("recycler_type_error", "BASE ADAPTER--> View type is not exists : " + String.valueOf(viewType));
        return new ProgressViewHolderVertical(inflater.inflate(R.layout.ea_recycler_progress_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseHolder baseHolder = (BaseHolder) holder;
        baseHolder.loadItems(context, items.get(position), position, eaRecyclerClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
