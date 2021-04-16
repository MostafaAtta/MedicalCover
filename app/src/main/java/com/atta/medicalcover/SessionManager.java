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
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_MEMBERSHIP_NO = "membership_number";
    private static final String KEY_POLICY_HOLDER = "policy_holder";
    private static final String KEY_POLICY_NO = "policy_number";
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
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_DATE_OF_BIRTH, user.getDateOfBirth());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_MEMBERSHIP_NO, user.getMembershipNumber());
        editor.putString(KEY_POLICY_NO, user.getPolicyNumber());
        editor.putString(KEY_POLICY_HOLDER, user.getPolicyHolder());
        editor.putBoolean(KEY_IS_LOGIN, true);

        editor.apply();
    }


    public String getEmail(){

        return pref.getString(KEY_EMAIL, "no mail");
    }
    public String getUserId(){

        return pref.getString(KEY_USER_ID, "");
    }

    public String getUsername(){
        String name = pref.getString(KEY_USERNAME, "no name");

        return name;
    }
    public String getMembershipNo(){

        return pref.getString(KEY_MEMBERSHIP_NO, "");
    }
    public String getPolicyNo(){

        return pref.getString(KEY_POLICY_NO, "");
    }
    public String getDateOfBirth(){

        return pref.getString(KEY_DATE_OF_BIRTH, "");
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
