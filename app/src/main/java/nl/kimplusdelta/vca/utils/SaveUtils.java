package nl.kimplusdelta.vca.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

public class SaveUtils {

    public static final String SETTINGS = "settings";

    public static SharedPreferences getPref(Context context, String category, String... additional) {
        if(additional.length == 0) {
            return context.getSharedPreferences(category, Context.MODE_PRIVATE);
        } else {
            return context.getSharedPreferences(category + "-" + Arrays.toString(additional), Context.MODE_PRIVATE);
        }
    }

    @SuppressWarnings("unused")
    public static void clearPref(Context context, String category, String... additional) {
        SharedPreferences sharedPref = getPref(context, category, additional);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.clear();
        edit.apply();
    }
}
