package com.app.vkparser.vkparser.screens;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.vkparser.vkparser.adapters.GroupsAdapter;
import com.app.vkparser.vkparser.R;
import com.app.vkparser.vkparser.Variables;
import com.app.vkparser.vkparser.screens.searchscreen.SearchActivity;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GropesSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private Unbinder mUnbinder;
    private String mQuery;
    private GroupsAdapter mAdapter;

    @BindView(R.id.goupes_listview) ListView mListView;

    SearchGroupsAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gropes_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUnbinder = ButterKnife.bind(this);

        mListView.setOnItemClickListener(this);

        searchGroups();
    }

    private void searchGroups() {
        if (task != null) {
            task.cancel(true);
        }
        task = new SearchGroupsAsyncTask();
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        mUnbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*HashMap<String, Object> group = (HashMap<String, Object>) parent.getAdapter().getItem(position);

        Intent startParsingActivity = new Intent(GropesSearchActivity.this, ParsingActivity.class);
        startParsingActivity.putExtra(SearchActivity.GROUP_EXTRA, group);
        startActivity(startParsingActivity);*/
    }

    class SearchGroupsAsyncTask extends AsyncTask {

        List<HashMap<String, Object>> groupsList = new ArrayList<>();

        @Override
        protected Object doInBackground(Object[] params) {
            VKRequest request = VKApi.groups().search(VKParameters.from(VKApiConst.Q, mQuery));
            request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    String groups = response.responseString;
                    if (groups != null) { //Проверяем не пустой ли нам пришел ответ
                        try {
                            JSONObject jsonParent = new JSONObject(groups); // Создаем переменную типа JSONObject и принимаем строку с JSON
                            JSONObject jsonObject = jsonParent.getJSONObject("response");//выбираем родителя в строке с JSON
                            String countGroups = jsonObject.getString("count"); //получаем кол-во групп

                            // Получаем массив из нашего объекта
                            JSONArray groupsArrayJson = jsonObject.getJSONArray("items");

                            // Проходим по всем группам циклом, для создания ассоциативного массива
                            for (int i = 0; i < groupsArrayJson.length() && !isCancelled(); i++) {
                                JSONObject p = groupsArrayJson.getJSONObject(i);
                                //Создаем некоторые переменные для дальнейшей работы
                                String imageGroup = p.optString(Variables.TAG_IMAGE, ""); // достаем миниатюру
                                String idGroup = p.optString(Variables.TAG_ID, ""); //достаем id
                                String nameGroup = p.optString(Variables.TAG_NAME, ""); //достаем навание
                                String accessGroup = p.optString("is_closed", ""); // закрытое или открытое сообщество
                                String typeGroup = p.optString("type", ""); // тип сообщества
                                String typeAccess = p.optString("is_admin", ""); // тип доступа
                                String members_count = p.optString(Variables.TAG_MEMBERS_COUNT, "");//кол-во участников

                                // Временная переменная hashmap для одиночной записи
                                HashMap<String, Object> groupsHash = new HashMap<>();
                                // Добовляем значения в наш HashMap в виде key => value
                                groupsHash.put(Variables.TAG_IMAGE, imageGroup);
                                groupsHash.put(Variables.TAG_ID, idGroup);
                                groupsHash.put(Variables.TAG_NAME, nameGroup);
                                groupsHash.put("is_closed", accessGroup);
                                groupsHash.put("type", typeGroup);
                                groupsHash.put("is_admin", typeAccess);
                                groupsHash.put(Variables.TAG_MEMBERS_COUNT, members_count);
                                // Добовляем группы в лист групп
                                groupsList.add(groupsHash);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", e.toString());
                        }
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            mAdapter = new GroupsAdapter(GropesSearchActivity.this, groupsList);
            //mListView.setAdapter(mAdapter);
            super.onPostExecute(o);
        }
    }
}
