package com.ahmetkilic.ophelia.ea_networking;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
@SuppressWarnings("unused")
public class EARequestBuilder {

    private EAVolleyHelper helper;
    private Gson gson;
    private String url;
    private int method;
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private boolean shouldCache;
    private boolean isShouldCacheSet;
    private boolean clearHeaders;
    private DefaultRetryPolicy defaultRetryPolicy;
    private EAErrorListener errorCallback;
    private EAObjectResponseListener objectResponseListener;
    private EAListResponseListener listResponseListener;
    private Class responseClass;
    private Class errorClass;

    public EARequestBuilder() {
        gson = new Gson();
        this.responseClass = String.class;
        this.errorClass = String.class;
    }

    public EARequestBuilder(GsonBuilder builder) {
        gson = builder.create();
        this.responseClass = String.class;
    }

    /**
     * Set the helper class
     *
     * @param helper Volley helper instance
     * @return builder
     */
    public EARequestBuilder setHelper(EAVolleyHelper helper) {
        this.helper = helper;
        return this;
    }

    /**
     * Set request url
     *
     * @param url request url
     * @return builder
     */
    public EARequestBuilder setUrl(String url) {
        this.url = url;
        this.clearHeaders = false;
        return this;
    }

    /**
     * Set request method using Request.Method.
     *
     * @param method method
     * @return builder
     */
    public EARequestBuilder setMethod(int method) {
        this.method = method;
        return this;
    }

    /**
     * Set parameters
     *
     * @param params parameters hash map
     * @return builder
     */
    public EARequestBuilder setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * Clear all parameters
     *
     * @return builder
     */
    public EARequestBuilder clearParams() {
        if (this.params == null)
            this.params = new HashMap<>();
        this.params.clear();
        return this;
    }

    /**
     * clear all headers
     *
     * @return builder
     */
    public EARequestBuilder clearHeaders() {
        this.clearHeaders = true;
        return this;
    }

    /**
     * Add a single parameter
     *
     * @param key parameter key
     * @param value parameter value
     * @return builder
     */
    public EARequestBuilder addParams(String key, String value) {
        if (this.params == null)
            this.params = new HashMap<>();
        this.params.put(key, value);
        return this;
    }

    /**
     * Set the list response listener if the server will return a JsonArray
     *
     * @param listResponseListener response listener
     * @return builder
     */
    public EARequestBuilder setListResponseListener(EAListResponseListener listResponseListener) {
        this.listResponseListener = listResponseListener;
        return this;
    }

    /**
     * Set the object response listener if the server will return a JsonObject
     *
     * @param objectResponseListener response listener
     * @return builder
     */
    public EARequestBuilder setObjectResponseListener(EAObjectResponseListener objectResponseListener) {
        this.objectResponseListener = objectResponseListener;
        return this;
    }

    /**
     * Set the response class for server response
     *
     * @param responseClass class of response
     * @return builder
     */
    public EARequestBuilder setResponseClass(Class responseClass) {
        this.responseClass = responseClass;
        return this;
    }

    /**
     * Set a custom error class for server error responses
     *
     * @param errorClass class of error
     * @return builder
     */
    public EARequestBuilder setErrorClass(Class errorClass) {
        this.errorClass = errorClass;
        return this;
    }

    /**
     * Set the error listener for request
     *
     * @param errorListener error listener
     * @return builder
     */
    public EARequestBuilder setErrorListener(EAErrorListener errorListener) {
        this.errorCallback = errorListener;
        return this;
    }

    /**
     * Add a single header to request
     *
     * @param key header key
     * @param value header value
     * @return builder
     */
    public EARequestBuilder addHeader(String key, String value) {
        if (this.headers == null)
            this.headers = new HashMap<>();

        this.headers.put(key, value);
        return this;
    }

    /**
     * Change the default retry policy of helper class
     *
     * @param defaultRetryPolicy retry policy
     * @return builder
     */
    public EARequestBuilder setDefaultRetryPolicy(DefaultRetryPolicy defaultRetryPolicy) {
        this.defaultRetryPolicy = defaultRetryPolicy;
        return this;
    }

    /**
     * change should cache parameter of helper class
     * @param shouldCache enable cache if true
     * @return builder
     */
    public EARequestBuilder setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
        this.isShouldCacheSet = true;
        return this;
    }

    /**
     * Change only time out of helper class with a new retry policy
     *
     * @param timeOutMillis time out in millis
     * @return builder
     */
    public EARequestBuilder setTimeOutMillis(int timeOutMillis) {
        defaultRetryPolicy = new DefaultRetryPolicy(timeOutMillis, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return this;
    }

    /**
     * Get current parameters of builder
     *
     * @return parameters
     */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * Get current headers of builder
     *
     * @return headers.
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Execute the builder
     *
     */
    public void fetch() {
        if (headers != null)
            helper.getHeaders().putAll(headers);
        if (clearHeaders)
            helper.getHeaders().clear();

        if (defaultRetryPolicy != null)
            helper.setDefaultRetryPolicy(defaultRetryPolicy);

        if (isShouldCacheSet)
            helper.setShouldCache(shouldCache);

        if (responseClass != null && url != null && helper != null)
            helper.requestEngine(url, params, method, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String url2 = url;
                    if (response != null && response.length() > 0) {
                        if (helper.isResponseLogEnabled())
                            helper.doLog("RESPONSE:" + response);
                        if (errorClass != null && response.startsWith("{"))
                            checkError(response);
                        else
                            handleSuccess(response);
                    } else
                        handleError("Success but empty response!");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (!helper.isStopListeningResponse()) {
                        if (helper.isResponseLogEnabled()) {
                            helper.doLog(error.toString());
                            helper.doLog(helper.getVolleyErrorString(error));
                        }
                        handleError(helper.getVolleyErrorString(error));
                    } else if (helper.isResponseLogEnabled())
                        helper.doLog("Response is not listening !");
                }
            });
        else if (helper != null && helper.getVolleyRequestTag() != null)
            Log.e(helper.getVolleyRequestTag(), "Response Class,Url and Helper Can Not Be NULL!");
    }

    /**
     * Check response if it is an error with given error class type
     *
     * @param response response string
     */
    private void checkError(String response) {
        if (errorClass == String.class){
            handleSuccess(response);
            return;
        }

        Object error = gson.fromJson(response, errorClass);
        if (error != null) {
            Field[] fields = errorClass.getDeclaredFields();
            boolean isEmpty = true;
            for (Field field : fields) {
                field.setAccessible(true);
                String type = field.getType().getSimpleName();
                String fieldName = field.getName();
                if (!fieldName.equals("serialVersionUID") && !fieldName.equals("$change"))
                    try {
                        Object result = field.get(error);
                        if (type.contains("int")) {
                            if ((int) result != 0)
                                isEmpty = false;
                        } else if (type.contains("bool")) {
                            if ((boolean) result)
                                isEmpty = false;
                        } else if (result != null)
                            isEmpty = false;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
            if (isEmpty)
                handleSuccess(response);
            else
                handleError(error);
        } else {
            handleSuccess(response);
        }
    }

    private void handleError(Object error) {
        if (errorCallback != null) {
            errorCallback.onError(error);
        }
    }

    private void handleSuccess(String response) {
        try {
            if (listResponseListener != null && response.startsWith("[")) {
                deliverListResult((ArrayList) gson.fromJson(response, TypeToken.getParameterized(ArrayList.class, responseClass).getType()));
            }
            if (objectResponseListener != null) {
                if (responseClass == String.class) {
                    deliverResult(response);
                } else if (response.startsWith("{")) {
                    deliverResult(gson.fromJson(response, responseClass));
                }
            }
        } catch (JsonSyntaxException exception) {
            exception.printStackTrace();
            Log.e("JsonParseError", "From Response:" + response);
            handleError("JsonParse Exception");
        }
    }

    @SuppressWarnings("all")
    private void deliverResult(Object object) {
        objectResponseListener.onResponse(object);
    }

    @SuppressWarnings("all")
    private void deliverListResult(ArrayList list) {
        listResponseListener.onResponse(list);
    }

}

