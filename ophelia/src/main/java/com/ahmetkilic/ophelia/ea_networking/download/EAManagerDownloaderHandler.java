package com.ahmetkilic.ophelia.ea_networking.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_utilities.tools.FileUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.StringUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Ahmet Kılıç on 27.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class EAManagerDownloaderHandler {
    private EADownloadListener listener;
    private String fileNameWithExtension;
    private String url;
    private HashMap<String, String> headers;
    private String subFolder;
    private WeakReference<Context> contextWeakReference;

    private DownloadManager mgr;
    private BroadcastReceiver downloadReceiver;
    private long downloadID = -1L;


    private boolean downloadCompleted;

    EAManagerDownloaderHandler(EADownloadListener downloadListener, String fileNameWithExtension, String url,
                               HashMap<String, String> headers, String subFolder, Context context) {
        this.listener = downloadListener;
        this.fileNameWithExtension = fileNameWithExtension;
        this.url = url;
        this.headers = headers;
        this.subFolder = subFolder;
        this.contextWeakReference = new WeakReference<>(context);

        mgr = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    }

    void startDownload() {
        registerReceivers();

        Boolean folderCreated = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        if (folderCreated)
            Log.i("EAFileDownloader", "Downloads folder is created");

        String dir = getSubFolderWithSeparator(false) + fileNameWithExtension;

        Uri sourceUri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager
                .Request(sourceUri)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dir);

        request.allowScanningByMediaScanner();

        if (headers != null)
            for (Map.Entry<String, String> entry : headers.entrySet())
                request.addRequestHeader(entry.getKey(), entry.getValue());

        downloadID = mgr.enqueue(request);
        startProgressThread();
    }

    void cancel() {
        mgr.remove(downloadID);
        String msg = "Download Cancelled.";
        if (isContextValid())
            msg = contextWeakReference.get().getString(R.string.error_cancelled);

        String filePath = FileUtils.getDownloadsPath() + getSubFolderWithSeparator(true) + fileNameWithExtension;
        FileUtils.deleteFileOrFolder(new File(filePath));

        deliverResult(false, msg);
    }


    private String getSubFolderWithSeparator(boolean includeSeperatorBefore) {
        String path = "";

        if (!StringUtils.isEmptyString(subFolder)) {
            if (includeSeperatorBefore)
                path = File.separator;
            path += subFolder + File.separator;
        } else if (includeSeperatorBefore)
            path = File.separator;

        return path;
    }

    private boolean isContextValid() {
        return contextWeakReference != null && contextWeakReference.get() != null;
    }

    private void registerReceivers() {
        if (!isContextValid())
            return;

        if (downloadReceiver == null)
            downloadReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction()))
                        queryStatus();
                }
            };

        contextWeakReference.get().registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    void unRegisterReceivers() {
        if (!isContextValid())
            return;
        if (downloadReceiver != null) {
            try {
                contextWeakReference.get().unregisterReceiver(downloadReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void queryStatus() {
        String error = "";
        Cursor c = mgr.query(new DownloadManager.Query().setFilterById(downloadID));
        if (c == null) {
            deliverResult(false, error);
        } else {
            if (isColumnContained(c) && c.getCount() > 0) {
                c.moveToFirst();
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL)
                    deliverResult(true, error);
                else
                    deliverResult(false, error);
            } else
                deliverResult(false, error);
        }
    }

    private void startProgressThread() {
        Thread mProgressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadID);
                Cursor c;
                while (true) {
                    if (downloadCompleted)
                        return;
                    c = mgr.query(q);
                    if (c != null) {
                        c.moveToFirst();
                        int bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);
                        listener.onProgress(dl_progress);
                    }
                }
            }
        });

        mProgressThread.start();
    }

    private void deliverResult(boolean success, String error) {
        downloadCompleted = true;
        String path = FileUtils.getDownloadsPath() + getSubFolderWithSeparator(true) + fileNameWithExtension;

        //Todo: File duplicates with sub folder
        if (!StringUtils.isEmptyString(subFolder)) {
            File temp = new File(FileUtils.getDownloadsPath() + File.separator + fileNameWithExtension);
            FileUtils.deleteFileOrFolder(temp);
        }

        if (listener != null) {
            if (success)
                listener.onDownloadComplete(path);
            else
                listener.onDownloadError(error);
        }
        unRegisterReceivers();
    }

    private boolean isColumnContained(Cursor c) {
        boolean contains = false;
        if (c != null && c.getColumnNames() != null)
            for (String name : c.getColumnNames()) {
                if (name.equals(DownloadManager.COLUMN_STATUS))
                    contains = true;
            }
        return contains;
    }
}
