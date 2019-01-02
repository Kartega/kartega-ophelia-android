package com.ahmetkilic.ophelia.ea_networking;

import android.content.Context;
import android.util.Log;

import com.ahmetkilic.ophelia.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
@SuppressWarnings("All")
public class EAVolleyHelper {

    /**
     * Base helper class for volley.
     * Settings and common headers are stored in this class.
     * You can use use one instance for all builders or you use separate instances for separate settings.
     */

    private Context context;
    private DefaultRetryPolicy defaultRetryPolicy;
    private HashMap<String, String> headers;
    private boolean stop_listening_response;
    private final String VOLLEY_REQUEST_TAG;
    private boolean SENT_PARAMS_LOG_ENABLED;
    private boolean URL_LOG_ENABLED;
    private boolean RESPONSE_LOG_ENABLED;
    private boolean HEADERS_LOG_ENABLED;
    private boolean VOLLEY_SHOULD_CACHE;

    private int timeOutMillis, retryCount;

    public EAVolleyHelper(Context context) {
        this.context = context;
        VOLLEY_REQUEST_TAG = context.getClass().getSimpleName();
        stop_listening_response = false;
        setShouldCache(false);
        setHeadersLogEnabled(true);
        setResponseLogEnabled(true);
        setSentParamsLogEnabled(true);
        setUrlLogEnabled(true);

        retryCount = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
        timeOutMillis = 21000;

        updateRetryPolicy();

        headers = new HashMap<>();
    }

    /**
     * Create a string request and add it to queue.
     *
     * @param URL           request url
     * @param params        request parameters
     * @param method        request method
     * @param listener      response listener
     * @param errorListener error listener
     */
    void requestEngine(String URL,
                       final HashMap<String, String> params,
                       int method,
                       Response.Listener<String> listener,
                       Response.ErrorListener errorListener) {

        if (params != null && SENT_PARAMS_LOG_ENABLED)
            doLog("Sent Params: " + params.toString());

        if (URL_LOG_ENABLED)
            doLog("URL: " + URL);

        StringRequest request = new StringRequest(method, URL, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null) {
                    if (SENT_PARAMS_LOG_ENABLED)
                        doLog("PARAMS:" + params.toString());
                    return params;
                } else
                    return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if (headers != null) {
                    if (HEADERS_LOG_ENABLED)
                        doLog("HEADERS: " + headers.toString());
                    return headers;
                }

                return super.getHeaders();
            }
        };

        request.setTag(VOLLEY_REQUEST_TAG);
        request.setRetryPolicy(defaultRetryPolicy);
        request.setShouldCache(VOLLEY_SHOULD_CACHE);
        try {
            VolleySingleton.getInstance().addToRequestQueue(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a custom request to queue.
     *
     * @param request request to add
     */
    public void addCustomRequest(Request request) {
        request.setTag(VOLLEY_REQUEST_TAG);
        request.setRetryPolicy(defaultRetryPolicy);
        request.setShouldCache(VOLLEY_SHOULD_CACHE);
        try {
            VolleySingleton.getInstance().addToRequestQueue(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show log for request
     *
     * @param message message to log
     */
    void doLog(String message) {
        Log.v(VOLLEY_REQUEST_TAG, message);
    }

    private String getResourcesString(int resID) {
        return context.getString(resID);
    }

    String getVolleyErrorString(VolleyError error) {
        String errorString = getResourcesString(R.string.error_unknown);
        if (error instanceof TimeoutError) {
            errorString = getResourcesString(R.string.time_out_error);
        } else if (error instanceof NoConnectionError) {
            errorString = getResourcesString(R.string.error_no_connection);
        } else if (error instanceof AuthFailureError) {
            errorString = getResourcesString(R.string.auth_failure_error);
        } else if (error instanceof ServerError) {
            errorString = getResourcesString(R.string.error_server);
        } else if (error instanceof NetworkError) {
            errorString = getResourcesString(R.string.error_network);
        } else if (error instanceof ParseError) {
            errorString = getResourcesString(R.string.error_parse);
        }

        if ((error != null ? error.getCause() : null) instanceof OutOfMemoryError) {
            errorString = getResourcesString(R.string.error_out_of_memory);
        } else if ((error != null ? error.getCause() : null) instanceof ConnectException) {
            errorString = getResourcesString(R.string.error_no_connection);
        } else if ((error != null ? error.getCause() : null) instanceof MalformedURLException) {
            errorString = getResourcesString(R.string.error_malformed_url);
        } else if ((error != null ? error.getCause() : null) instanceof ServerError) {
            errorString = getResourcesString(R.string.error_server);
        } else if ((error != null ? error.getCause() : null) instanceof JSONException) {
            errorString = getResourcesString(R.string.error_parse_json);
        } else if ((error != null ? error.getCause() : null) instanceof XmlPullParserException) {
            errorString = getResourcesString(R.string.error_parse_xml);
        } else if ((error != null ? error.getCause() : null) instanceof SocketException) {
            errorString = getResourcesString(R.string.error_network);
        }

        return errorString;
    }

    boolean isStopListeningResponse() {
        return stop_listening_response;
    }

    /**
     * Set the default config for cache
     *
     * @param shouldCache enable cache
     */
    public void setShouldCache(boolean shouldCache) {
        this.VOLLEY_SHOULD_CACHE = shouldCache;
    }

    /**
     * Set the default config for logging of parameters
     *
     * @param sentParamsLogEnabled log is enabled if true
     */
    public void setSentParamsLogEnabled(boolean sentParamsLogEnabled) {
        SENT_PARAMS_LOG_ENABLED = sentParamsLogEnabled;
    }

    /**
     * Set the default config for logging of the URL
     *
     * @param urlLogEnabled log is enabled if true
     */
    public void setUrlLogEnabled(boolean urlLogEnabled) {
        URL_LOG_ENABLED = urlLogEnabled;
    }

    /**
     * Set the default config for logging of the response
     *
     * @param responseLogEnabled log is enabled if true
     */
    public void setResponseLogEnabled(boolean responseLogEnabled) {
        RESPONSE_LOG_ENABLED = responseLogEnabled;
    }

    /**
     * Set the default config for logging of headers
     *
     * @param headersLogEnabled log is enabled if true
     */
    public void setHeadersLogEnabled(boolean headersLogEnabled) {
        HEADERS_LOG_ENABLED = headersLogEnabled;
    }

    /**
     * Set the default config for time out millis
     *
     * @param timeOutMillis milliseconds for timeout of the request
     */
    public void setTimeOutMillis(int timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
        updateRetryPolicy();
    }


    /**
     * Check if the response log is enabled
     *
     * @return response log enabled value
     */
    public boolean isResponseLogEnabled() {
        return RESPONSE_LOG_ENABLED;
    }

    /**
     * Set maximum amount of retries for a request
     *
     * @param retryCount amount of retries
     */
    public void setMaxRetries(int retryCount) {
        this.retryCount = retryCount;
        updateRetryPolicy();
    }

    /**
     * Set the default retry policy
     *
     * @param defaultRetryPolicy retryPolicy
     */
    public void setDefaultRetryPolicy(DefaultRetryPolicy defaultRetryPolicy) {
        this.defaultRetryPolicy = defaultRetryPolicy;
    }

    private void updateRetryPolicy() {
        defaultRetryPolicy = new DefaultRetryPolicy(timeOutMillis, retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    /**
     * Call this on your activity's onDestroy
     */
    public void stopResponseListening() {
        stop_listening_response = true;
        try {
            VolleySingleton.getInstance().cancelRequest(VOLLEY_REQUEST_TAG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the tag of the helper class
     *
     * @return request tag
     */
    public String getVolleyRequestTag() {
        return VOLLEY_REQUEST_TAG;
    }


    /**
     * Get the headers of the helper class
     *
     * @return headers
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }
}
