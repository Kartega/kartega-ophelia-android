package com.ahmetkilic.ophelia.ea_networking;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ahmetkilic.ophelia.ea_networking.Method.DELETE;
import static com.ahmetkilic.ophelia.ea_networking.Method.DEPRECATED_GET_OR_POST;
import static com.ahmetkilic.ophelia.ea_networking.Method.GET;
import static com.ahmetkilic.ophelia.ea_networking.Method.HEAD;
import static com.ahmetkilic.ophelia.ea_networking.Method.OPTIONS;
import static com.ahmetkilic.ophelia.ea_networking.Method.PATCH;
import static com.ahmetkilic.ophelia.ea_networking.Method.POST;
import static com.ahmetkilic.ophelia.ea_networking.Method.PUT;
import static com.ahmetkilic.ophelia.ea_networking.Method.TRACE;

/**
 * Created by Ahmet Kılıç on 4.01.2019.
 * Copyright © 2019, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({DEPRECATED_GET_OR_POST, GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, PATCH})
public @interface Method {
    int DEPRECATED_GET_OR_POST = -1;
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int HEAD = 4;
    int OPTIONS = 5;
    int TRACE = 6;
    int PATCH = 7;
}
