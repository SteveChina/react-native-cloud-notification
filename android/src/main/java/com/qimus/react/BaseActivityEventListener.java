package com.qimus.react;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;

public class BaseActivityEventListener implements ActivityEventListener {
    protected Intent intent;

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    public void onNewIntent(Intent intent) {
        this.intent = intent;
    }
}
