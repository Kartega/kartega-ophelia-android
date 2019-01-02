package com.ahmetkilic.ophelia.ea_slider;

import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EASlide implements Serializable {
    private transient Bundle extras;

    private String path;
    private File file;
    private int resourceId;
    private Uri uri;

    /**
     * Create a Slide Item from File
     *
     * @param file File from device
     */
    public EASlide(File file) {
        this.file = file;
    }
    /**
     * Create a Slide Item from URL
     *
     * @param path Image path from web
     */
    public EASlide(String path) {
        this.path = path;
    }
    /**
     * Create a Slide Item from Resources
     *
     * @param resourceId image from resources
     */
    public EASlide(int resourceId) {
        this.resourceId = resourceId;
    }
    /**
     * Create a Slide Item from File
     *
     * @param uri Uri for image.
     */
    public EASlide(Uri uri) {
        this.uri = uri;
    }
    /**
     * Give extra variables to EASlide as Bundle
     *
     * @param extras extra values
     */
    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public Bundle getExtras() {
        if (extras == null)
            extras = new Bundle();

        return extras;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public int getResourceId() {
        return resourceId;
    }

    public Uri getUri() {
        return uri;
    }
}
