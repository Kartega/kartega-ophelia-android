package com.ahmetkilic.ophelia.ea_utilities.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.ahmetkilic.ophelia.R;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class DrawableTintTextView extends AppCompatTextView {

    public DrawableTintTextView(Context context) {
        super(context);
    }

    public DrawableTintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadTint(context, attrs, 0);
    }

    public DrawableTintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadTint(context, attrs, defStyleAttr);
    }

    public void loadTint(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTintTextView, defStyleAttr, 0);

        if (typedArray.hasValue(R.styleable.DrawableTintTextView_drawableTint)) {
            int color = typedArray.getColor(R.styleable.DrawableTintTextView_drawableTint, 0);

            Drawable[] drawables = getCompoundDrawables();

            for (Drawable drawable : drawables) {
                if (drawable == null) continue;
                DrawableCompat.setTint(DrawableCompat.wrap(drawable).mutate(), color);
            }
        }

        typedArray.recycle();
    }
}
