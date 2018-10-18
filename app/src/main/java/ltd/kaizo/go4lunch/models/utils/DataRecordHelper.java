package ltd.kaizo.go4lunch.models.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import ltd.kaizo.go4lunch.models.API.PlaceApiData;
import ltd.kaizo.go4lunch.models.PlaceApiDataConverter;

/**
 * Class to record and read data from the SharedPreferences
 */
public class DataRecordHelper {
    public static final String RESTAURANT_LIST_KEY = "RESTAURANT_LIST_KEY";
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
         * Gets list of result from sharedPreferences.
         *
         * @param KEY the key
         * @return the searchQuery object from sharedPreferences
         */
        public static PlaceApiDataConverter getRestaurantListFromSharedPreferences(String KEY) {
            Gson gson = new Gson();
            String gsonStr="";
            return gson.fromJson(read(KEY, gsonStr),PlaceApiDataConverter.class);
        }


}


