package com.qimus.react;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class ReactHelper {
    private static ReactHelper instance;
    private ReactContext context;

    public static ReactHelper getInstance() {
        if (instance == null) {
            instance = new ReactHelper();
        }

        return instance;
    }

    public void setReactContext(ReactContext context) {
        this.context = context;
    }

    public void sendEvent(String eventName, Object params) {
        if (this.context != null) {
            this.context
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }
}
