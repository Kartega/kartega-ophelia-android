package com.ahmetkilic.ophelia.ea_utilities.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle.FULL;
import static com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle.LONG;
import static com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle.MEDIUM;
import static com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle.NONE;
import static com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle.SHORT;

/**
 * Created by Ahmet Kılıç on 20.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({SHORT, MEDIUM, LONG, FULL,NONE})
public @interface DateStyle {
    int SHORT = 3;
    int MEDIUM = 2;
    int LONG = 1;
    int FULL = 0;
    int NONE = -1;
}
