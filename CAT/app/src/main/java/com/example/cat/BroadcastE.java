package com.example.cat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class BroadcastE extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        MainActivity ma = new MainActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);

            Bitmap largeIconForNoti =
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.logo3);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "channel_id")
                            .setSmallIcon(R.drawable.logo3)
                            .setTicker("HETT")
                            .setWhen(System.currentTimeMillis())
                            .setContentTitle("CAT")
                            .setContentText("오늘 자외선은 " + ma.getBuffer() + "입니다.")
                            .setDefaults(Notification.DEFAULT_SOUND) // 알림 시 소리
                            .setLargeIcon(largeIconForNoti)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true) // true 사용자가 알림 터치 시 자동으로 사라짐, false 반대
                            .setContentIntent(mPendingIntent);

            notificationManager.notify(1, builder.build());
        }
    }
}
