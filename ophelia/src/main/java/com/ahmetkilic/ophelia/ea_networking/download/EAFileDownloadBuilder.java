package com.ahmetkilic.ophelia.ea_networking.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_utilities.tools.FileUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ahmet Kılıç on 26.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EAFileDownloadBuilder {

    private String url;
    private String fileName;
    private Context context;
    private Date dateModified;
    private HashMap<String, String> headers;
    private EADownloadListener downloadListener;

    private EAManagerDownloaderHandler managerDownloaderHandler;
    private EAFileDownloaderTask fileDownloaderTask;

    private boolean saveToDownloads;
    private String subFolder;
    private File saveLocationFolder;

    private boolean isDownloadingWithManager;

    public EAFileDownloadBuilder(Context context) {
        this.context = context;
    }

    /**
     * Set the url of the download request
     *
     * @param url url
     * @return builder
     */
    public EAFileDownloadBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Add a time value to the file's name to save same file multiple times with different dates
     *
     * @param dateModified date value
     * @return builder
     */
    public EAFileDownloadBuilder setDateModified(Date dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    /**
     * Set a custom file name for the file. If not set, name will be taken from the url
     *
     * @param fileName file name
     * @return builder
     */
    public EAFileDownloadBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * If set true, the file will be downloaded to the downloads folder, else it will be downloaded to the cache folder of the app
     *
     * @param saveToDownloads value
     * @return builder
     */
    public EAFileDownloadBuilder setSaveToDownloads(boolean saveToDownloads) {
        Boolean folderCreated = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        if (folderCreated)
            Log.i("EAFileDownloader", "Downloads folder is created");
        this.saveToDownloads = saveToDownloads;
        return this;
    }

    /**
     * Because of a duplication bug, do not use with manager downloads.
     * Create a sub folder with given name
     *
     * @param subFolder name of the sub folder
     * @return builder
     */
    public EAFileDownloadBuilder setSubFolder(String subFolder) {
        this.subFolder = subFolder;
        return this;
    }

    /**
     * Set a custom download location
     *
     * @param customDownloadLocation location folder
     * @return builder
     */
    public EAFileDownloadBuilder setCustomDownloadLocation(File customDownloadLocation) {
        this.saveLocationFolder = customDownloadLocation;
        return this;
    }

    /**
     * Set a listener for the request's progress and response
     *
     * @param downloadListener listener
     * @return builder
     */
    public EAFileDownloadBuilder setDownloadListener(EADownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    /**
     * Set headers of the request
     *
     * @param headers headers
     * @return builder
     */
    public EAFileDownloadBuilder setHeaders(HashMap<String, String> headers) {
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
    public EAFileDownloadBuilder addHeader(String key, String value) {
        if (headers == null)
            headers = new HashMap<>();
        headers.put(key, value);
        return this;
    }

    public void download() {
        downloadWith(false);
    }

    public void downloadWithManager() {
        downloadWith(true);
    }

    /**
     * Start the download with
     */
    private void downloadWith(boolean manager) {
        this.isDownloadingWithManager = manager;
        createVariables();
        File file1 = new File(saveLocationFolder.getAbsolutePath() + File.separator + fileName);
        File file2 = new File(url);
        if (file1.isFile()) {
            downloadListener.onDownloadComplete(saveLocationFolder.getAbsolutePath() + File.separator + fileName);
        } else if (file2.isFile()) {
            downloadListener.onDownloadComplete(url);
        } else {
            if (manager) {
                managerDownloaderHandler = new EAManagerDownloaderHandler(downloadListener, fileName, url, headers, subFolder, context);
                managerDownloaderHandler.startDownload();
            } else {
                fileDownloaderTask = new EAFileDownloaderTask(downloadListener, url, fileName, saveLocationFolder, context, headers);
                fileDownloaderTask.execute();
            }
        }
    }

    /**
     * Cancel the download
     */
    public void cancel() {
        if (!isDownloadingWithManager)
            fileDownloaderTask.cancel(true);
        else {
            managerDownloaderHandler.cancel();
            managerDownloaderHandler.unRegisterReceivers();
        }
    }

    /**
     * If using the manager, you can unregister download manager's broadcast receiver manually
     */
    public void unregisterManager() {
        managerDownloaderHandler.unRegisterReceivers();
    }

    //--------- Private functions ---------------//

    private void createVariables() {
        //Create the file name
        String dateValue = FileUtils.getTimeFromDate(dateModified);
        String fileNameWithExtension;
        if (!StringUtils.isEmptyString(fileName)) {
            if (!fileName.contains("."))
                fileNameWithExtension = fileName + dateValue + '.' + FileUtils.getExtensionFromPath(url);
            else
                fileNameWithExtension = fileName.substring(0, fileName.indexOf(".")) + dateValue + '.' + FileUtils.getExtensionFromPath(fileName);
        } else
            fileNameWithExtension = FileUtils.getFileNameToSave(url, dateModified);

        fileName = fileNameWithExtension;

        if (saveLocationFolder == null) {
            //Create or select the save destination
            String dir;
            if (saveToDownloads)
                dir = FileUtils.getDownloadsPath();
            else
                dir = context.getCacheDir().getAbsolutePath();

            if (!StringUtils.isEmptyString(subFolder))
                dir += File.separator + subFolder;

            File temp = new File(dir);
            if (!temp.exists()) {
                Boolean folderCreatedTemp = temp.mkdir();
                if (!folderCreatedTemp) {
                    if (downloadListener != null)
                        downloadListener.onDownloadError(context.getString(R.string.filer_not_found_exception));
                    return;
                }
            }

            saveLocationFolder = new File(dir);
        }
    }

}
