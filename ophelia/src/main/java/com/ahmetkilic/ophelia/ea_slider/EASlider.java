package com.ahmetkilic.ophelia.ea_slider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import java.lang.reflect.Field;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EASlider extends ViewPager {

    private boolean swipeActive, autoScrollActive;
    private int BLOCK_TIME_MILLIS;
    private Runnable runnable;
    private int autoScrollDuration;
    private int scrollDuration;
    private FixedSpeedScroller scroller;

    public EASlider(Context context) {
        super(context);
        initMyPager();
    }

    public EASlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyPager();
    }

    private void initMyPager() {
        enableSwipe();
        setBlockTimeMillis(100);
        setAutoScrollDuration(4000);
        setScrollDuration(300);

        OnPageChangeListener onPageChangeListener = new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                disableSwipeTemporary();
            }
        };
        addOnPageChangeListener(onPageChangeListener);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (autoScrollActive) {
                    setCurrentItem(getCurrentItem() + 1, true);
                    startAutoScroll();
                }
            }
        };

        addCustomScroller();
    }

    private void addCustomScroller() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new FixedSpeedScroller(getContext(), new LinearInterpolator());
            scroller.setFixedDuration(scrollDuration);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    /**
     * Set page scrolling duration in millis
     *
     * @param scrollDuration duration in millis
     */
    public void setScrollDuration(int scrollDuration) {
        this.scrollDuration = scrollDuration;
        if (scroller != null)
            this.scroller.setFixedDuration(scrollDuration);
    }

    /**
     * set Auto scroll duration in millis
     *
     * @param autoScrollDuration duration in millis
     */
    public void setAutoScrollDuration(int autoScrollDuration) {
        this.autoScrollDuration = autoScrollDuration;
    }

    /**
     * Start autoScroll
     *
     */
    public void startAutoScroll() {
        stopAutoScroll();
        autoScrollActive = true;
        postDelayed(runnable, autoScrollDuration);
    }

    /**
     * Stop autoScroll
     *
     */
    public void stopAutoScroll() {
        removeCallbacks(runnable);
        autoScrollActive = false;
    }

    public boolean isAutoScrollActive() {
        return autoScrollActive;
    }

    @Override
    public EASliderAdapter getAdapter() {
        return (EASliderAdapter) super.getAdapter();
    }

    public void setAdapter(EASliderAdapter adapter) {
        super.setAdapter(adapter);
        int temp = ((adapter.getCount() / 2) % adapter.getRealCount());
        setCurrentItem(((adapter.getCount() / 2) - temp));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            removeCallbacks(runnable);

        return swipeActive && super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP && autoScrollActive)
            startAutoScroll();

        return swipeActive && super.onTouchEvent(ev);
    }

    public void enableSwipe() {
        this.swipeActive = true;
    }

    public void disableSwipe() {
        this.swipeActive = false;
    }

    public void disableSwipeTemporary() {
        if (BLOCK_TIME_MILLIS != 0) {
            disableSwipe();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableSwipe();
                }
            }, BLOCK_TIME_MILLIS);
        }
    }

    /**
     * The swipe function is temporarily disabled to prevent rapid swipe.
     *
     * @param blockTimeMillis disable duration in millis
     */
    public void setBlockTimeMillis(int blockTimeMillis) {
        BLOCK_TIME_MILLIS = blockTimeMillis;
    }

    public boolean isSwipeActive() {
        return this.swipeActive;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.itemPosition = getCurrentItem();
        ss.autoScrollStatus = autoScrollActive;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentItem(ss.itemPosition);
        this.autoScrollActive = ss.autoScrollStatus;
        if (autoScrollActive)
            startAutoScroll();
    }

    private static class SavedState extends BaseSavedState {
        int itemPosition;
        boolean autoScrollStatus;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.itemPosition = in.readInt();
            this.autoScrollStatus = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.itemPosition);
            out.writeByte((byte) (autoScrollStatus ? 1 : 0));
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
