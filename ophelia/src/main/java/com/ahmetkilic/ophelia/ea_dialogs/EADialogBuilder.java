package com.ahmetkilic.ophelia.ea_dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahmetkilic.ophelia.R;

import static com.ahmetkilic.ophelia.ea_utilities.tools.ViewTools.drawableColorChange;

/**
 * Created by Ahmet Kılıç on 15.01.2019.
 * Copyright © 2019, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EADialogBuilder {

    private Context context;

    private Dialog dialog;
    private View dialogView;
    private int style;
    private LinearLayout dialogBody;
    private RelativeLayout topIconHolder;
    private ProgressBar progressView;
    private ImageView dialogIcon;

    private TextView tvTitle;
    private android.widget.TextView tvMessage;

    public EADialogBuilder(Context context) {
        this.context = context;
        createDialog();
    }

    public EADialogBuilder setStyle(@DialogStyle int style) {
        this.style = style;
        createDialog();
        return this;
    }

    public EADialogBuilder setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public EADialogBuilder setMessage(String message) {
        if (tvMessage != null) {
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public EADialogBuilder hideTitle() {
        if (tvTitle != null) {
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    public EADialogBuilder hideMessage() {
        if (tvMessage != null) {
            tvMessage.setVisibility(View.GONE);
        }
        return this;
    }

    public EADialogBuilder setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    public EADialogBuilder setColoredCircle(int color) {
        if (topIconHolder != null) {
            topIconHolder.getBackground().setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
        }
        return this;
    }

    public EADialogBuilder setDialogIconAndColor(int icon, int iconColor) {
        if (dialogIcon != null) {
            dialogIcon.setImageDrawable(drawableColorChange(context, icon, iconColor));
        }
        return this;
    }

    public EADialogBuilder addButton(@ButtonStyle int buttonStyle) {
        return addButton(buttonStyle, (View.OnClickListener) null);
    }

    public EADialogBuilder addButton(@ButtonStyle int buttonStyle, String text) {
        return addButton(buttonStyle, text, null);
    }

    public EADialogBuilder addButton(@ButtonStyle int buttonStyle, View.OnClickListener clickListener) {
        String text;
        switch (buttonStyle) {
            case ButtonStyle.POSITIVE:
                text = context.getString(R.string.yes);
                break;
            case ButtonStyle.NEGATIVE:
                text = context.getString(R.string.no);
                break;
            case ButtonStyle.NEUTRAL:
                text = context.getString(R.string.ok);
                break;
            default:
                text = context.getString(R.string.ok);
                break;
        }
        return addButton(buttonStyle, text, clickListener);
    }

    public EADialogBuilder addButton(@ButtonStyle int buttonStyle, String text, final View.OnClickListener clickListener) {
        switch (buttonStyle) {
            case ButtonStyle.POSITIVE:
                return addButton(text, R.color.ea_dialog_button_positive_background_color, R.color.ea_dialog_button_positive_text_color, clickListener);
            case ButtonStyle.NEGATIVE:
                return addButton(text, R.color.ea_dialog_button_negative_background_color, R.color.ea_dialog_button_negative_text_color, clickListener);
            case ButtonStyle.NEUTRAL:
                return addButton(text, R.color.ea_dialog_button_neutral_background_color, R.color.ea_dialog_button_neutral_text_color, clickListener);
            default:
                return this;
        }
    }

    public EADialogBuilder addButton(String text, int bgColor, int textColor, final View.OnClickListener clickListener) {
        Button button = (Button) LayoutInflater.from(context).inflate(R.layout.ea_dialog_button, dialogBody, false);
        button.getBackground().setColorFilter(ContextCompat.getColor(context, bgColor), PorterDuff.Mode.SRC_IN);
        button.setTextColor(ContextCompat.getColor(context, textColor));
        button.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                if (clickListener != null)
                    clickListener.onClick(v);
            }
        });
        dialogBody.addView(button);
        return this;
    }

    public EADialogBuilder addButton(Button button) {
        dialogBody.addView(button);
        return this;
    }

    public void createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(dialogBuilder.getContext()).inflate(R.layout.ea_dialog_builder, null, false);
        dialog = dialogBuilder.setView(dialogView).create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        dialogBody = dialogView.findViewById(R.id.dialog_body);
        tvTitle = dialogView.findViewById(R.id.dialog_title);
        tvMessage = dialogView.findViewById(R.id.dialog_message);
        topIconHolder = dialogView.findViewById(R.id.colored_circle);

        dialogIcon = (ImageView) LayoutInflater.from(context).inflate(R.layout.ea_dialog_image, dialogBody, false);

        if (style == DialogStyle.PROGRESS) {
            topIconHolder.removeAllViews();
            progressView = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.ea_dialog_progress, topIconHolder, false);
            topIconHolder.addView(progressView);
            dialogIcon = null;
        } else {
            switch (style) {
                case DialogStyle.SUCCESS:
                    topIconHolder.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.ea_dialog_background_color_success), PorterDuff.Mode.SRC_IN);
                    dialogIcon.setImageDrawable(drawableColorChange(context, R.drawable.ic_dialog_success, R.color.white));
                    break;
                case DialogStyle.ERROR:
                    topIconHolder.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.ea_dialog_background_color_error), PorterDuff.Mode.SRC_IN);
                    dialogIcon.setImageDrawable(drawableColorChange(context, R.drawable.ic_dialog_error, R.color.white));
                    break;
                case DialogStyle.INFO:
                    topIconHolder.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.ea_dialog_background_color_info), PorterDuff.Mode.SRC_IN);
                    dialogIcon.setImageDrawable(drawableColorChange(context, R.drawable.ic_dialog_info, R.color.white));
                    break;
                case DialogStyle.NOTICE:
                    topIconHolder.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.ea_dialog_background_color_notice), PorterDuff.Mode.SRC_IN);
                    dialogIcon.setImageDrawable(drawableColorChange(context, R.drawable.ic_dialog_notice, R.color.white));
                    break;
                case DialogStyle.WARNING:
                    topIconHolder.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.ea_dialog_background_color_warning), PorterDuff.Mode.SRC_IN);
                    dialogIcon.setImageDrawable(drawableColorChange(context, R.drawable.ic_dialog_warning, R.color.white));
                    break;
                default:
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dialogBody.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    dialogBody.setLayoutParams(params);
                    dialogView.findViewById(R.id.dialog_top_circle).setVisibility(View.GONE);
                    LinearLayout.LayoutParams tvParams = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
                    tvParams.setMargins(0, 0, 0, 0);
                    tvTitle.setLayoutParams(tvParams);
                    return;
            }
            topIconHolder.removeAllViews();
            if (dialogIcon != null)
                topIconHolder.addView(dialogIcon);
        }
    }

    public Dialog show() {
        try {
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    dialog.show();
                }
            } else {
                dialog.show();
            }

            if (dialogIcon != null) {
                Animation alertIcon = AnimationUtils.loadAnimation(context, R.anim.rubber_band);
                dialogIcon.startAnimation(alertIcon);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EADialogBuilder", "Error on show!");
        }

        return dialog;
    }

    public Dialog hide() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EADialogBuilder", "Error on hide!");
        }
        dialog.dismiss();
        return dialog;
    }

    public LinearLayout getDialogBody() {
        return dialogBody;
    }
}
