package com.ahmetkilic.ophelia.ea_utilities.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.ahmetkilic.ophelia.ea_utilities.interfaces.WebViewTouchListener;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class TouchListenerWebView extends WebView {

    private WebViewTouchListener touchListener;

    public void setTouchListener(WebViewTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    public TouchListenerWebView(Context context) {
        super(context);
    }

    public TouchListenerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchListenerWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchListener != null)
            touchListener.onTouch();

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                this.performClick();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
