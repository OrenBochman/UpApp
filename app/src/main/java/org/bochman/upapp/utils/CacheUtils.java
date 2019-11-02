package org.bochman.upapp.utils;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class CacheUtils {

    public static String TAG = CacheUtils.class.getName();

    public static void initializeCache(Context context){
        //create HttpRequestCache
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
            Log.i(TAG, "HTTP response cache installation success");

        } catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:" + e);
        }
    }

    // method to flush cache contents to the filesystem
    public static void flushCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

}