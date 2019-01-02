package com.ahmetkilic.ophelia.ea_networking.upload.ProgressRequest;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public abstract class ProgressListener implements ProgressCallback {
    boolean started;
    long lastRefreshTime = 0L;
    long lastBytesWritten = 0L;
    int minTime = 100;

    public final void onProgressChanged(long numBytes, long totalBytes, float percent) {
        if (!started) {
            onProgressStart(totalBytes);
            started = true;
        }
        if (numBytes == -1 && totalBytes == -1 && percent == -1) {
            onProgressChanged(-1, -1, -1, -1);
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefreshTime >= minTime || numBytes == totalBytes || percent >= 1F) {
            long intervalTime = (currentTime - lastRefreshTime);
            if (intervalTime == 0) {
                intervalTime += 1;
            }
            long updateBytes = numBytes - lastBytesWritten;
            final long networkSpeed = updateBytes / intervalTime;
            onProgressChanged(numBytes, totalBytes, percent, networkSpeed);
            lastRefreshTime = System.currentTimeMillis();
            lastBytesWritten = numBytes;
        }
        if (numBytes == totalBytes || percent >= 1F) {
            onProgressFinish();
        }
    }

    public abstract void onProgressChanged(long numBytes, long totalBytes, float percent, float speed);

    public void onProgressStart(long totalBytes) {
    }

    public void onProgressFinish() {
    }
}
