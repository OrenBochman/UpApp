package org.bochman.upapp.utils;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

public class SharedPreferencesUtils {

    public static String getLastSearch(Context ctx){
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences("History", Context.MODE_PRIVATE);

        List<Object> pair = new LinkedList<>();
        String query= sharedPreferences.getString("query", "");
        //float latitude = sharedPreferences.getFloat("latitude", 0.0f);
        //float longitude= sharedPreferences.getFloat("longitude", 0.0f);
        //LatLng location = new LatLng(latitude,longitude);
        //pair.add(query);
        //pair.add(location);
        return query;
    }

    public static void setLastSearch(String query, //LatLng location,
                              Context ctx){
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences("History", Context.MODE_PRIVATE);

        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("query", query);
        //editor.putFloat("latitude", (float) location.latitude);
        //editor.putFloat("longitude",(float) location.longitude);
        editor.apply();
    }
}
