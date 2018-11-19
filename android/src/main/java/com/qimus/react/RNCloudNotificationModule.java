
package com.qimus.react;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

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
    public void isAvailable(final Promise promise) {
        try {
            FingerprintManager manager = getFingerprintManager();
            boolean v = (
                    manager != null
                            && manager.isHardwareDetected()
                            && manager.hasEnrolledFingerprints());
            promise.resolve(v);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    private FingerprintManager getFingerprintManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (FingerprintManager) reactContext.getSystemService(Context.FINGERPRINT_SERVICE);
        }

        return null;
    }
}