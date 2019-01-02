package com.ahmetkilic.ophelia.ea_networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class VolleySingleton {

    /**
     * Singleton for Volley Request Queue
     */

    private RequestQueue requestQueue;

    private static VolleySingleton mInstance;

    static VolleySingleton getInstance() throws IOException {
        if (mInstance == null) throw new IOException("Initialize the singleton first!");
        return mInstance;
    }

    /**
     * Init this instance in your application class with application context
     *
     * @param context application context
     */
    public static void initInstance(Context context) {
        if (mInstance == null)
            mInstance = new VolleySingleton(context);
    }

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Add a request to request queue with custom tag.
     *
     * @param request request to add
     * @param tag     custom tag
     */
    public void addToRequestQueue(Request request, Object tag) {
        if (tag != null)
            request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * Add a request to request queue with default class tag.
     *
     * @param request request to add
     */
    public void addToRequestQueue(Request request) {
        requestQueue.add(request);
    }

    /**
     * Cancel all requests with a tag
     *
     * @param tag tag of request
     */
    public void cancelRequest(Object tag) {
        requestQueue.cancelAll(tag);
    }
}
