package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_utilities.interfaces.DialogButtonClickListener;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ViewTools {

    private static final int DISABLE_CLICK_TIME = 500;

    public static int colorPositive;
    public static int colorNegative;
    public static int colorNeutral;

    /**
     * Hide keyboard from activity
     *
     * @param activity activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /**
     * Hide keyboard from view
     *
     * @param context context
     * @param view    view
     */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Disable click of a view temporarily(0.5 sec)
     *
     * @param view view to disable click
     */
    public static void disableClickTemporarily(final View view) {
        setViewClickable(view, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewClickable(view, true);
            }
        }, DISABLE_CLICK_TIME);
    }

    private static void setViewClickable(View view, boolean isClickable) {
        if (view != null) {
            view.setClickable(isClickable);
            view.setFocusable(isClickable);
            view.setEnabled(isClickable);
            view.setActivated(isClickable);
        }
    }

    private static ViewGroup getParent(View view) {
        return (ViewGroup) view.getParent();
    }

    private static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if (parent != null) {
            parent.removeView(view);
        }
    }

    /**
     * Replace two views
     *
     * @param currentView current view to remove
     * @param newView     new view to add
     */
    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        removeView(newView);
        parent.addView(newView, index);
    }

    public static Drawable drawableColorChange(Context context, int icon, int color) {
        Drawable drawable;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(icon);
        } else {
            drawable = context.getResources().getDrawable(icon);
        }

        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
        }

        return drawable;
    }

    /**
     * Check list of editTexts and put field required error if empty
     *
     * @param context      context to get error string
     * @param editTextList list of editTexts
     */
    public static boolean checkFields(Context context, List<EditText> editTextList) {
        View focus = null;

        for (EditText editText : editTextList) {
            editText.setError(null);
            if (editText.getText().toString().length() < 1) {
                editText.setError(context.getString(R.string.field_can_not_be_empty));
                focus = editText;
            }
        }

        if (focus != null) {
            focus.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Show alert dialog with custom button variations. If the text string of a button is null or empty, button won't be shown.
     *
     * @param context               context to create alert dialog.
     * @param message               message to show
     * @param btnTextPositive       positive button text
     * @param btnTextNegative       negative button text
     * @param btnTextNeutral        neutral button text
     * @param positiveClickListener click listener for positive button
     * @param negativeClickListener click listener for negative button
     * @param neutralClickListener  click listener for neutral button
     */
    public static void showAlertDialog(Context context,
                                       @Nonnull String message,
                                       @Nullable String btnTextPositive,
                                       @Nullable String btnTextNegative,
                                       @Nullable String btnTextNeutral,
                                       @Nullable final DialogButtonClickListener positiveClickListener,
                                       @Nullable final DialogButtonClickListener negativeClickListener,
                                       @Nullable final DialogButtonClickListener neutralClickListener) {

        createAlertDialog(context, message, btnTextPositive, btnTextNegative, btnTextNeutral, positiveClickListener, negativeClickListener, neutralClickListener).show();
    }

    /**
     * Create alert dialog with custom button variations. If the text string of a button is null or empty, button won't be shown.
     *
     * @param context               context to create alert dialog.
     * @param message               message to show
     * @param btnTextPositive       positive button text
     * @param btnTextNegative       negative button text
     * @param btnTextNeutral        neutral button text
     * @param positiveClickListener click listener for positive button
     * @param negativeClickListener click listener for negative button
     * @param neutralClickListener  click listener for neutral button
     */
    public static AlertDialog createAlertDialog(Context context,
                                                @Nonnull String message,
                                                @Nullable String btnTextPositive,
                                                @Nullable String btnTextNegative,
                                                @Nullable String btnTextNeutral,
                                                @Nullable final DialogButtonClickListener positiveClickListener,
                                                @Nullable final DialogButtonClickListener negativeClickListener,
                                                @Nullable final DialogButtonClickListener neutralClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);
        if (StringUtils.isEmptyString(btnTextNeutral))
            builder.setNeutralButton(btnTextNeutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    if (neutralClickListener != null)
                        neutralClickListener.onDialogButtonClicked();
                }
            });

        if (StringUtils.isEmptyString(btnTextNegative))
            builder.setNegativeButton(btnTextNegative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    dialoginterface.cancel();
                    if (negativeClickListener != null)
                        negativeClickListener.onDialogButtonClicked();
                }
            });
        if (StringUtils.isEmptyString(btnTextPositive))
            builder.setPositiveButton(btnTextPositive, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    dialoginterface.cancel();
                    if (positiveClickListener != null)
                        positiveClickListener.onDialogButtonClicked();
                }
            }).create();

        if (colorPositive == 0)
            colorPositive = ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null);
        if (colorNegative == 0)
            colorNegative = ResourcesCompat.getColor(context.getResources(), R.color.negative_button_default_text_color, null);
        if (colorNeutral == 0)
            colorNeutral = ResourcesCompat.getColor(context.getResources(), R.color.neutral_button_default_text_color, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(colorPositive);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(colorNegative);
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(colorNeutral);
            }
        });

        return dialog;
    }
}
