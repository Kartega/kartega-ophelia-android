package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.content.Context;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class Calculator {

    /**
     * Generate dp value from pixel value
     *
     * @param context context
     * @param px pixel value
     */
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * Generate pixel value from dp value
     *
     * @param context context
     * @param dp dp value
     */
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
