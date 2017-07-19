package com.app.vkparser.vkparser.screens.searchscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.EditText;

import com.app.vkparser.vkparser.adapters.GroupsAdapter;
import com.app.vkparser.vkparser.R;
import com.app.vkparser.vkparser.screens.common.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class SearchActivity extends BaseActivity implements SearchView,
        SwipeRefreshLayout.OnRefreshListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private Unbinder mUnbinder;
    private SearchPresenter mSearchPresenter;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_search_edittext)
    EditText mSearchEditText;
    @BindView(R.id.refrash_layout)
    SwipeRefreshLayout mRefreshLayout;

    private GroupsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mUnbinder = ButterKnife.bind(this);

        mSearchPresenter = new SearchPresenterImpl(this);

        initToolBar();

        initRecyclerView();

        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        mSearchPresenter.release();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {
        mSearchPresenter.searchGoups(mSearchEditText.getText().toString());
    }

    @OnTextChanged(value = R.id.toolbar_search_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onQueryTextChanged (Editable query){
        mSearchPresenter.searchGoups(query.toString());
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onGroupsFound(List<HashMap<String, Object>> groups) {
        mAdapter.updateGroups(groups);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMoreGroupsFound(List<HashMap<String, Object>> groups) {
        mAdapter.addGroups(groups);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(Throwable t) {
        Snackbar.make(mRecyclerView, t.toString(), Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.setRefreshing(false);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new GroupsAdapter(this, new ArrayList<>());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new OnScrollListener());
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastPosition = mLayoutManager.findLastVisibleItemPosition();
            int size = mAdapter.getItemCount();
            if (lastPosition == size - 1) {
                mSearchPresenter.searchMoreGroups(mSearchEditText.getText().toString(), size);
                mRefreshLayout.setRefreshing(true);
            }
        }
    }
}
