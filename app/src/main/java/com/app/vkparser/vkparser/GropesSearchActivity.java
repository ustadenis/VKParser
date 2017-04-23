package com.app.vkparser.vkparser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GropesSearchActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private Unbinder mUnbinder;
    private String mQuery;
    private GropesAdapter mAdapter;

    private List<HashMap<String, Object>> groupsArray = new ArrayList<>();

    @BindView(R.id.goupes_listview) ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gropes_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(SettingsActivity.QUERY_EXTRA)) {
            mQuery = intent.getStringExtra(SettingsActivity.QUERY_EXTRA);
        }

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
                        for (int i = 0; i < groupsArrayJson.length(); i++) {
                            JSONObject p = groupsArrayJson.getJSONObject(i);
                            //Создаем некоторые переменные для дальнейшей работы
                            String imageGroup = p.getString(Variables.TAG_IMAGE); // достаем миниатюру
                            String idGroup = p.getString(Variables.TAG_ID); //достаем id
                            String nameGroup = p.getString(Variables.TAG_NAME); //достаем навание
                            String accessGroup = p.getString("is_closed"); // закрытое или открытое сообщество
                            String typeGroup = p.getString("type"); // тип сообщества
                            String typeAccess = p.getString("is_admin"); // тип доступа
                            String members_count = p.getString(Variables.TAG_MEMBERS_COUNT);//кол-во участников

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
                            groupsArray.add(groupsHash);

                            mAdapter = new GropesAdapter(GropesSearchActivity.this, groupsArray);
                            mListView.setAdapter(mAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONError", e.toString());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
