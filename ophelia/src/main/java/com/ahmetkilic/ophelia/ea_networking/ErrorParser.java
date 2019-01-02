package com.ahmetkilic.ophelia.ea_networking;

import android.content.Context;

import com.ahmetkilic.ophelia.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;

/**
 * Created by Ahmet Kılıç on 26.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ErrorParser {
    public static String getNetworkExceptionString(Exception error, Context context) {
        String errorString = context.getString(R.string.error_unknown);

        if (error instanceof TimeoutError) {
            errorString = context.getString(R.string.time_out_error);
        } else if (error instanceof NoConnectionError) {
            errorString = context.getString(R.string.error_no_connection);
        } else if (error instanceof AuthFailureError) {
            errorString = context.getString(R.string.auth_failure_error);
        } else if (error instanceof ServerError) {
            errorString = context.getString(R.string.error_server);
        } else if (error instanceof NetworkError) {
            errorString = context.getString(R.string.error_network);
        } else if (error instanceof ParseError) {
            errorString = context.getString(R.string.error_parse);
        } else if (error instanceof FileNotFoundException) {
            errorString = context.getString(R.string.filer_not_found_exception);
        } else if (error instanceof SocketException) {
            errorString = context.getString(R.string.error_network);
        } else if (error instanceof MalformedURLException) {
            errorString = context.getString(R.string.error_malformed_url);
        } else if (error instanceof JSONException) {
            errorString = context.getString(R.string.error_parse_json);
        } else if (error instanceof XmlPullParserException) {
            errorString = context.getString(R.string.error_parse_xml);
        } else if ((error != null ? error.getCause() : null) instanceof OutOfMemoryError) {
            errorString = context.getString(R.string.error_out_of_memory);
        } else if ((error != null ? error.getCause() : null) instanceof ConnectException) {
            errorString = context.getString(R.string.error_no_connection);
        } else if ((error != null ? error.getCause() : null) instanceof MalformedURLException) {
            errorString = context.getString(R.string.error_malformed_url);
        } else if ((error != null ? error.getCause() : null) instanceof ServerError) {
            errorString = context.getString(R.string.error_server);
        } else if ((error != null ? error.getCause() : null) instanceof JSONException) {
            errorString = context.getString(R.string.error_parse_json);
        } else if ((error != null ? error.getCause() : null) instanceof XmlPullParserException) {
            errorString = context.getString(R.string.error_parse_xml);
        } else if ((error != null ? error.getCause() : null) instanceof SocketException) {
            errorString = context.getString(R.string.error_network);
        }

        return errorString;
    }

}
