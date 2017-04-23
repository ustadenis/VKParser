package com.app.vkparser.vkparser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VKParserActivity extends AppCompatActivity {

    private static final String TAG = VKParserActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_INTERNET = 100;

    private Unbinder mUnbinder;

    @BindView(R.id.fingerprint) TextView mFingerPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vkparser);
        mUnbinder = ButterKnife.bind(this);

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        mFingerPrint.setText(fingerprints[0]);

        checkForPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.login_button)
    public void login(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            VKSdk.login(this, VKScope.GROUPS, VKScope.FRIENDS, VKScope.PAGES, VKScope.PHOTOS, VKScope.WALL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    Log.e(TAG, res.toString());
                    Intent startSettingsActivityIntent = new Intent(VKParserActivity.this, SettingsActivity.class);
                    startActivity(startSettingsActivityIntent);
                }
                @Override
                public void onError(VKError error) {
                    Log.e(TAG, error.toString());
                }
            })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        PERMISSIONS_REQUEST_INTERNET);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, String.valueOf(PackageManager.PERMISSION_GRANTED));
                } else {
                    Log.e(TAG, String.valueOf(PackageManager.PERMISSION_DENIED));
                }
                break;
            }
        }
    }
}
