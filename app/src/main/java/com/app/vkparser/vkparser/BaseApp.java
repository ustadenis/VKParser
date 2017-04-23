package com.app.vkparser.vkparser;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by denustav on 21/04/2017.
 */

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
    }
}
