package com.ahmetkilic.ophelia.ea_slider;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class FixedSpeedScroller extends Scroller {

    private int mDuration = 500;

    FixedSpeedScroller(Context context) {
        super(context);
    }

    FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    void setFixedDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}