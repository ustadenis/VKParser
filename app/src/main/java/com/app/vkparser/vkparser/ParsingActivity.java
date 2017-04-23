package com.app.vkparser.vkparser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ParsingActivity extends AppCompatActivity {

    private static final String TAG = ParsingActivity.class.getSimpleName();

    private Unbinder mUnbinder;
    private String mGropeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(SettingsActivity.GROPE_ID_EXTRA)) {
            mGropeID = intent.getStringExtra(SettingsActivity.GROPE_ID_EXTRA);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void getGropes() {
        //VKRequest request = VKApi.groups().getById()
    }
}
