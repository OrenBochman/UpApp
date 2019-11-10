package org.bochman.upapp.power;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.bochman.upapp.utils.Debug;

/**
 * A Broadcastreciever that watches for charging changes & shows toasts
 */
public class PowerWatcher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info = cm.getActiveNetworkInfo();
        if(intent.getAction() == Intent.ACTION_POWER_CONNECTED) {
            //Handle power connected
            Log.i(Debug.getTag(),"power connected");
            Toast.makeText(context, "Charger PluggedIn", Toast.LENGTH_SHORT).show();
        } else if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED){
            //Handle power disconnected
            Log.i(Debug.getTag(),"power disconnected");
            Toast.makeText(context, "Charger PluggedOut", Toast.LENGTH_SHORT).show();
        } else if(intent.getAction() == Intent.ACTION_BATTERY_CHANGED){
            //Handle power disconnected
            //Log.i(Debug.getTag(),"power disconnected");
            //Toast.makeText(context, "Charger PluggedOut", Toast.LENGTH_SHORT).show();
        }

    }
}

