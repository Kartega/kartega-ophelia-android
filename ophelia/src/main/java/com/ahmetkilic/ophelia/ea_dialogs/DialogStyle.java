package com.ahmetkilic.ophelia.ea_dialogs;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ahmetkilic.ophelia.ea_dialogs.DialogStyle.ERROR;
import static com.ahmetkilic.ophelia.ea_dialogs.DialogStyle.INFO;
import static com.ahmetkilic.ophelia.ea_dialogs.DialogStyle.NOTICE;
import static com.ahmetkilic.ophelia.ea_dialogs.DialogStyle.PROGRESS;
import static com.ahmetkilic.ophelia.ea_dialogs.DialogStyle.SUCCESS;
import static com.ahmetkilic.ophelia.ea_dialogs.DialogStyle.WARNING;

/**
 * Created by Ahmet Kılıç on 15.01.2019.
 * Copyright © 2019, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({SUCCESS, ERROR, WARNING, INFO, NOTICE, PROGRESS})
public @interface DialogStyle {
    int SUCCESS = 1;
    int ERROR = 2;
    int WARNING = 3;
    int INFO = 4;
    int NOTICE = 5;
    int PROGRESS = 6;
}
