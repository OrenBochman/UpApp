package org.bochman.upapp.utils;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

public class SpUtils {
    static final String NAME = "History";

    static final String LAT = "lat";
    static final String LNG = "lng";
    static final String QUERY = "query";
    static final String IS_METRIC = "isMetric";

    static void setFloat(Double x, String key, Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, x.floatValue());
        editor.apply();
    }

    static void setInt(int x, String key, Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, x);
        editor.apply();
    }

    static void setString(String x, String key, Context ctx) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, x);
        editor.apply();
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
        return ctx.getSharedPreferences("lat", Context.MODE_PRIVATE).
                getFloat("lat", 32.0668f);
    }

    public static void setLat(Double lat, Context ctx) {
        setFloat(lat, LAT, ctx);
    }


    public static double getLng(Context ctx) {
        return ctx.getSharedPreferences("lat", Context.MODE_PRIVATE).
                getFloat("lat", 34.8149f);
    }

    public static void setLng(Double lat, Context ctx) {
        setFloat(lat, LNG, ctx);
    }


}
