package com.qimus.react;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        ReactHelper.getInstance().sendEvent(RNCloudNotificationModule.EVENT_FCM_UPDATE, s);
        Log.d(TAG, "New token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "My secret token: " + FirebaseInstanceId.getInstance().getToken());

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification == null) {
            Map<String, String> data = remoteMessage.getData();
            if (data.containsKey("title") && data.containsKey("body")) {
                sendDataNotification(data.get("title"), data.get("body"));
            } else {
                this.tryWakeUp();
            }
        } else {
            sendDataNotification(notification.getTitle(), notification.getBody());
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

            PowerManager pm = (PowerManager) getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            PowerManager.WakeLock wl1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wl1");
            wl1.acquire(10000);

            PowerManager.WakeLock cpuLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wl2");
            cpuLock.acquire(10000);

            startActivity(intent);

            wl1.release();
        } catch (Exception e) {
            Log.w(TAG, "Failed to open application on message receive", e);
        }
    }

    private void sendDataNotification(String title, String body) {
        try {
            Intent intent = new Intent(getApplicationContext(), Class.forName("com.qimus.MainActivity"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Uri defaultsSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    //.setSmallIcon(R.drawable.ic_launcher)
                    //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultsSoundUri);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

        } catch (Exception e) {
            Log.d(TAG, "Error on sendDataNotification");
        }
    }
}
