package com.ahmetkilic.ophelia.ea_slider.transformers;

import android.view.View;

public class DrawerTransformer extends BaseTransformer {
    @Override
    protected void onTransform(View view, float position) {
        if (position <= 0) {
            view.setTranslationX(0);
        } else if (position <= 1) {
            view.setTranslationX(-view.getWidth() / 2 * position);
        }
    }
}
