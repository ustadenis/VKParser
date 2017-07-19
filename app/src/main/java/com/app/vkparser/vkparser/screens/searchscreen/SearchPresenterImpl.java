package com.app.vkparser.vkparser.screens.searchscreen;

import com.app.vkparser.vkparser.interactors.GroupSearchInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Denis on 27.06.2017.
 */

public class SearchPresenterImpl implements SearchPresenter {

    private GroupSearchInteractor mGroupSearchInteractor;
    private CompositeDisposable mCompositeDisposable;

    private SearchView mView;

    public SearchPresenterImpl(SearchView view) {
        mGroupSearchInteractor = new GroupSearchInteractor();
        mCompositeDisposable = new CompositeDisposable();

        mView = view;
    }

    @Override
    public void searchGoups(String query) {
        mCompositeDisposable.add(mGroupSearchInteractor.searchGoups(query, 0)
                                                       .subscribeOn(Schedulers.io())
                                                       .observeOn(AndroidSchedulers.mainThread())
                                                       .subscribe(
                                                               response -> mView.onGroupsFound(response),
                                                               error -> mView.onError(error)
                                                       ));
    }

    @Override
    public void searchMoreGroups(String query, int offset) {
        mCompositeDisposable.add(mGroupSearchInteractor.searchGoups(query, offset)
                                                       .subscribeOn(Schedulers.io())
                                                       .observeOn(AndroidSchedulers.mainThread())
                                                       .subscribe(
                                                               response -> mView.onMoreGroupsFound(response),
                                                               error -> mView.onError(error)
                                                       ));
    }

    @Override
    public void release() {
        mCompositeDisposable.dispose();
    }
}
