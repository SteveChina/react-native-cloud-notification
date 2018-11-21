package com.qimus.react;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "New token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "My secret token: " + FirebaseInstanceId.getInstance().getToken());

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification == null) {
            this.tryWakeUp();
        } else {
            sendNotification(notification.getTitle(), notification.getBody());
        }
    }

    private void tryWakeUp() {
        try {

            String ns = getApplicationContext().getPackageName();
            String cls = ns + ".MainActivity";
            Intent intent = new Intent(getApplicationContext(), Class.forName(cls));

            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.putExtra("foreground", true);

            KeyguardManager manager = (KeyguardManager) getApplication().getSystemService(KEYGUARD_SERVICE);
            manager.

            startActivity(intent);
        } catch (Exception e) {
            Log.w(TAG, "Failed to open application on message receive", e);
        }
    }

    private void sendNotification(String title, String body) {
        try {
            PowerManager.WakeLock wakelock = ((PowerManager) getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE))
                    .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "");
            wakelock.acquire();

            Intent intent = new Intent(getApplicationContext(), Class.forName("com.mtsiot.MainActivity")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

           // builder.setSmallIcon(R.drawable.ic_stat_notification);
            builder.setVibrate(new long[]{0, 200});
            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setColor(0xffffa714);
            builder.setSound(alarmSound);
            builder.setContentIntent(contentIntent);
            builder.setAutoCancel(true);
            builder.setChannelId("my_channel_01");

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(builder);
            bigTextStyle.setBigContentTitle(title == null ? getString(R.string.common_google_play_services_install_title) : title);
            bigTextStyle.bigText(body);

            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

//            String id = "my_channel_0";
//            CharSequence name = getString(R.string.common_google_play_services_notification_channel_name);
//
//            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
//            channel.enableLights(true);

            mNotificationManager.notify(1, bigTextStyle.build());
            wakelock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
