package com.qimus.react;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends android.content.BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    private Intent createIntent(Context context) throws Exception {
        String ns = context.getPackageName();
        String cls = ns + ".MainActivity";
        try {
            return new Intent(context, Class.forName(cls));
        } catch (Exception e) {
            Log.d(TAG, "Error on create MainActivity intent");
            throw new Exception("Package not found");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Intent appIntent = createIntent(context);

            String targetRoute = intent.getExtras().getString("targetRoute");
            if (targetRoute != null) {
                appIntent.putExtra("targetRoute", targetRoute);
                if (RNCloudNotificationModule.isForeground == true) {
                    ReactHelper.getInstance().sendEvent(RNCloudNotificationModule.EVENT_CHANGE_ROUTE, targetRoute);
                }
            }
            context.startActivity(appIntent);
        } catch (Exception e) {
            Log.d(TAG, "Error on create MainActivity intent", e);
        }
    }
}
