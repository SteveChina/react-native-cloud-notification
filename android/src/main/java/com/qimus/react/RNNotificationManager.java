package com.qimus.react;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class RNNotificationManager {
    private static final String TAG = "RNNotificationManager";
    private static final String NOTIFICATION_CHANNEL = "myNotificationChannel";

    public static final String PRIORITY_URGENT = "urgent";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_DEFAULT = "default";
    public static final String PRIORITY_LOW = "low";

    private Context context;

    public RNNotificationManager(Context context) {
        this.context = context;
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

    public void sendNotification(NotificationDto dto) {
        try {
            Intent intent = new Intent(this.context, NotificationReceiver.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (dto.getTargetRoute() != null) {
                intent.putExtra("targetRoute", dto.getTargetRoute());
            }

            PendingIntent contentIntent = PendingIntent.getBroadcast(
                    this.context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            PackageManager pm = this.context.getPackageManager();
            Resources resources = pm.getResourcesForApplication(context.getPackageName());
            int resId = resources.getIdentifier(dto.getSmallIcon(), "mipmap", context.getPackageName());

            int priorityMessage;
            String channelId;
            switch (dto.getPriority()) {
                case PRIORITY_URGENT:
                    priorityMessage = NotificationCompat.PRIORITY_MAX;
                    channelId = NOTIFICATION_CHANNEL + "_urgent";
                    break;
                case PRIORITY_HIGH:
                    priorityMessage = NotificationCompat.PRIORITY_HIGH;
                    channelId = NOTIFICATION_CHANNEL + "_high";
                    break;
                case PRIORITY_DEFAULT:
                    priorityMessage = NotificationCompat.PRIORITY_DEFAULT;
                    channelId = NOTIFICATION_CHANNEL + "_medium";
                    break;
                case PRIORITY_LOW:
                    priorityMessage = NotificationCompat.PRIORITY_LOW;
                    channelId = NOTIFICATION_CHANNEL + "_low";
                    break;
                default:
                    priorityMessage = NotificationCompat.PRIORITY_DEFAULT;
                    channelId = NOTIFICATION_CHANNEL + "_medium";
            }


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context, channelId)
                    .setSmallIcon(resId)
                    .setContentTitle(dto.getTitle())
                    .setContentText(dto.getBody())
                    .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setPriority(priorityMessage)
                    .setBadgeIconType(resId)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent);

            if (!dto.getLargeIcon().equals("")) {
                int largeIconResId = resources.getIdentifier(dto.getLargeIcon(), "mipmap", context.getPackageName());
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, largeIconResId));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "app_notifications";
                int importance;

                switch (dto.getPriority()) {
                    case PRIORITY_URGENT:
                        channelName = channelName + "_urgent";
                        importance = NotificationManager.IMPORTANCE_HIGH;
                        break;
                    case PRIORITY_HIGH:
                        channelName = channelName + "_high";
                        importance = NotificationManager.IMPORTANCE_DEFAULT;
                        break;
                    case PRIORITY_DEFAULT:
                        channelName = channelName + "_medium";
                        importance = NotificationManager.IMPORTANCE_LOW;
                        break;
                    case PRIORITY_LOW:
                        channelName = channelName + "_low";
                        importance = NotificationManager.IMPORTANCE_MIN;
                        break;
                    default:
                        channelName = channelName + "_medium";
                        importance = NotificationManager.IMPORTANCE_LOW;
                }

                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance
                );
                mChannel.setDescription("Smart Intercom notifications");
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.WHITE);
                mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                mChannel.setShowBadge(true);
                getNotificationManager().createNotificationChannel(mChannel);
            }

            getNotificationManager().notify(
                    dto.getChannelId(), notificationBuilder.build()
            );

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
