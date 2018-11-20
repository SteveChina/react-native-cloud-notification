package com.qimus.react;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "FirebaseDataReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        if (isScreenOn == false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }

        try {
            //Redirect particular screen after receiving notification, this is like ola driver app concept accepting driver request
            Intent service = new Intent(context, Class.forName("com.mtsiot.MainActivity"));
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(service);
            //startWakefulService(context, service);
        } catch (Exception ex) {
            Log.d(TAG, "Failed to open application", ex);
        }
    }
}
