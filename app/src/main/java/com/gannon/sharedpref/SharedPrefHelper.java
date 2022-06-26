package com.gannon.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.gannon.Login.interactor.model.LoginActivityRes;
import com.gannon.splash.interactor.model.CheckForUpdateRes;
import com.google.gson.Gson;


public class SharedPrefHelper {

    // Shared preferences file name
    public static final String PREFER_NAME = "Reg";
    public static final String KEY_NotificationRegID = "regid";
    // Editor reference for Shared preferences
    static SharedPreferences.Editor editor;
    private static SharedPrefHelper objInstance;
    int PRIVATE_MODE = 0;
    // Shared Preferences reference
    SharedPreferences pref;
    // Context
    Context _context;


    // Constructor
    public SharedPrefHelper(Context context) {
        context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void checkUpdate(Context context, CheckForUpdateRes checkForUpdateRes) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "CheckUpdate", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        Gson gson = new Gson();
        String json = gson.toJson(checkForUpdateRes);
        editor.putString("checkUpdate_data", json);
        editor.commit();
    }

    public static CheckForUpdateRes getCheckUpdate(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "CheckUpdate", Context.MODE_PRIVATE);
        String user_data = prefsLogin.getString("checkUpdate_data", "");
        CheckForUpdateRes checkUpdate_dataRes = new Gson().fromJson(user_data, CheckForUpdateRes.class);
        return checkUpdate_dataRes;
    }

    public static SharedPrefHelper getInstance(Context context) {
        if (objInstance == null) {
            objInstance = new SharedPrefHelper(context);
        }
        return objInstance;
    }

    public static void saveLoginDts(Context context, LoginActivityRes userLoginActivityRes) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userLoginActivityRes);
        editor.putString("user_data", json);
        editor.commit();
    }

    public static LoginActivityRes getLogin(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS", Context.MODE_PRIVATE);
        String user_data = prefsLogin.getString("user_data", "");
        LoginActivityRes loginActivityRes = new Gson().fromJson(user_data, LoginActivityRes.class);
        return loginActivityRes;
    }

    public static void clearLoginData(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        editor.putString("user_data", null);
        editor.commit();

    }

    public void SaveNotificationRegID(String regid) {
        editor.putString(KEY_NotificationRegID, regid);
        editor.commit();
    }

    public String getNotificationRegID() {
        return pref.getString(KEY_NotificationRegID, "");
    }


}
