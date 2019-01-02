package com.ahmetkilic.ophelia.ea_recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EARecyclerView extends RecyclerView {

    /**
     * A Recycler view with load more listener.
     */

    public interface LoadMoreListener {
        void onLoadMore();
    }

    private boolean loadingMoreEnabled = false;
    private boolean isLoadingData = false;
    private LoadMoreListener mLoadingListener;

    public EARecyclerView(Context context) {
        super(context);
    }

    public EARecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public EARecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData() && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition = 0;


            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (layoutManager != null && layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 &&
                    layoutManager.getItemCount() > layoutManager.getChildCount()) {

                isLoadingData = true;
                mLoadingListener.onLoadMore();
                if (getAdapter() != null)
                    scrollToPosition(getAdapter().getItemCount() - 1);
            }
        }
    }

    @Override
    public LayoutManager getLayoutManager() {
        return super.getLayoutManager();
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions)
            if (value > max)
                max = value;
        return max;
    }

    public void setLoadingListener(LoadMoreListener listener) {
        mLoadingListener = listener;
        setLoadingMoreEnabled(true);
    }

    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.loadingMoreEnabled = loadingMoreEnabled;
    }

    public void loadMoreComplete() {
        isLoadingData = false;
    }

    public boolean isLoadingData() {
        return isLoadingData;
    }
}
