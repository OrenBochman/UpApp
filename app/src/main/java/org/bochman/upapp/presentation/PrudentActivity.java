package org.bochman.upapp.presentation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.bochman.upapp.presentation.favourites.FavouritesActivity;
import org.bochman.upapp.utils.Debug;

import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

/**
 * this activity provides shared behaviours for requesting permissions for ...
 * <p>
 * implements permission requests
 * <p>
 * use in {@link org.bochman.upapp.presentation.SmartActivity},
 *
 */
public abstract class PrudentActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("PrudentActivity");
        Timber.d("Activity Created");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    protected void toast(String msg) {
        Log.v(Debug.getTag(), msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
