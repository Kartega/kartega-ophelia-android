package com.ahmetkilic.ophelia.ea_progress;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.ahmetkilic.ophelia.R;

import java.lang.ref.WeakReference;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

public class EAProgressDialog extends Dialog {

    private RelativeLayout main;
    private WeakReference<Context> owner;

    public EAProgressDialog(Context context) {
        super(context, R.style.EADialogTheme);
        setContentView(R.layout.ea_progress_layout);
        setCancelable(false);
        main = findViewById(R.id.ea_progress_main);
        owner = new WeakReference<>(context);
    }

    @Override
    public void show() {
        showWithAnimation();
    }

    @Override
    public void dismiss() {
        if (isActivityValid())
            dismissWithAnimation();
    }

    private void dismissWithAnimation() {
        if (main != null) {
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
            main.startAnimation(fadeOutAnimation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isActivityValid())
                        EAProgressDialog.super.dismiss();
                }
            }, fadeOutAnimation.getDuration());
        } else
            super.dismiss();
    }

    private void showWithAnimation() {
        if (main != null)
            main.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        EAProgressDialog.super.show();
    }

    private boolean isActivityValid() {
        return owner.get() != null;
    }

    /**
     * Create a progress for dialog
     *
     * @param style    CrystalPreloader.STYLE value
     * @param size     CrystalPreloader.SIZE value
     * @param color_fg color id of foreground color
     * @param color_bg color id of background color
     * @param duration duration of progress item
     */
    public void setProgress(int style, int size, int color_fg, int color_bg, int duration) {
        CrystalPreloader crystalPreloader = new CrystalPreloader(getContext());
        crystalPreloader.setDuration(duration);
        crystalPreloader.setStyle(style);
        crystalPreloader.setSize(size);
        crystalPreloader.setFgColor(ResourcesCompat.getColor(getContext().getResources(), color_fg, null));
        crystalPreloader.setBgColor(ResourcesCompat.getColor(getContext().getResources(), color_bg, null));

        main.removeAllViews();
        main.addView(crystalPreloader);

        crystalPreloader.init();
    }
}
