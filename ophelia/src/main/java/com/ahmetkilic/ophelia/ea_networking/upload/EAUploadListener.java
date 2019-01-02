package com.ahmetkilic.ophelia.ea_networking.upload;


/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public interface EAUploadListener {
    void onProgressChanged(float progress);
    void onUploadResponse(String response);
    void onUploadError(String error);
}
