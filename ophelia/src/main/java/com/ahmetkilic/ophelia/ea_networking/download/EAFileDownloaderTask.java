package com.ahmetkilic.ophelia.ea_networking.download;

import android.content.Context;
import android.os.AsyncTask;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_networking.ErrorParser;
import com.ahmetkilic.ophelia.ea_utilities.tools.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Ahmet Kılıç on 26.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class EAFileDownloaderTask extends AsyncTask<Void, Float, Void> {

    private EADownloadListener downloadListener;
    private String url;
    private String fileNameWithExtension;
    private File saveDestination;
    private WeakReference<Context> contextWeakReference;
    private HashMap<String, String> headers;

    EAFileDownloaderTask(EADownloadListener downloadListener, String url, String fileNameWithExtension, File saveDestination,
                         Context context, HashMap<String, String> headers) {
        this.downloadListener = downloadListener;
        this.url = url;
        this.fileNameWithExtension = fileNameWithExtension;
        this.saveDestination = saveDestination;
        this.contextWeakReference = new WeakReference<>(context);
        this.headers = headers;
    }

    @Override
    protected Void doInBackground(Void... params) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null)
            for (Map.Entry<String, String> entry : headers.entrySet())
                builder.addHeader(entry.getKey(), entry.getValue());

        Call currentCall = client.newCall(builder.get().build());
        try {
            Response response = currentCall.execute();
            if (response.code() == 200 || response.code() == 201) {
                ResponseBody body = response.body();

                if (body == null) {
                    String error = getUnexpectedError();
                    if (isContextValid())
                        error = contextWeakReference.get().getString(R.string.error_empty_response);
                    if (downloadListener != null)
                        downloadListener.onDownloadError(error);
                    return null;
                }

                InputStream inputStream = null;
                try {
                    inputStream = body.byteStream();

                    byte[] buff = new byte[1024 * 4];
                    long downloaded = 0;
                    long target = body.contentLength();
                    File file = new File(saveDestination, fileNameWithExtension);
                    OutputStream output = new FileOutputStream(file);

                    publishProgress((float) 0);
                    while (true) {
                        int read = inputStream.read(buff);

                        if (read == -1) {
                            break;
                        }
                        output.write(buff, 0, read);
                        downloaded += read;
                        float progress = ((float) downloaded / (float) target) * 100;
                        publishProgress(progress);
                        if (isCancelled()) {
                            currentCall.cancel();
                            String msg = "Download Cancelled.";
                            if (isContextValid())
                                msg = contextWeakReference.get().getString(R.string.error_cancelled);
                            if (downloadListener != null)
                                downloadListener.onDownloadError(msg);
                            FileUtils.deleteFileOrFolder(file);
                            return null;
                        }
                    }

                    output.flush();
                    output.close();
                    if (downloadListener != null) {
                        downloadListener.onDownloadComplete(file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (downloadListener != null) {
                        if (isContextValid())
                            downloadListener.onDownloadError(ErrorParser.getNetworkExceptionString(e, contextWeakReference.get()));
                        else
                            downloadListener.onDownloadError(getUnexpectedError());
                    }
                } finally {
                    if (inputStream != null)
                        inputStream.close();
                }
            } else {
                if (downloadListener != null) {
                    if (isContextValid())
                        downloadListener.onDownloadError(contextWeakReference.get().getString(R.string.error_server));
                    else
                        downloadListener.onDownloadError(getUnexpectedError());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (downloadListener != null) {
                if (isContextValid())
                    downloadListener.onDownloadError(contextWeakReference.get().getString(R.string.error_server));
                else
                    downloadListener.onDownloadError(getUnexpectedError());
            }
        }
        return null;
    }

    private String getUnexpectedError() {
        return "Unexpected error";
    }

    private boolean isContextValid() {
        return contextWeakReference != null && contextWeakReference.get() != null;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        if (downloadListener != null)
            downloadListener.onProgress(values[0]);

    }
}
