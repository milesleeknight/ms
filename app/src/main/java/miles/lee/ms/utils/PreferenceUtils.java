package miles.lee.ms.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import miles.lee.ms.App;

/**
 * @author
 * @classname: PreferenceUtils
 * @Description: TODO(配置文件工具类)
 * @date 2016/7/19 15:22
 */
public final class PreferenceUtils{

    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getInstance().getApplicationContext());
    }

    /**
     * 清空数据
     */
    public static void reset() {
        Editor edit = getDefaultSharedPreferences().edit();
        edit.clear();
        edit.apply();
    }

    public static String getString(String key, String defValue) {
        return getDefaultSharedPreferences().getString(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getDefaultSharedPreferences().getLong(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return getDefaultSharedPreferences().getFloat(key, defValue);
    }

    public static void put(String key, String value) {
        putString(key, value);
    }

    public static void put(String key, int value) {
        putInt(key, value);
    }

    public static void put(String key, float value) {
        putFloat(key, value);
    }

    public static void put(String key, boolean value) {
        putBoolean(key, value);
    }

    public static void putFloat(String key, float value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static SharedPreferences getPreferences() {
        return getDefaultSharedPreferences();
    }

    public static int getInt(String key, int defValue) {
        return getDefaultSharedPreferences().getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getDefaultSharedPreferences().getBoolean(key, defValue);
    }

    public static boolean hasString(String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        return sharedPreferences.contains(key);
    }

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putLong(String key, long value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void remove(String... keys) {
        if (keys != null) {
            SharedPreferences sharedPreferences = getDefaultSharedPreferences();
            Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }
}
