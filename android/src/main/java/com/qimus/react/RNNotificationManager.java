package com.qimus.react;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class RNNotificationManager {
    private static final String TAG = "RNNotificationManager";
    private static final String NOTIFICATION_CHANNEL = "myNotificationChannel2";

    private static RNNotificationManager instance = null;

    private Context context;

    public static RNNotificationManager getInstance() {
        if (instance == null) {
            instance = new RNNotificationManager();
        }

        return instance;
    }

    public RNNotificationManager setContext(Context context) {
        this.context = context;
        return this;
    }

    public NotificationManager getNotificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Intent createIntent() throws Exception {
        String ns = context.getPackageName();
        String cls = ns + ".MainActivity";
        try {
            return new Intent(context, Class.forName(cls));
        } catch (Exception e) {
            Log.d(TAG, "Error on create MainActivity intent");
            throw new Exception("Package not found");
        }
    }

    public void sendNotification(String title, String body) {
        try {
            Intent intent = createIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent contentIntent = PendingIntent.getActivity(
                    this.context,
                    0,
                    intent,
                    0
            );

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context, NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.common_google_signin_btn_icon_dark))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setBadgeIconType(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .setContentIntent(contentIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line..."))
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        NOTIFICATION_CHANNEL, "my notification", NotificationManager.IMPORTANCE_DEFAULT
                );
                mChannel.setDescription("internal notification");
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.WHITE);
                getNotificationManager().createNotificationChannel(mChannel);
            }

            getNotificationManager().notify(0, notificationBuilder.build());
            this.playNotificationSound();

        } catch (Exception e) {
            Log.d(TAG, "Error on sendNotification", e);
        }
    }

    public void playNotificationSound() {
        try {
            Uri defaultsSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this.context, defaultsSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
