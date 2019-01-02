package com.ahmetkilic.ophelia.ea_slider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EASliderAdapter extends FragmentPagerAdapter {

    private int progressViewResId;
    private int errorViewResId;
    private List<EASlide> EASlides;
    private boolean disableLoopForOneElement;
    private boolean disableLoop;
    private SliderClickListener sliderClickListener;

    public EASliderAdapter(FragmentManager fm) {
        super(fm);
        EASlides = new ArrayList<>();
        setProgressLayoutId(0);
        setDisableLoop(false);
        setDisableLoopForOneElement(true);
    }

    /**
     * Shows this layout as progress while loading the image.
     *
     * @param progressViewResId Layout resource id
     */
    public void setProgressLayoutId(int progressViewResId) {
        this.progressViewResId = progressViewResId;
    }

    /**
     * Sets the click listener for slide images.
     *
     * @param sliderClickListener Click listener interface
     */
    public void setSliderClickListener(SliderClickListener sliderClickListener) {
        this.sliderClickListener = sliderClickListener;
    }
    /**
     * Shows this layout if an error occurred while loading the image.
     *
     * @param errorViewResId Layout resource id
     */
    public void setErrorLayoutId(int errorViewResId) {
        this.errorViewResId = errorViewResId;
    }

    /**
     * Add new EASlide element to the slider.
     *
     * @param EASlide Slide element
     */
    public void addSlide(EASlide EASlide) {
        EASlides.add(EASlide);
        notifyDataSetChanged();
    }

    /**
     * Prevents or allows the loop for the slider.
     *
     * @param disableLoop If true, the loop is disabled.
     */
    public void setDisableLoop(boolean disableLoop) {
        this.disableLoop = disableLoop;
    }

    /**
     * Prevents the loop when 1 element remains in the slider.
     *
     * @param disableLoopForOneElement If true, the loop is disabled.
     */
    public void setDisableLoopForOneElement(boolean disableLoopForOneElement) {
        this.disableLoopForOneElement = disableLoopForOneElement;
    }

    @Override
    public Fragment getItem(int position) {
        return SlideFragment.newInstance(EASlides.get(position % getRealCount()), progressViewResId, errorViewResId, sliderClickListener);
    }

    /**
     * If the loop is enabled, do not use this for getting the count of slides. Use getRealCount()
     */
    @Override
    public int getCount() {
        if (disableLoop)
            return getRealCount();
        else
            return disableLoopForOneElement && getRealCount() == 1 ? 1 : Integer.MAX_VALUE;
    }

    /**
     * Returns the count of slides.
     */
    public int getRealCount() {
        return EASlides.size();
    }
}
