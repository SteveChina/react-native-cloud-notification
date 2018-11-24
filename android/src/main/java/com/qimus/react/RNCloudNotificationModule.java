
package com.qimus.react;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.rtp.AudioGroup;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;
import java.util.Map;

public class RNCloudNotificationModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "RNCloudNotification";

    private final ReactApplicationContext reactContext;

    public static final String EVENT_FCM_UPDATE  = "FCMTokenUpdate";
    public static final String EVENT_CHANGE_ROUTE = "FCMChangeRoute";

    public static boolean isForeground;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            boolean isClicked = data.getExtras().getBoolean("clicked");
            String yesNo = isClicked == true ? "yes" : "no";
            Log.d("ActivityEventListener", "clicked: " + yesNo);
        }
    };

    public RNCloudNotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        ReactHelper.getInstance().setReactContext(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);
        reactContext.addLifecycleEventListener(this);

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

        if (isScreenOn(pm) == false) {
            PowerManager.WakeLock wl1 = pm.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE, "wl1");
            wl1.acquire();
            wl1.release();
        }
    }

    @ReactMethod
    public boolean isScreenOn(PowerManager pm) {
        boolean isScreenOn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isInteractive();
        } else {
            isScreenOn = pm.isScreenOn();
        }

        return  isScreenOn;
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

        if (data.hasKey("routeName")) {
            dto.setTargetRoute(data.getString("routeName"));
        }

        if (data.hasKey("routeParams")) {
            dto.setRouteParams(MapUtil.toMap(data.getMap("routeParams")));
        }

        RNNotificationManager notificationManager = new RNNotificationManager(getReactApplicationContext());
        notificationManager.sendNotification(dto);
    }

    @Override
    public void onHostResume() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }

        Bundle extra = currentActivity.getIntent().getExtras();
        if (extra != null && extra.containsKey("routeName")) {
            String targetRoute = extra.getString("routeName");
            WritableMap params = Arguments.createMap();
            params.putString("routeName", targetRoute);
            if (extra.containsKey("routeParams")) {
                Serializable sRouteParams = currentActivity.getIntent().getSerializableExtra("routeParams");
                params.putMap("routeParams", MapUtil.toWritableMap((Map<String, Object>) sRouteParams));
            }
            ReactHelper.getInstance().sendEvent(EVENT_CHANGE_ROUTE, params);
        }

        isForeground = true;
    }

    @Override
    public void onHostPause() {
        Log.d(TAG, "onHostPause");
        isForeground = false;
    }

    @Override
    public void onHostDestroy() {
        Log.d(TAG, "onHostDestroy");
        isForeground = false;
    }
}
