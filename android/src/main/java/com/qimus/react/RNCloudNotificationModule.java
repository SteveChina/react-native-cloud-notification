
package com.qimus.react;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.google.firebase.iid.FirebaseInstanceId;

public class RNCloudNotificationModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNCloudNotificationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
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
}