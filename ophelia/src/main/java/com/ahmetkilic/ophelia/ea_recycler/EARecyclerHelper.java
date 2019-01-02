package com.ahmetkilic.ophelia.ea_recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.animation.Interpolator;


import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_recycler.animation.adapters.AlphaInAnimationAdapter;
import com.ahmetkilic.ophelia.ea_recycler.animation.adapters.AnimationAdapter;
import com.ahmetkilic.ophelia.ea_recycler.animation.adapters.ScaleInAnimationAdapter;
import com.ahmetkilic.ophelia.ea_recycler.animation.adapters.SlideInBottomAnimationAdapter;
import com.ahmetkilic.ophelia.ea_recycler.animation.adapters.SlideInLeftAnimationAdapter;
import com.ahmetkilic.ophelia.ea_recycler.animation.adapters.SlideInRightAnimationAdapter;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.BaseItemAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FadeInAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FadeInDownAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FadeInLeftAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FadeInRightAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FadeInUpAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FlipInBottomXAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FlipInLeftYAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FlipInRightYAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.FlipInTopXAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.LandingAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.OvershootInLeftAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.OvershootInRightAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.ScaleInAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.ScaleInBottomAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.ScaleInLeftAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.ScaleInRightAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.ScaleInTopAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.SlideInDownAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.SlideInLeftAnimator;
import com.ahmetkilic.ophelia.ea_recycler.animation.animators.SlideInRightAnimator;
import com.ahmetkilic.ophelia.ea_recycler.enums.AdapterAnimation;
import com.ahmetkilic.ophelia.ea_recycler.enums.ItemAnimation;
import com.ahmetkilic.ophelia.ea_recycler.holders.BaseHolder;
import com.ahmetkilic.ophelia.ea_recycler.holders.ProgressViewHolderHorizontal;
import com.ahmetkilic.ophelia.ea_recycler.holders.ProgressViewHolderVertical;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerClickListener;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EARecyclerOperationsListener;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EATitleInterface;
import com.ahmetkilic.ophelia.ea_recycler.interfaces.EATypeInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
@SuppressWarnings("unused")
public class EARecyclerHelper implements EARecyclerView.LoadMoreListener {

    private EARecyclerView recyclerView;
    private EABaseAdapter recycleAdapter;
    private List<Object> objectList;

    private boolean loadMoreIsActive;
    private boolean loadMoreDisabled;
    private Context context;
    private EARecyclerOperationsListener eaRecyclerOperationsListener;

    private int pageSize;
    private int page;

    public static final int PROGRESS_HORIZONTAL = 980;
    public static final int PROGRESS_VERTICAL = 990;

    public EARecyclerHelper(Context context, EARecyclerView myRecyclerView) {
        this.context = context;
        this.recyclerView = myRecyclerView;
        init(R.layout.ea_progress_item);
    }

    public EARecyclerHelper(Context context, EARecyclerView myRecyclerView, int progressViewLayoutId) {
        this.context = context;
        this.recyclerView = myRecyclerView;

        init(progressViewLayoutId);
    }

    private void init(int progressViewLayoutId) {
        this.objectList = new ArrayList<>();
        this.recycleAdapter = new EABaseAdapter(context, this.objectList, progressViewLayoutId);

        setOrientation(true);

        if (context instanceof EARecyclerOperationsListener)
            this.eaRecyclerOperationsListener = (EARecyclerOperationsListener) context;

        loadMoreIsActive = true;
        setPage(1);
        setPageSize(20);

        this.recyclerView.setLoadingListener(this);
        this.recyclerView.setAdapter(this.recycleAdapter);

        addNewHolder(ProgressViewHolderVertical.class);
        addNewHolder(ProgressViewHolderHorizontal.class);
    }

    /**
     * Set the item count per request in load more.
     *
     * @param pageSize
     */
    @SuppressWarnings("all")
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Set the click listener for recycler view
     *
     * @param eaRecyclerClickListener click listener
     */
    public void setEaRecyclerClickListener(EARecyclerClickListener eaRecyclerClickListener) {
        recycleAdapter.setEaRecyclerClickListener(eaRecyclerClickListener);
    }

    /**
     * Set the operations listener for recycler view's requirements
     *
     * @param eaRecyclerOperationsListener operations listener
     */
    public void setEaRecyclerOperationsListener(EARecyclerOperationsListener eaRecyclerOperationsListener) {
        this.eaRecyclerOperationsListener = eaRecyclerOperationsListener;
    }

    /**
     * Add a new holder class to the adapter.
     *
     * @param clazz class of the holder
     */
    public void addNewHolder(Class<? extends BaseHolder> clazz) {
        recycleAdapter.addClass(clazz);
    }

    private void showProgress(boolean show) {
        if (eaRecyclerOperationsListener != null)
            eaRecyclerOperationsListener.onProgressRequired(show);
    }

    private void showNoDataMessage(boolean show) {
        if (eaRecyclerOperationsListener != null)
            eaRecyclerOperationsListener.onNoDataViewRequired(show);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreIsActive && eaRecyclerOperationsListener != null) {
            recycleAdapter.showLoadProgress();
            eaRecyclerOperationsListener.onLoadMore();
        }
    }

    /**
     * Prepare for getting the data
     *
     * @param loadMore is load more
     */
    public void prepare(boolean loadMore) {
        showNoDataMessage(false);
        if (!loadMore) {
            setPage(1);
            showProgress(true);
        }
    }

    /**
     * Insert a list of object
     *
     * @param objects list of objects
     * @param loadMore insert as load more or refresh
     */
    public void insertAll(Collection<? extends EATypeInterface> objects, boolean loadMore) {
        if (loadMore) {
            if (recyclerView.isLoadingData())
                recycleAdapter.hideLoadProgress();
            recyclerView.loadMoreComplete();
            if (objects == null) {
                deactivateLoadMore();
            } else {
                if (objects.size() == 0) {
                    deactivateLoadMore();
                } else {
                    objectList.addAll(objects);
                    recycleAdapter.notifyItemRangeInserted(objectList.size() - objects.size(), objects.size());
                }
            }
        } else {
            objectList.clear();
            setPage(1);
            if (objects == null || objects.size() == 0) {
                showNoDataMessage(true);
                deactivateLoadMore();
            } else {
                objectList.addAll(objects);
                calculatePageWithoutTitles(objects);
                activateLoadMore();
            }
            recycleAdapter.notifyDataSetChanged();
            showProgress(false);
        }
    }

    /**
     * Insert a single object
     *
     * @param object object
     */
    public void insert(Object object) {
        objectList.add(object);
        recycleAdapter.notifyItemInserted(objectList.size() - 1);
        recalculatePages();
    }

    /**
     * Change an item
     *
     * @param position position of the old object
     * @param item     new object to update
     */
    public void updateItem(int position, Object item) {
        objectList.set(position, item);
        validateItem(position);
    }

    /**
     * Remove an item
     *
     * @param position position to remove
     */
    @SuppressWarnings("All")
    public void removeItem(int position) {
        objectList.remove(position);
        recycleAdapter.notifyItemRemoved(position);
        recycleAdapter.notifyItemRangeChanged(position, objectList.size());
        recalculatePages();
    }

    /**
     * Remove an item
     *
     * @param object object to remove
     */
    public void removeItem(Object object) {
        int position = objectList.indexOf(object);
        if (position > -1)
            removeItem(position);
    }

    private void recalculatePages() {
        int itemCount = 0;
        for (Object item : objectList) {
            if (!(item instanceof EATitleInterface))
                itemCount++;
        }

        if (itemCount >= pageSize) {
            int division = itemCount / pageSize;
            setPage(division + 1);
            if (itemCount % pageSize == 0) {
                activateLoadMore();
            } else {
                deactivateLoadMore();
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------

    private void calculatePageWithoutTitles(Collection<? extends EATypeInterface> objects) {
        int count = 0;
        for (Object object : objects) {
            if (!(object instanceof EATitleInterface)) {
                count++;
            }
        }
        if (count < pageSize) {
            deactivateLoadMore();
        } else {
            setPage(getPage() + 1);
            activateLoadMore();
        }
    }

    /**
     * Get object list
     *
     * @return list of objects
     */
    public List<Object> getObjectList() {
        return objectList;
    }

    /**
     * Get recycler view
     *
     * @return recycler view
     */
    public EARecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Get recycler adapter
     *
     * @return adapter
     */
    public EABaseAdapter getRecycleAdapter() {
        return recycleAdapter;
    }

    /**
     * Get current page
     *
     * @return page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Set the page for server
     *
     * @param page page number
     */
    @SuppressWarnings("All")
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Reload a single item
     *
     * @param position item's position
     */
    public void validateItem(int position) {
        recycleAdapter.notifyItemChanged(position);
    }

    /**
     * Reload items
     */
    public void validateItems() {
        recycleAdapter.notifyDataSetChanged();
    }

    /**
     * Disable the load more functionality for the recycler view
     */
    public void disableLoadMore() {
        this.loadMoreDisabled = true;
        recyclerView.setLoadingMoreEnabled(false);
    }

    private void deactivateLoadMore() {
        this.loadMoreIsActive = false;
        recyclerView.setLoadingMoreEnabled(false);
    }

    private void activateLoadMore() {
        if (!loadMoreDisabled) {
            this.loadMoreIsActive = true;
            recyclerView.setLoadingMoreEnabled(true);
        }
    }

    /**
     * Set true if size of the items is fixed
     *
     * @param hasFixedSize is fixed
     */
    public void setHasFixedSize(boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }


    /**
     * Set the direction of the recycler view
     *
     * @param vertical set true if vertical, set false if horizontal
     */
    public void setOrientation(boolean vertical) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                vertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL,
                false);

        recyclerView.setLayoutManager(layoutManager);
        recycleAdapter.setOrientation(vertical);
    }

    /**
     * Set the adapter animation
     *
     * @param adapterAnimation animation type
     * @param interpolator     interpolator
     * @param firstOnly        enable animation only first time visibility of an item
     */
    public void setAdapterAnimation(AdapterAnimation adapterAnimation, Interpolator interpolator, boolean firstOnly) {
        AnimationAdapter animationAdapter;
        switch (adapterAnimation) {
            case ALPHA_IN:
                animationAdapter = new AlphaInAnimationAdapter(this.recycleAdapter);
                break;
            case SCALE_IN:
                animationAdapter = new ScaleInAnimationAdapter(this.recycleAdapter);
                break;
            case SLIDE_IN_LEFT:
                animationAdapter = new SlideInLeftAnimationAdapter(this.recycleAdapter);
                break;
            case SLIDE_IN_RIGHT:
                animationAdapter = new SlideInRightAnimationAdapter(this.recycleAdapter);
                break;
            case SLIDE_IN_BOTTOM:
                animationAdapter = new SlideInBottomAnimationAdapter(this.recycleAdapter);
                break;
            default:
                animationAdapter = new AlphaInAnimationAdapter(this.recycleAdapter);
                break;
        }
        animationAdapter.setFirstOnly(firstOnly);
        if (interpolator != null)
            animationAdapter.setInterpolator(interpolator);
        this.recyclerView.setAdapter(animationAdapter);
    }

    /**
     * Set item animation for recycler view
     *
     * @param itemAnimation animation type
     * @param interpolator  interpolator
     */
    public void setItemAnimation(ItemAnimation itemAnimation, Interpolator interpolator) {
        BaseItemAnimator animator;
        switch (itemAnimation) {
            case FADE_IN:
                animator = new FadeInAnimator();
                break;
            case FADE_IN_LEFT:
                animator = new FadeInLeftAnimator();
                break;
            case FADE_IN_RIGHT:
                animator = new FadeInRightAnimator();
                break;
            case FADE_IN_UP:
                animator = new FadeInUpAnimator();
                break;
            case FADE_IN_DOWN:
                animator = new FadeInDownAnimator();
                break;
            case FLIP_IN_BOTTOM:
                animator = new FlipInBottomXAnimator();
                break;
            case FLIP_IN_LEFT:
                animator = new FlipInLeftYAnimator();
                break;
            case FLIP_IN_RIGHT:
                animator = new FlipInRightYAnimator();
                break;
            case FLIP_IN_TOP:
                animator = new FlipInTopXAnimator();
                break;
            case LANDING:
                animator = new LandingAnimator();
                break;
            case OVERSHOOT_IN_LEFT:
                animator = new OvershootInLeftAnimator();
                break;
            case OVERSHOOT_IN_RIGHT:
                animator = new OvershootInRightAnimator();
                break;
            case SCALE_IN:
                animator = new ScaleInAnimator();
                break;
            case SCALE_IN_LEFT:
                animator = new ScaleInLeftAnimator();
                break;
            case SCALE_IN_RIGHT:
                animator = new ScaleInRightAnimator();
                break;
            case SCALE_IN_BOTTOM:
                animator = new ScaleInBottomAnimator();
                break;
            case SCALE_IN_TOP:
                animator = new ScaleInTopAnimator();
                break;
            case SLIDE_IN_LEFT:
                animator = new SlideInLeftAnimator();
                break;
            case SLIDE_IN_BOTTOM:
                animator = new SlideInDownAnimator();
                break;
            case SLIDE_IN_TOP:
                animator = new ScaleInTopAnimator();
                break;
            case SLIDE_IN_RIGHT:
                animator = new SlideInRightAnimator();
                break;
            default:
                animator = new FadeInAnimator();
                break;
        }

        if (interpolator != null)
            animator.setInterpolator(interpolator);

        this.recyclerView.setItemAnimator(animator);
    }
}
