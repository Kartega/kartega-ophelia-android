package com.ahmetkilic.ophelia.ea_utilities.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmetkilic.ophelia.R;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class CustomToast {

    private Toast toast;
    private Context context;
    private View view;

    @SuppressLint("InflateParams")
    public CustomToast(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.toast_custom_layout, null, false);
    }

    public void showToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setView(view);
        TextView textView = toast.getView().findViewById(R.id.tv_toast);
        if (textView != null) {
            textView.setText(message);
            toast.show();
        }
    }
}
