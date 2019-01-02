package com.ahmetkilic.ophelia.ea_networking.upload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_networking.ErrorParser;
import com.ahmetkilic.ophelia.ea_networking.upload.ProgressRequest.ProgressHelper;
import com.ahmetkilic.ophelia.ea_networking.upload.ProgressRequest.ProgressUIListener;
import com.ahmetkilic.ophelia.ea_utilities.tools.FileUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
@SuppressWarnings("unused")
public class EAUploadBuilder {

    private String url;
    private HashMap<String, String> parameters;
    private HashMap<String, String> headers;
    private EAUploadListener uploadListener;

    private List<File> files;
    private List<String> fileNames;
    private Call currentCall;

    private WeakReference<Context> contextWeakReference;

    public EAUploadBuilder(Context context) {
        files = new ArrayList<>();
        fileNames = new ArrayList<>();
        contextWeakReference = new WeakReference<>(context);
    }

    /**
     * Set url to upload
     *
     * @param url server url
     * @return builder
     */
    public EAUploadBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Set headers of the request
     *
     * @param headers headers
     * @return builder
     */
    public EAUploadBuilder setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Add a single header to the request
     *
     * @param key   header key
     * @param value header value
     * @return builder
     */
    public EAUploadBuilder addHeader(String key, String value) {
        if (headers == null)
            headers = new HashMap<>();
        headers.put(key, value);
        return this;
    }

    /**
     * Set additional form parameters for the request
     *
     * @param parameters parameters
     * @return builder
     */
    public EAUploadBuilder setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Add a single parameter to the request
     *
     * @param key   parameter key
     * @param value parameter value
     * @return builder
     */
    public EAUploadBuilder addParameter(String key, String value) {
        if (this.parameters == null)
            this.parameters = new HashMap<>();
        this.parameters.put(key, value);
        return this;
    }

    /**
     * Set a listener for the request's progress and response
     *
     * @param uploadListener listener
     * @return builder
     */
    public EAUploadBuilder setUploadListener(EAUploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }

    /**
     * Add a single file to upload with it's own name
     *
     * @param file file to upload
     * @return builder
     */
    public EAUploadBuilder addFile(File file) {
        files.add(file);
        fileNames.add(FileUtils.getNameFromPath(file.getPath()));
        return this;
    }

    /**
     * Add a single file to upload with a name
     *
     * @param file     file to upload
     * @param fileName file's name
     * @return builder
     */
    public EAUploadBuilder addFile(File file, String fileName) {
        files.add(file);
        fileNames.add(fileName);
        return this;
    }

    /**
     * Add a list of files to upload with their own names
     *
     * @param files files to upload
     * @return builder
     */
    public EAUploadBuilder addFiles(List<File> files) {
        this.files.addAll(files);
        for (File file : files)
            fileNames.add(FileUtils.getNameFromPath(file.getPath()));
        return this;
    }

    /**
     * Add a list of files to upload with a list of file names
     *
     * @param files     files to upload
     * @param fileNames files names
     * @return builder
     */
    public EAUploadBuilder addFiles(List<File> files, List<String> fileNames) {
        this.files.addAll(files);
        this.fileNames.addAll(fileNames);
        return this;
    }

    /**
     * Start uploading
     */
    public void upload() {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (ObjectUtils.arrayIsNotEmpty(files))
            for (int i = 0; i < files.size(); i++) {
                bodyBuilder.addFormDataPart
                        (
                                fileNames.get(i),
                                files.get(i).getName(),
                                RequestBody.create(getMediaType(files.get(i)), files.get(i))
                        );

            }

        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        uploadToServer(bodyBuilder.build());
    }

    /**
     * Cancel the upload
     */
    public void cancelUpload() {
        if (currentCall != null && !currentCall.isCanceled())
            currentCall.cancel();
    }

    //---------------- Private Methods --------------------//

    private void uploadToServer(MultipartBody build) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        RequestBody requestBody = ProgressHelper.withProgress(build, new ProgressUIListener() {
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                if (uploadListener != null)
                    uploadListener.onProgressChanged(percent * 100);
            }
        });

        builder.post(requestBody);

        currentCall = getHttpClient().newCall(builder.build());
        currentCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                if (uploadListener != null && contextWeakReference != null && contextWeakReference.get() != null)
                    uploadListener.onUploadError(ErrorParser.getNetworkExceptionString(e, contextWeakReference.get()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                ResponseBody body = response.body();
                try {
                    if (body != null) {
                        if (isListenerNotNull())
                            uploadListener.onUploadResponse(body.string());
                    } else {
                        if (isContextAndListenerValid())
                            uploadListener.onUploadError(contextWeakReference.get().getString(R.string.error_empty_response));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (isContextAndListenerValid())
                        uploadListener.onUploadError(ErrorParser.getNetworkExceptionString(e, contextWeakReference.get()));
                }
            }
        });
    }

    private boolean isContextAndListenerValid() {
        return isListenerNotNull() && isContextValid();
    }

    private boolean isListenerNotNull() {
        return uploadListener != null;
    }

    private boolean isContextValid() {
        return contextWeakReference != null && contextWeakReference.get() != null;
    }

    private MediaType getMediaType(File file) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = FileUtils.getExtensionFromPath(file.getPath());
        String mime_type = map.getMimeTypeFromExtension(ext);
        if (mime_type != null)
            return MediaType.parse(mime_type);
        else
            return null;
    }

    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder builder = original.newBuilder();
                if (headers != null && headers.size() > 0)
                    for (Map.Entry<String, String> entry : headers.entrySet())
                        builder.addHeader(entry.getKey(), entry.getValue());

                Request request = builder.method(original.method(), original.body()).build();
                return chain.proceed(request);
            }
        });

        httpClientBuilder.connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);

        return httpClientBuilder.build();
    }
}
