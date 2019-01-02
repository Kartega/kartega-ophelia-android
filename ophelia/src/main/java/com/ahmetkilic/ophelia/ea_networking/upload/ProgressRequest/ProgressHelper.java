package com.ahmetkilic.ophelia.ea_networking.upload.ProgressRequest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ProgressHelper {

    public static RequestBody withProgress(RequestBody requestBody, ProgressListener progressListener) {
        if (requestBody == null) {
            throw new IllegalArgumentException("requestBody == null");
        }
        if (progressListener == null) {
            throw new IllegalArgumentException("progressListener == null");
        }
        return new ProgressRequestBody(requestBody, progressListener);
    }

    public static ResponseBody withProgress(ResponseBody responseBody, ProgressListener progressListener) {
        if (responseBody == null) {
            throw new IllegalArgumentException("responseBody == null");
        }
        if (progressListener == null) {
            throw new IllegalArgumentException("progressListener == null");
        }
        return new ProgressResponseBody(responseBody, progressListener);
    }
}
