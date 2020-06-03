package kg.buhara.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;

import kg.buhara.data.model.Address;
import kg.buhara.data.model.ProfileResponseModel;

/**
 * User data stored in system Shared Preferences
 * data like tokens
 */
public class UserInfoPref {
    private static final String ACCESS_TOKEN = "access-token";
    private static final String SETTINGS_STORAGE_NAME = "ds.credo";
    private static final String USER_INFO = "user-info";
    private static final String ADDRESS = "address-info";

    public static boolean initIntertnet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static String getString(Context context, String key, String defValue) {
        return getPrefs(context).getString(key, defValue);
    }

    public static boolean isAuth(Context context) {
        if (UserInfoPref.getAccessToken(context) != null && !UserInfoPref.getAccessToken(context).isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isUserInfo(Context context) {
        if (UserInfoPref.getUserString(context) != null && !UserInfoPref.getUserString(context).isEmpty()) {
            return true;
        }
        return false;
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SETTINGS_STORAGE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(SETTINGS_STORAGE_NAME, Context.MODE_PRIVATE).edit();
    }

    public static String getAccessToken(Context context) {
        return getString(context, ACCESS_TOKEN, "");
    }

    public static void setAccessToken(Context context, String token) {
        getEditor(context).putString(ACCESS_TOKEN, token).commit();
    }

    public static void setUserToken(Context context, String token) {
        Log.d("UserInfoPref", "" + token);

        setAccessToken(context, token);
    }

    //User

    public static void setUserInfo(Context context, ProfileResponseModel data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        getEditor(context).putString(USER_INFO, json).commit();
    }

    public static ProfileResponseModel getUserInfo(Context context) {
        Gson gson = new Gson();
        String json = getString(context, USER_INFO, "");
        return gson.fromJson(json, ProfileResponseModel.class);
    }

    public static String getUserString(Context context) {
        return getString(context, USER_INFO, "");
    }

    public static void deleteUserInfo(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(USER_INFO);
        editor.commit();
    }


    public static void deleteToken(Context context) {
        setAccessToken(context, "");
        SharedPreferences mySPrefs = context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(ACCESS_TOKEN);
        editor.apply();
    }

    //Address

    public static void setAddress(Context context, Address data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        getEditor(context).putString(ADDRESS, json).commit();
    }

    public static Address getAddress(Context context) {
        Gson gson = new Gson();
        String json = getString(context, ADDRESS, "");
        return gson.fromJson(json, Address.class);
    }


}
