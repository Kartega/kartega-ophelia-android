package com.ahmetkilic.eaframework;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ahmetkilic.ophelia.EABaseActivity;
import com.ahmetkilic.ophelia.ea_networking.EAErrorListener;
import com.ahmetkilic.ophelia.ea_networking.EAObjectResponseListener;
import com.ahmetkilic.ophelia.ea_networking.EARequestBuilder;
import com.ahmetkilic.ophelia.ea_networking.EAVolleyHelper;
import com.ahmetkilic.ophelia.ea_networking.download.EADownloadListener;
import com.ahmetkilic.ophelia.ea_networking.download.EAFileDownloadBuilder;
import com.ahmetkilic.ophelia.ea_networking.upload.EAUploadBuilder;
import com.ahmetkilic.ophelia.ea_networking.upload.EAUploadListener;
import com.ahmetkilic.ophelia.ea_utilities.enums.LogType;
import com.ahmetkilic.ophelia.ea_utilities.tools.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NetworkingActivity extends EABaseActivity {

    private TextView tvResponse;
    private EAVolleyHelper helper;
    private boolean isDownloadingManager;
    private boolean isDownloadingCustom;
    private EAFileDownloadBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tvResponse = findViewById(R.id.tv_response);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_download_manager).setOnClickListener(this);
        findViewById(R.id.btn_download_custom).setOnClickListener(this);
        findViewById(R.id.btn_delete_downloads).setOnClickListener(this);

        helper = new EAVolleyHelper(this);
        helper.setResponseLogEnabled(false);
        helper.setHeadersLogEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (isClickDisabled())
            return;
        disableClickTemporarily();
        switch (v.getId()) {
            case R.id.btn_request:
                requestTest();
                break;
            case R.id.btn_upload:
                uploadTest();
                break;
            case R.id.btn_download_custom:
                if (isDownloadingCustom)
                    builder.cancel();
                else
                    downloadTest(false);
                break;
            case R.id.btn_download_manager:
                if (isDownloadingManager)
                    builder.cancel();
                else
                    downloadTest(true);
                break;
            case R.id.btn_delete_downloads:
                FileUtils.deleteFileOrFolder(new File(FileUtils.getDownloadsPath() + File.separator + "test_file.jpg"));
                break;
            default:
                enableClick();
                super.onClick(v);
                break;
        }
    }

    private void requestTest() {
        //String url = "https://enqolrgw1pyfj.x.pipedream.net/sample/test";
        String url = "https://jsonplaceholder.typicode.com/todos/1";
/*
        Car car = new Car("Audi");
        Human human = new Human("Ahmet", car);
        Gson gson = new Gson();

        JSONArray array = new JSONArray();
        try {
            JSONObject obj1 = new JSONObject(gson.toJson(human));
            JSONObject obj2 = new JSONObject(gson.toJson(human));
            array.put(obj1);
            array.put(obj2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // String jsonBody = gson.toJson(human);
        String jsonBody = array.toString();
*/
        showProgress();
        EARequestBuilder requestBuilder = new EARequestBuilder();
        requestBuilder
                .setUrl(url)
                .setHelper(helper)
                .setShouldCache(false)
                //.addParams("oKey","HELLO")
                //.setMethod(Method.POST)
                //.setJsonParams(jsonBody)
                //.setResponseClass(String.class)
                //.setErrorClass(String.class)
                //.setMethod(Request.Method.GET)
                //.setDefaultRetryPolicy(...)
                //.setParams(...)
                //.setShouldCache(false)
                //.setTimeOutMillis(15000)
                //.setListResponseListener(...) // for List of items
                .setObjectResponseListener(new EAObjectResponseListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgress();
                        makeLog(LogType.INFO, response);
                        tvResponse.setText(response);
                    }
                })
                .setErrorListener(new EAErrorListener() {
                    @Override
                    public void onError(Object error) {
                        hideProgress();
                        makeLog(LogType.ERROR, error.toString());
                        tvResponse.setText(error.toString());
                    }
                })
                .fetch();
    }

    private void uploadTest() {
        String url = "http://thyopet.peticms.com/mobile/FileUploadTest" + "?param2=p2FromUrl&param3=p3FromUrl";

        List<String> fileNames = new ArrayList<>();

        fileNames.add("file-item-1");
        fileNames.add("file-item-2");
        fileNames.add("file-item-3");

        List<File> files = new ArrayList<>();
        files.add(new File("/storage/emulated/0/DCIM/1544425059682.jpg"));
        files.add(new File("/storage/emulated/0/DCIM/1544427246052.jpg"));
        files.add(new File("/storage/emulated/0/DCIM/1544425059682.jpg"));

        HashMap<String, String> headers = new HashMap<>();
        headers.put("UserName", "mUserName");
        headers.put("Password", "mPassword");

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("param1", "My Param 1 From Form");
        parameters.put("param2", "My Param 2 From Form");
        parameters.put("StreetName", "Çatalca");
        parameters.put("NeighborhoodName", "BOZHANE");

        new EAUploadBuilder(this)
                .setUrl(url)
                .setHeaders(headers)
                .setParameters(parameters)
                .addFiles(files, fileNames)
                .setUploadListener(new EAUploadListener() {
                    @Override
                    public void onProgressChanged(float progress) {
                        String uploading = "EAUploading: " + String.valueOf(progress);
                        runOnUiThread(() -> tvResponse.setText(uploading));
                    }

                    @Override
                    public void onUploadResponse(String response) {
                        runOnUiThread(() -> tvResponse.setText(response));
                    }

                    @Override
                    public void onUploadError(String e) {
                        runOnUiThread(() -> tvResponse.setText(e));
                        tvResponse.setText(e);
                    }
                })
                .upload();
    }

    private void downloadTest(boolean withManager) {
        isDownloadingManager = withManager;
        isDownloadingCustom = !withManager;
        String fileUrl = "http://s1.picswalls.com/wallpapers/2017/12/10/4k-gaming-wallpaper_11062893_312.jpg";
        //String fileUrl = "http://ipv4.download.thinkbroadband.com/5MB.zip";

        builder = new EAFileDownloadBuilder(this)
                .setUrl(fileUrl)
                .setSaveToDownloads(true)
                // .setSubFolder("MyCustomDownloadsFolder")
                .setFileName("test_file.jpg")
                //.setDateModified(new Date())
                //.setHeaders(...)
                .setDownloadListener(new EADownloadListener() {
                    @Override
                    public void onProgress(float progress) {
                        String downloading = "EADownloading: " + String.valueOf(progress);
                        runOnUiThread(() -> tvResponse.setText(downloading));
                    }

                    @Override
                    public void onDownloadComplete(String filePath) {
                        runOnUiThread(() -> {
                            isDownloadingCustom = false;
                            isDownloadingManager = false;
                            ((Button) findViewById(R.id.btn_download_custom)).setText("Download Test Custom");
                            ((Button) findViewById(R.id.btn_download_manager)).setText("Download Test Manager");
                            ((TextView) findViewById(R.id.tv_response)).setText(filePath);
                            onFileClicked(filePath, null);
                        });
                    }

                    @Override
                    public void onDownloadError(String error) {
                        runOnUiThread(() -> {
                            tvResponse.setText(error);
                            isDownloadingCustom = false;
                            isDownloadingManager = false;
                            ((Button) findViewById(R.id.btn_download_custom)).setText("Download Test Custom");
                            ((Button) findViewById(R.id.btn_download_manager)).setText("Download Test Manager");
                        });
                    }
                });
        if (withManager)
            builder.downloadWithManager();
        else
            builder.download();
        if (isDownloadingCustom)
            ((Button) findViewById(R.id.btn_download_custom)).setText("Cancel Upload");
        if (isDownloadingManager)
            ((Button) findViewById(R.id.btn_download_manager)).setText("Cancel Upload");
    }
}
