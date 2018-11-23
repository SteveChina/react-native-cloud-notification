
package com.qimus.react;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.firebase.iid.FirebaseInstanceId;

public class RNCloudNotificationModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNCloudNotification";

    private final ReactApplicationContext reactContext;

    public static final String EVENT_FCM_UPDATE  = "FCMTokenUpdate";

    public RNCloudNotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        ReactHelper.getInstance().setReactContext(reactContext);
        RNNotificationManager
                .getInstance()
                .setActivity(getCurrentActivity());

        Log.d(TAG, "token: " + FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public String getName() {
        return "RNCloudNotification";
    }

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @ReactMethod
    public String getFCMToken() {
        ReactHelper.getInstance().sendEvent(EVENT_FCM_UPDATE, FirebaseInstanceId.getInstance().getToken());
        return FirebaseInstanceId.getInstance().getToken();
    }

    @ReactMethod
    public void unlockScreen() {
        PowerManager pm = (PowerManager) getReactApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isInteractive();
        } else {
            isScreenOn = pm.isScreenOn();
        }

        if (isScreenOn == false) {
            PowerManager.WakeLock wl1 = pm.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE, "wl1");
            wl1.acquire();
            wl1.release();
        }
    }

    @ReactMethod
    public void sendNotification(ReadableMap data) {
        Log.d(TAG, "send notification");
        NotificationDto dto = new NotificationDto();
        dto.setTitle(data.getString("title"));
        dto.setBody(data.getString("body"));

        if (data.hasKey("smallIcon")) {
            dto.setSmallIcon(data.getString("smallIcon"));
        }

        if (data.hasKey("channelId")) {
            dto.setChannelId(data.getInt("channelId"));
        }

        if (data.hasKey("largeIcon")) {
            dto.setLargeIcon(data.getString("largeIcon"));
        }

        if (data.hasKey("priority")) {
            dto.setPriority(data.getString("priority"));
        }

        RNNotificationManager.getInstance().setContext(getReactApplicationContext());
        RNNotificationManager.getInstance().sendNotification(dto);
    }
}
