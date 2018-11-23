package com.qimus.react;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        ReactHelper.getInstance().sendEvent(RNCloudNotificationModule.EVENT_FCM_UPDATE, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        notifyAboutNewMessage(remoteMessage);

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

    private void notifyAboutNewMessage(RemoteMessage remoteMessage) {
        WritableMap notificationData = Arguments.createMap();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            notificationData.putString("title", notification.getTitle());
            notificationData.putString("body", notification.getBody());
        }

        WritableMap dataMap = Arguments.createMap();

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            dataMap.putString(entry.getKey(), entry.getValue());
        }

        WritableMap params = Arguments.createMap();
        params.putMap("data", dataMap);
        params.putMap("notification", notificationData);

        ReactHelper.getInstance().sendEvent("FCMIncomingMessage", params);
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
        RNNotificationManager.getInstance().setContext(getApplicationContext());
        Log.d(TAG, "sendDataNotification, title: " + title);
        NotificationDto dto = new NotificationDto();
        dto.setBody(body);
        dto.setTitle(title);
        RNNotificationManager.getInstance().sendNotification(dto);
    }
}
