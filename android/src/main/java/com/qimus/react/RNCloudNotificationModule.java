
package com.qimus.react;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.firebase.iid.FirebaseInstanceId;

public class RNCloudNotificationModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public static final String EVENT_FCM_UPDATE  = "FCMTokenUpdate";

    public RNCloudNotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        ReactHelper.getInstance().setReactContext(reactContext);
    }

    @Override
    public String getName() {
        return "RNCloudNotification";
    }


    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
        Log.d("RnModule", "Your token: " + FirebaseInstanceId.getInstance().getToken());
    }

    @ReactMethod
    public String getFCMToken() {
        ReactHelper.getInstance().sendEvent(EVENT_FCM_UPDATE, FirebaseInstanceId.getInstance().getToken());
        Log.d("RnModule", "getFCMToken: " + FirebaseInstanceId.getInstance().getToken());
        return FirebaseInstanceId.getInstance().getToken();
    }

    @ReactMethod
    public void unlockScreen() {
        PowerManager pm = (PowerManager) getReactApplicationContext()
                .getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl1 = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wl1");
        wl1.acquire();
        wl1.release();

//        Activity mainActivity = getCurrentActivity();
//
//        if (mainActivity != null) {
//            mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mainActivity != null) {
//            Window window = mainActivity.getWindow();
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }
}
