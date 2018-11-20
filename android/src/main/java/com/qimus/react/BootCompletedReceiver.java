package com.qimus.react;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            String ns = context.getPackageName();
            String cls = ns + ".MainActivity";
            try {
                Intent i = new Intent(context, Class.forName(cls));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                Log.d(TAG, "Error on create application intent");
            }
        }
    }
}
