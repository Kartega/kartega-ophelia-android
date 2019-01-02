package com.ahmetkilic.ophelia.ea_slider.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.ahmetkilic.ophelia.R;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class IndicatorAttr {
    int eai_item_size;

    int eai_ic_margin;
    int eai_ic_current_margin;

    Drawable eai_ic_src;
    Drawable eai_ic_current_src;

    IndicatorAttr(AttributeSet attrs, Context context) {
        eai_item_size = (int) context.getResources().getDimension(R.dimen.eai_item_size);

        eai_ic_margin = (int) context.getResources().getDimension(R.dimen.eai_ic_margin);
        eai_ic_current_margin = (int) context.getResources().getDimension(R.dimen.eai_ic_current_margin);

        eai_ic_src = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_circle_empty, null);
        eai_ic_current_src = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_circular_filled, null);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.eaindicator, 0, 0);
            try {
                eai_item_size = (int) ta.getDimension(R.styleable.eaindicator_item_size, eai_item_size);

                eai_ic_margin = (int) ta.getDimension(R.styleable.eaindicator_ic_margin, eai_ic_margin);
                eai_ic_current_margin = (int) ta.getDimension(R.styleable.eaindicator_ic_current_margin, eai_ic_current_margin);

                if (ta.getDrawable(R.styleable.eaindicator_ic_src) != null)
                    eai_ic_src = ta.getDrawable(R.styleable.eaindicator_ic_src);
                if (ta.getDrawable(R.styleable.eaindicator_ic_current_src) != null)
                    eai_ic_current_src = ta.getDrawable(R.styleable.eaindicator_ic_current_src);

            } finally {
                ta.recycle();
            }
        }
    }
}
