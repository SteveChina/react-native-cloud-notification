package com.qimus.react;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends Service {
    private static final String TAG = "PjSipService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), intent.getStringExtra("userId"), 10);
        Log.d(TAG, "some message");
        return super.onStartCommand(intent, flags, startId);
    }

    public void wakeUp() {

        Toast.makeText(getApplicationContext(), "wakeUp", 10);

        try {
            String ns = getApplicationContext().getPackageName();
            String cls = ns + ".MainActivity";

            Intent intent = new Intent(getApplicationContext(), Class.forName(cls));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.putExtra("foreground", true);

            startActivity(intent);
        } catch (Exception e) {
            Log.w(TAG, "Failed to open application on received call", e);
        }
    }
}
