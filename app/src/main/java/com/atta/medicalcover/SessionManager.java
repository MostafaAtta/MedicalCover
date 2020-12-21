package com.atta.medicalcover;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context mContext;
    private static SessionManager mInstance;

    private static final String PREF_NAME = "med_pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_IS_LOGIN = "is_login";

    //mode 0 private
    private static final int MODE = 0;

    private SessionManager(Context mContext) {
        this.mContext = mContext;
        pref = mContext.getSharedPreferences(PREF_NAME, MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context){
        if (mInstance == null){
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    public void login(User user){
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_USERNAME, user.getFullName());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_CITY, user.getCity());
        editor.putBoolean(KEY_IS_LOGIN, true);

        editor.apply();
    }


    public String getEmail(){

        return pref.getString(KEY_EMAIL, "no mail");
    }

    public String getUsername(){
        String name = pref.getString(KEY_USERNAME, "no name");

        return name;
    }

    public boolean isLogin(){
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void logout(){
        editor.clear();
        editor.putBoolean(KEY_IS_LOGIN, false);
        editor.apply();
    }
}
