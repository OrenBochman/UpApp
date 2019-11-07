package org.bochman.upapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Shared Preferences Utilities.
 *
 * TODO: Inject this as a singleton using dagger and depend on the app for context.
 */
public class SpUtils {

    // keys of shared preferences:

    static final String NAME = "History";               // key for sp file

    static final String LAT = "lat";                    // set in PoiMasterActivity
    static final String LNG = "lng";                    // set in PoiMasterActivity
    static final String QUERY = "query";                // set in PoiMasterActivity
    static final String IS_METRIC = "units";            // set in root_values
    static final String RADIUS = "radius_values";       // set in root_values
    static final String MAP_TYPE = "map_type";          // set in root_values
    static final String PLACE_TYPE = "place_type";      // set in root_values

    // some defaults:

    private static final float LNG_DEFAULT= 34.8149f;
    private static final float LAT_DEFAULT= 32.0668f;
    private static final int RADIUS_DEFAULT= 1000;

    private static final String MAP_TYPE_DEFAULT= "normal";

    public static int getRadius(Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(RADIUS, RADIUS_DEFAULT);
    }

    public static Set<String> getMapType(Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(PLACE_TYPE, new HashSet<String>());
    }

    public static String getPlaceType(Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MAP_TYPE, MAP_TYPE_DEFAULT);
    }
    public static int getIsMetric(Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(IS_METRIC, 1);
    }

    public static void setIsMetric(int isMetric, Context ctx) {
        setInt(isMetric,IS_METRIC,ctx);
    }

    public static String getLastSearch(Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        String query = sharedPreferences.getString("query", "");
        return query;
    }

    public static void setLastSearch(String query, //LatLng location,
                                     Context ctx) {
        setString(query,QUERY,ctx);
    }

    public static double getLat(Context ctx) {
        return ctx.getSharedPreferences(LAT, Context.MODE_PRIVATE).
                getFloat(LAT, LAT_DEFAULT);
    }

    public static void setLat(Double lat, Context ctx) {

        setFloat(lat, LAT, ctx);
    }

    public static void setLng(Double lat, Context ctx) {

        setFloat(lat, LNG, ctx);
    }

    public static double getLng(Context ctx) {

        return ctx.getSharedPreferences(LNG, Context.MODE_PRIVATE).getFloat(LNG, LNG_DEFAULT);
    }

    // Internal setters per DRY ///////////////////////////////////////////////////////////////////

    private static void setInt(int x, String key, Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, x);
        editor.apply();
    }

    private static void setString(String x, String key, Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, x);
        editor.apply();
    }

    private static void setFloat(Double x, String key, Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, x.floatValue());
        editor.apply();
    }

}
