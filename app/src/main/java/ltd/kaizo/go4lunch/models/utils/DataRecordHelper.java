package ltd.kaizo.go4lunch.models.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.User;

/**
 * Class to record and read data from the SharedPreferences
 */
public class DataRecordHelper {
    /**
     * The constant RESTAURANT_LIST_KEY.
     */
    public static final String RESTAURANT_LIST_KEY = "RESTAURANT_LIST_KEY";
    /**
     * The constant CURRENT_LATITUDE_KEY.
     */
    public static final String CURRENT_LATITUDE_KEY = "CURRENT_LATITUDE_KEY";
    /**
     * The constant CURRENT_LONGITUDE_KEY.
     */
    public static final String CURRENT_LONGITUDE_KEY = "CURRENT_LONGITUDE_KEY";
    /**
     * The constant ALL_USER_LIST.
     */
    public static final String ALL_USER_LIST = "ALL_USER_LIST";

    /**
     * The constant sharedPreferences.
     */
    private static SharedPreferences sharedPreferences;

    /**
     * Instantiates a new Data record manager.
     */
    private DataRecordHelper() {
    }

    /**
     * Init.
     *
     * @param context the context
     */
    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    /**
     * Read string.
     *
     * @param key      the key
     * @param defValue the def value
     * @return the string
     */
    public static String read(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * Write String value
     *
     * @param key   the key
     * @param value the value
     */
    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    /**
     * Read boolean.
     *
     * @param key      the key
     * @param defValue the def value
     * @return the boolean
     */
    public static boolean read(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Write boolean value
     *
     * @param key   the key
     * @param value the value
     */
    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    /**
     * Read integer.
     *
     * @param key      the key
     * @param defValue the def value
     * @return the integer
     */
    public static Integer read(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * Write int value
     *
     * @param key   the key
     * @param value the value
     */
    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value).apply();
    }

    /**
     * Read double.
     *
     * @param key      the key
     * @param defValue the def value
     * @return the double
     */
    public static Double read(String key, double defValue) {
        return Double.longBitsToDouble(sharedPreferences.getLong(key, (long) defValue));
    }

    /**
     * Write double value in sharedPreferences
     * by converting it to LongBits
     *
     * @param key   the key
     * @param value the value
     */
    public static void write(String key, double value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putLong(key, Double.doubleToLongBits(value)).apply();
    }


    /**
     * Gets list of result from sharedPreferences.
     *
     * @param KEY the key
     * @return the searchQuery object from sharedPreferences
     */
    public static ArrayList<PlaceFormater> getRestaurantListFromSharedPreferences(String KEY) {
        Gson gson = new Gson();
        String gsonStr = "";
        Type type = new TypeToken<ArrayList<PlaceFormater>>() {
        }.getType();
        return gson.fromJson(read(KEY, gsonStr), type);
    }

    /**
     * Gets user list from shared preferences.
     *
     * @param KEY the key
     * @return the user list from shared preferences
     */
    public static ArrayList<User> getUserListFromSharedPreferences(String KEY) {
        Gson gson = new Gson();
        String gsonStr = "";
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        return gson.fromJson(read(KEY, gsonStr), type);
    }


}


