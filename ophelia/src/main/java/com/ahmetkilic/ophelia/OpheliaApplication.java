package com.ahmetkilic.ophelia;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.ahmetkilic.ophelia.ea_networking.VolleySingleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class OpheliaApplication extends Application {
    /*
     * Project application class should extend ApheliaApplication class.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize volley for application
        VolleySingleton.initInstance(getApplicationContext());
    }

    /**
     * Initialize Realm for project.
     * If realm is used, classpath "io.realm:realm-gradle-plugin:x.y.z" should be added in project level gradle file
     *
     * @param realmName Name for .realm file.
     */
    public void initRealm(String realmName){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(realmName + ".realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);

        /*
        If you use realm, add these lines to app build gradle android scope.
        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }

        or

        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_10
            targetCompatibility JavaVersion.VERSION_1_10
        }

        */

    }

    /**
     * If sdk version is bigger than 25, notification channels should be initialized with this function.
     *
     * @param channelID ID for notification channel.
     * @param channelName Name for notification channel.
     * @param channelDescription Description for notification channel.
     */
    public void initChannels(String channelID, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.canShowBadge();
        channel.setShowBadge(true);
        channel.setDescription(channelDescription);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
