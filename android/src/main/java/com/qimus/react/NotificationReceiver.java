package com.qimus.react;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import java.util.Map;

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
            Bundle extras = intent.getExtras();

            if (extras != null && extras.containsKey("routeName")) {
                appIntent.putExtra("routeName", extras.getString("routeName"));
                if (extras.containsKey("routeParams")) {
                    appIntent.putExtra("routeParams", intent.getSerializableExtra("routeParams"));
                }

                //if app in foregraund send notification immediately
                if (RNCloudNotificationModule.isForeground == true) {
                    WritableMap params = Arguments.createMap();
                    params.putString("routeName", extras.getString("routeName"));
                    if (extras.containsKey("routeParams")) {
                        params.putMap("routeParams", MapUtil.toWritableMap((Map<String, Object>)intent.getSerializableExtra("routeParams")));
                    }
                    ReactHelper.getInstance().sendEvent(RNCloudNotificationModule.EVENT_CHANGE_ROUTE, params);
                }
            }
            context.startActivity(appIntent);
        } catch (Exception e) {
            Log.d(TAG, "Error on create MainActivity intent", e);
        }
    }
}
