package com.qimus.react;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        // We need to run this on the main thread, as the React code assumes that is true.
        // Namely, DevServerHelper constructs a Handler() without a Looper, which triggers:
        // "Can't create handler inside thread that has not called Looper.prepare()"
       /* Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                // Construct and load our normal React JS code bundle
                ReactInstanceManager mReactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
                ReactContext context = mReactInstanceManager.getCurrentReactContext();
                // If it's constructed, send a notificationv
                if (context != null) {
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(message);
                } else {
                    // Otherwise wait for construction, then send the notification
                    mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                        public void onReactContextInitialized(ReactContext context) {
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(message);
                        }
                    });
                    if (!mReactInstanceManager.hasStartedCreatingInitialContext()) {
                        // Construct it in the background
                        mReactInstanceManager.createReactContextInBackground();
                    }
                }
            }
        });*/

       /*
       Intent notificationService = new Intent(this, NotificationService.class);
       notificationService.putExtra("userName", "Denis");
       this.startService(notificationService);
*/
//        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//
//        Log.d(TAG, "messageService hello");
//
//
//
//        Context context = getApplicationContext();
//        PackageManager pm = context.getPackageManager();
//        Intent launchIntent = pm.getLaunchIntentForPackage("com.mtsiot");
//
//        /*if (launchIntent != null) {
//            Log.d(TAG, "Launch intent: " + launchIntent.getPackage());
//            context.startActivity(launchIntent);
//            return;
//        }*/
//
//        List<ResolveInfo> activities = pm.queryIntentActivities(launchIntent, 0);
//
//        Log.d(TAG, "Activities list:");
//        for (ResolveInfo item : activities) {
//            Log.d(TAG, "Activity name: " + item.activityInfo.name);
//
//        }
//
///*
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        ComponentName componentName = new ComponentName(getApplicationContext().getPackageName(), ".MainActivity");
//        i.addCategory(Intent.CATEGORY_BROWSABLE);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.setComponent(componentName);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        startActivity(i);
//        return;
//*/
//        if (true) {
//            try {
//
//                String ns = getApplicationContext().getPackageName();
//                String cls = ns + ".MainActivity";
//                Intent intent = new Intent(getApplicationContext(), Class.forName(cls));
//
//               /* Intent i = getApplicationContext().getPackageManager().getLaunchIntentForPackage(cls);
//                getApplicationContext().startActivity(i);
//                startActivity(i);*/
//
//                intent.setAction(Intent.ACTION_MAIN);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                intent.putExtra("foreground", true);
//
//                startActivity(intent);
//            } catch (Exception e) {
//                Log.w(TAG, "Failed to open application on received call", e);
//            }
//        }

       // Toast.makeText(getApplication().getApplicationContext(), "some text", 10).show();
    }

    private void sendNotification(String title, String body) {
        try {
            PowerManager.WakeLock wakelock = ((PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "");
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
