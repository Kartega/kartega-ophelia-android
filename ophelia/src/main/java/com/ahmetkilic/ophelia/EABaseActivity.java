package com.ahmetkilic.ophelia;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ahmetkilic.ophelia.ea_networking.download.EADownloadListener;
import com.ahmetkilic.ophelia.ea_networking.download.EAFileDownloadBuilder;
import com.ahmetkilic.ophelia.ea_progress.CrystalPreloader;
import com.ahmetkilic.ophelia.ea_progress.EAProgressDialog;
import com.ahmetkilic.ophelia.ea_utilities.enums.LogType;
import com.ahmetkilic.ophelia.ea_utilities.interfaces.FileClickListener;
import com.ahmetkilic.ophelia.ea_utilities.tools.FileUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.ProviderUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.StringUtils;
import com.ahmetkilic.ophelia.ea_utilities.views.CustomToast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.Date;
import java.util.List;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EABaseActivity extends AppCompatActivity implements
        View.OnClickListener,
        FileClickListener,
        EADownloadListener {

    private String TAG;
    private EAProgressDialog eaProgressDialog;
    private CustomToast customToast;

    private EAFileDownloadBuilder downloader;

    private int clickDisableDuration;

    private boolean isClickDisabled;
    private Handler clickHandler;
    private Runnable clickRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView
        initBaseViews();
        initVariables();
    }

    private void initBaseViews() {

    }

    private void initVariables() {
        clickDisableDuration = 300;
        TAG = getClass().getSimpleName();
        eaProgressDialog = new EAProgressDialog(this);
        eaProgressDialog.setProgress(CrystalPreloader.Style.BALL_SPIN_FADE, CrystalPreloader.Size.SMALL,
                R.color.colorPrimary, R.color.colorPrimaryDark, 0);
        customToast = new CustomToast(this);

        clickHandler = new Handler();
        clickRunnable = new Runnable() {
            @Override
            public void run() {
                isClickDisabled = false;
            }
        };
    }

    /**
     * Set progress dialog in your base activity to change progress
     *
     * @param eaProgressDialog your custom dialog
     */
    public void setEaProgressDialog(EAProgressDialog eaProgressDialog) {
        this.eaProgressDialog = eaProgressDialog;
    }

    /**
     * Show full screen progress dialog
     */
    public void showProgress() {
        if (!isFinishing())
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (eaProgressDialog != null && !eaProgressDialog.isShowing())
                        eaProgressDialog.show();
                }
            });
    }

    /**
     * Hide full screen progress dialog
     */
    public void hideProgress() {
        if (!isFinishing()) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!EABaseActivity.this.isFinishing() && eaProgressDialog != null && eaProgressDialog.isShowing())
                            eaProgressDialog.dismiss();
                    }
                });
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Make log with activity name as tag if log is enabled
     */
    public void makeLog(LogType type, String message) {
        //if (ConstantValues.LOG_ENABLED)
        Log.v(TAG, "Type:" + String.valueOf(type) + "," + message);
    }

    /**
     * Open a file. If the file exists in device; just open it, else download and open the file.
     *
     * @param filePath     file path to open
     * @param dateModified last modified date of file
     */
    @Override
    public void onFileClicked(String filePath, @Nullable Date dateModified) {
        makeLog(LogType.INFO, "ClickedFile : " + String.valueOf(filePath));
        if (!StringUtils.isEmptyString(filePath)) {
            if (FileUtils.isFileExistsInDevice(filePath)) {
                ProviderUtils.openFileFromLocal(this, filePath);
            } else
                downloadFile(filePath, dateModified);
        } else
            showToast(getString(R.string.warning_invalid_url));
    }

    private void downloadFile(final String path, final Date dateModified) {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        showProgress();
                        downloader = new EAFileDownloadBuilder(EABaseActivity.this)
                                .setUrl(path)
                                .setDateModified(dateModified)
                                .setDownloadListener(EABaseActivity.this)
                                .setSaveToDownloads(true);
                        downloader.downloadWithManager();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        showToast(R.string.write_permission_required_to_download_file);
                    }
                })
                .setDeniedMessage(R.string.write_permission_required_to_download_file)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }

    @Override
    protected void onDestroy() {
        if (downloader != null)
            downloader.unregisterManager();
        super.onDestroy();
    }

    /**
     * Show custom toast from resource id
     */
    public void showToast(int message) {
        showToast(getString(message));
    }

    /**
     * Show custom toast with string
     */
    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customToast != null)
                    customToast.showToast(message);
                else
                    Toast.makeText(EABaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isClickDisabled() {
        return isClickDisabled;
    }

    public void enableClick() {
        clickHandler.removeCallbacks(clickRunnable);
        isClickDisabled = false;
    }

    public void disableClickTemporarily() {
        isClickDisabled = true;
        clickHandler.postDelayed(clickRunnable, clickDisableDuration);

    }

    @Override
    public void onClick(View v) {
        disableClickTemporarily();
    }

    @Override
    public void onProgress(float progress) {

    }

    @Override
    public void onDownloadComplete(String filePath) {
        hideProgress();
        ProviderUtils.openFileFromLocal(this, filePath);

    }

    @Override
    public void onDownloadError(String error) {
        hideProgress();
        showToast(getString(R.string.warning_download_not_completed));
    }
}
