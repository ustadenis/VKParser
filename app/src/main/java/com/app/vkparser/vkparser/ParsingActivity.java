package com.app.vkparser.vkparser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ParsingActivity extends AppCompatActivity {

    private static final String TAG = ParsingActivity.class.getSimpleName();

    private Unbinder mUnbinder;
    private String mGropeID;
    private MemberAdapter mAdapter;

    private List<String> idsArray = new ArrayList<>();

    @BindView(R.id.members_listview) ListView mListView;

    GetUsersAsyncTask task;

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
        } else if(intent.hasExtra(SettingsActivity.GROUP_EXTRA)) {
            HashMap<String, Object> group = (HashMap<String, Object>) intent.getSerializableExtra(SettingsActivity.GROUP_EXTRA);
            mGropeID = group.get(Variables.TAG_ID).toString();
        }

        mAdapter = new MemberAdapter(ParsingActivity.this, new ArrayList<VKApiUserFull>());
        mListView.setAdapter(mAdapter);

        VKRequest request = VKApi.groups().getMembers(VKParameters.from(VKApiConst.GROUP_ID, mGropeID));
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
                            String userID = groupsArrayJson.getString(i);

                            // Временная переменная hashmap для одиночной записи
                            //HashMap<String, Object> groupsHash = new HashMap<>();
                            // Добовляем значения в наш HashMap в виде key => value
                            //groupsHash.put(Variables.TAG_ID, userID);
                            // Добовляем группы в лист групп
                            idsArray.add(userID);
                        }

                        getUsersByIds();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONError", e.toString());
                    }
                }
            }
        });
    }

    private void getUsersByIds() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        task = new GetUsersAsyncTask();
        task.execute();
    }

    private void getUserByIDs(String id, final MemberAdapter.OnUserReady listener) {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, id, VKApiConst.FIELDS, "id, name, sex, city, photo_50"));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                final VKApiUserFull user = ((VKList<VKApiUserFull>) response.parsedModel).get(0);
                listener.onUser(user);
            }
        });
    }

    class GetUsersAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            for (String id:
                    idsArray) {
                if (isCancelled()) {
                    break;
                }
                getUserByIDs(id, new MemberAdapter.OnUserReady() {
                    @Override
                    public void onUser(VKApiUserFull user) {
                        mAdapter.add(user);
                    }
                });
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
        mUnbinder.unbind();
    }
}
