package org.bochman.upapp.presentation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import org.bochman.upapp.presentation.favourites.FavouritesActivity;
import org.bochman.upapp.utils.Debug;


/**
 * this activity provides shared behaviours for
 * <p>
 * implements
 *  * wifi and power connection monitoring. using BroadcastReceivers.
 *  * location
 *  * permission requests
 * <p>
 * use in {@link org.bochman.upapp.presentation.masterDetail.POIMasterActivity},
 * {@link org.bochman.upapp.presentation.masterDetail.POIDetailActivity} and
 * {@link FavouritesActivity}
 *
 * TODO: supplant the toast() function with an SAM interface (Single Action Method)
 *       implement it via a delegate Toast/SanckBar/tester and inject via Dagger2.
 *
 */
public abstract class SmartActivity extends PrudentActivity {

    /**
     * status watchers.
     */
    protected ConnectivityWatcher connectivityWatcher = new ConnectivityWatcher();
    protected PowerWatcher powerWatcher = new PowerWatcher();

    @Override
    protected void onStart() {
        super.onStart();

        //Feature start charging monitoring service
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        iFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        iFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(powerWatcher, iFilter);

        //Feature start wifi monitoring service - will not work for api >= 28 pie
        registerReceiver(connectivityWatcher, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityWatcher);            //Feature: suspend wifi monitoring service
        unregisterReceiver(powerWatcher);                   //Feature: suspend wifi monitoring service
    }

    /**
     * A BroadcastReceiver that watches for charging changes & shows toasts
     */
    class PowerWatcher extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //here, check that the network connection is available. If yes, start your service. If not, stop your service.
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            String msg;
            @SuppressLint("MissingPermission") NetworkInfo info = cm.getActiveNetworkInfo();
            final String action = intent.getAction();
            if(action==null)return;
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    msg = "power connected";
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    msg = "power disconnected";
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent battery = context.registerReceiver(null, batIntentFilter);
                    assert battery != null;
                    int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                            || status == BatteryManager.BATTERY_STATUS_FULL;
                    if (isCharging)
                        msg = "is charging";
                    else
                        msg = "isn't charging";
                    break;
                case Intent.ACTION_BATTERY_LOW:
                    msg = "battery low";
                    break;
                case Intent.ACTION_BATTERY_OKAY:
                    msg = "battery okay";
                    break;
                default:
                    msg = "";
            }
            toast(msg);
        }
    }

    /**
     * A BroadcastReceiver that watches for network changes & shows toasts
     *  TODO: set state in the ViewModel so that when offline search wont trigger the network
     *        request in the IntentService but rather, run a query on existing repo & recalculate
     *        distances.
     *        Also switch number of items and size of images loaded.
     */
    class ConnectivityWatcher extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission")
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {
                toast("wifi online");
            }else if(info != null && info.getType() == ConnectivityManager.TYPE_MOBILE && info.isConnected()) {
                toast("roaming online");
            } else {
                toast("networking offline");
            }
        }
    }

}
