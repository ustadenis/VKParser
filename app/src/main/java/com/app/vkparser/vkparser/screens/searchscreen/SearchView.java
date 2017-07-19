package com.app.vkparser.vkparser.screens.searchscreen;

import com.app.vkparser.vkparser.screens.common.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Denis on 27.06.2017.
 */

public interface SearchView extends BaseView {
    void onGroupsFound(List<HashMap<String, Object>> groups);
    void onMoreGroupsFound(List<HashMap<String, Object>> groups);
}
