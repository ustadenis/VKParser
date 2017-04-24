package com.app.vkparser.vkparser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    public static final String GROPE_ID_EXTRA = "grope_id_extra";
    public static final String QUERY_EXTRA = "query_extra";
    public static final String GROUP_EXTRA = "group__extra";

    private Unbinder mUnbinder;

    @BindView(R.id.grope_id_edit_text) EditText mIDEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.parse_button)
    public void parse(View view) {
        String gropeID = mIDEditText.getText().toString();
        if (gropeID != null && !gropeID.isEmpty()) {
            Intent startParsingActivity = new Intent(SettingsActivity.this, ParsingActivity.class);
            startParsingActivity.putExtra(GROPE_ID_EXTRA, gropeID);
            startActivity(startParsingActivity);
        }
    }

    @OnClick(R.id.search_button)
    public void search(View view) {
        String query = mIDEditText.getText().toString();
        if (query != null && !query.isEmpty()) {
            Intent startGropesActivity = new Intent(SettingsActivity.this, GropesSearchActivity.class);
            startGropesActivity.putExtra(QUERY_EXTRA, query);
            startActivity(startGropesActivity);
        }
    }
}
