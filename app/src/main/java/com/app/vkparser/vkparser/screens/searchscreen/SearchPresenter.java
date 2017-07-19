package com.app.vkparser.vkparser.screens.searchscreen;

import com.app.vkparser.vkparser.screens.common.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Denis on 27.06.2017.
 */

public interface SearchPresenter extends BasePresenter {
    void searchGoups(String query);
    void searchMoreGroups(String query, int offset);
}
