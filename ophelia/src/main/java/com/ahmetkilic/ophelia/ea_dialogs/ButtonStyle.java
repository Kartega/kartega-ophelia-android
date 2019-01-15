package com.ahmetkilic.ophelia.ea_dialogs;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ahmetkilic.ophelia.ea_dialogs.ButtonStyle.NEGATIVE;
import static com.ahmetkilic.ophelia.ea_dialogs.ButtonStyle.NEUTRAL;
import static com.ahmetkilic.ophelia.ea_dialogs.ButtonStyle.POSITIVE;

/**
 * Created by Ahmet Kılıç on 15.01.2019.
 * Copyright © 2019, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({POSITIVE, NEGATIVE, NEUTRAL})
public @interface ButtonStyle {
    int POSITIVE = 1;
    int NEGATIVE = 2;
    int NEUTRAL = 3;
}
