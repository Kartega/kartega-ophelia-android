package com.ahmetkilic.ophelia.ea_utilities.tools;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.ahmetkilic.ophelia.R;

import java.io.File;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ProviderUtils {

    /**
     *
     * Do not forget to put provider in manifest.
     *
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true"
            tools:replace="android:authorities">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"
                tools:replace="android:resource" />
        </provider>
     */

    /**
     * Share a file from an activity with FileProvider.
     *
     * @param activity activity
     * @param file file to share
     */
    private static void shareCompat(Activity activity, File file) {

        Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".fileprovider", file);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.toString()));
        ShareCompat.IntentBuilder.from(activity)
                .setType(mimeType)
                .addStream(uri)
                .setChooserTitle(activity.getString(R.string.chooser_title))
                .startChooser();
    }

    /**
     * Open a file from device storage
     *
     * @param context context
     * @param filePath file path to open
     */
    public static void openFileFromLocal(Context context, String filePath) {
        File file = new File(filePath);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.toString()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+ ".fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, mimeType);

            try {
                Intent chooser = Intent.createChooser(intent, context.getString(R.string.chooser_title));
                context.startActivity(chooser);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, context.getString(R.string.error_no_application_to_view), Toast.LENGTH_SHORT).show();
            }

        } else {
            Uri apkUri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, mimeType);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
