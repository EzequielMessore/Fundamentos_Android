package com.br.fundamentosandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.br.fundamentosandroid.models.entities.User;
import com.br.fundamentosandroid.models.service.UserBusinessService;

public class SharedPreferenceUtil {

    public final static String PREFERENCE_NAME = "USER";
    public final static String PREFERENCE_ATTRIBUTE_USER_ID = "USER_ID";
    public final static String PREFERENCE_ATTRIBUTE_USER_NAME = "USER_NAME";
    public static final String PREFERENCE_CONNECT = "USER_CONNECT";
    private final static SharedPreferences preferences = AppUtil.CONTEXT_APPLICATION.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

    public static User getUserPreference() {
        return UserBusinessService.getUserById(preferences.getInt(PREFERENCE_ATTRIBUTE_USER_ID, 0));
    }

    public static boolean isUserConnect(){
        return preferences.getBoolean(PREFERENCE_CONNECT,false);
    }

    public static void disconnectUser(){
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(PREFERENCE_CONNECT);
        edit.commit();
    }
}
