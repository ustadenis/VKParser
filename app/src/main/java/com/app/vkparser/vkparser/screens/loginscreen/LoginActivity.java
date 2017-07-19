package com.app.vkparser.vkparser.screens.loginscreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.app.vkparser.vkparser.R;
import com.app.vkparser.vkparser.screens.searchscreen.SearchActivity;
import com.app.vkparser.vkparser.screens.common.BasePermissionActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class LoginActivity extends BasePermissionActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected String[] getDesiredPermissions() {
        return new String[] { Manifest.permission.INTERNET };
    }

    @Override
    protected void onPermissionDenied() {
        Log.d(TAG, String.valueOf(PackageManager.PERMISSION_DENIED));
    }

    @Override
    protected void onPermissionGranted() {
        Log.d(TAG, String.valueOf(PackageManager.PERMISSION_GRANTED));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(VKSdk.isLoggedIn()) {
            startSettingsActivity();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            VKSdk.login(this, VKScope.GROUPS, VKScope.FRIENDS, VKScope.PAGES, VKScope.PHOTOS, VKScope.WALL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    startSettingsActivity();
                }
                @Override
                public void onError(VKError error) {
                    VKSdk.login(LoginActivity.this, VKScope.GROUPS, VKScope.FRIENDS, VKScope.PAGES, VKScope.PHOTOS, VKScope.WALL);
                }
            })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startSettingsActivity() {
        Intent startSettingsActivityIntent = new Intent(LoginActivity.this, SearchActivity.class);
        startActivity(startSettingsActivityIntent);
        finish();
    }
}
