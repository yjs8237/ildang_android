package com.jscompany.ildang.fcmPush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.ildanghistory.IldangHistory;
import com.jscompany.ildang.ildangregister.RegisterIldangType;

import java.util.Map;

public class FcmPushService  extends FirebaseMessagingService {

    private SharedPreferences pref;
    private NotificationManager manager;
    private NotificationCompat.Builder builder = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Map<String, String> pushDataMap = remoteMessage.getData();

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isPushAble = pref.getBoolean("ring_push" , false);

        if(isPushAble) {
            Log.d("pref" , "ring_push");
        }


        if(isPushAble) {
            sendNotificationV4(pushDataMap);
        }
    }


    public void sendNotificationV4(Map<String, String> dataMap) {

        String title = dataMap.get("title");
        String message = dataMap.get("message");

        Intent intent = new Intent(FcmPushService.this, IldangHistory.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("badge_count" , 1);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("push" , "Oreo~~~~");

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannelGroup group1 = new NotificationChannelGroup("channel_group_id", "channel_group_name");
            notificationManager.createNotificationChannelGroup(group1);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.setGroup("channel_group_id");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
            mBuilder = new NotificationCompat.Builder(FcmPushService.this , "channel_id")
                    //.setSmallIcon(R.drawable.app_icon)
                    .setSmallIcon(R.drawable.app_icon_sample)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(mPendingIntent);
        } else {
            mBuilder = new NotificationCompat.Builder(FcmPushService.this)
                    //.setSmallIcon(R.mipmap.ic_launcher)
                    .setSmallIcon(R.mipmap.ic_launcher_sample)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(mPendingIntent);
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }



    public void sendNotificationV3(Map<String, String> dataMap) {
        String title = dataMap.get("title");
        String message = dataMap.get("message");
        Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

        PendingIntent mPendingIntent = PendingIntent.getActivity(this,0, new Intent(getApplicationContext(), FcmPushService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setDefaults(Notification.DEFAULT_SOUND)
        .setLargeIcon(mLargeIconForNoti)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(mPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());


    }



    public void sendNotificationV2(Map<String, String> dataMap) {

        String title = dataMap.get("title");
        String message = dataMap.get("message");

        Log.d("push" , "sendNotification_1");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Log.d("push" , "sendNotification_2");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("push" , "sendNotification_3");

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder nBuilder;



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nBuilder = new NotificationCompat.Builder(this, "push_01")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent);

            Log.d("push" , "sendNotification_4");

        } else {
            nBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{1000, 1000})
                    .setLights(Color.WHITE,1500,1500)
                    .setContentIntent(contentIntent);

            Log.d("push" , "sendNotification_5");
        }

        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0 /* ID of notification */, nBuilder.build());

        Log.d("push" , "sendNotification_6");

    }


    public void sendNotification(Map<String, String> dataMap) {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String title = dataMap.get("title");
        String message = dataMap.get("message");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "one-channel";
            String channelName = "My Channel";
            String channelDescription = "Description";

            NotificationChannel channel = new NotificationChannel(channelId , channelName , NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);

            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this,channelId);

        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Log.d("push" , "sendNotification");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        manager.notify(222, builder.build());



//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.app_icon: R.mipmap.app_icon_new;
//
//        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.app_icon_new)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000})
//                .setLights(Color.WHITE,1500,1500)
//                .setContentIntent(contentIntent);
//
//        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nManager.notify(0 /* ID of notification */, nBuilder.build());

    }


}
