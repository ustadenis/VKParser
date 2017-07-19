package com.app.vkparser.vkparser.interactors;

import android.util.Log;

import com.app.vkparser.vkparser.Variables;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Denis on 27.06.2017.
 */

public class GroupSearchInteractor {

    public Observable<List<HashMap<String, Object>>> searchGoups(String query, int offset) {
        return Observable.create(emitter -> {
            final List<HashMap<String, Object>> groupsList = new ArrayList<>();

            VKRequest request = VKApi.groups().search(VKParameters.from(VKApiConst.Q, query, VKApiConst.OFFSET, offset));
            request.executeSyncWithListener(new VKRequest.VKRequestListener() {

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    emitter.onError(new Exception(error.apiError.errorMessage));
                }

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
                            emitter.onError(e);
                        }
                    }

                    emitter.onNext(groupsList);
                    emitter.onComplete();
                }
            });
        });
    }

}
