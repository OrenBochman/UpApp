package org.bochman.upapp.wifi;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * A Broadcastreciever that watches for network changes & shows toasts
 */
public class ConnectivityWatcher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {

                //notify wifi is on
                Toast.makeText(context, "wifi on", Toast.LENGTH_LONG).show();
                //start service
//              Intent intent = new Intent(context, MyService.class);
//               context.startService(intent);
        } else {
                //notify wifi is off
                Toast.makeText(context, "wifi off", Toast.LENGTH_LONG).show();
                //stop service
//              intent intent = new Intent(context, MyService.class);
//              context.stopService(intent);
        }

    }
}

