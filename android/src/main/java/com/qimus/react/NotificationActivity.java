package com.qimus.react;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notofication);

        Log.d(TAG, "hello from notification activity");
    }
}
