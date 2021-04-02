package com.example.dayary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class DailyNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setContentTitle("Dayary")
                .setContentText("Write in your diary now!")
                .setSmallIcon(R.drawable.ic_edit)
                .setColor(context.getResources().getColor(R.color.black_real))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_splashscreen))
                .setAutoCancel(true);

        Intent write = new Intent(context, LoginActivity.class);
        write.putExtra("write", true);
        write.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingWrite = PendingIntent.getActivity(context, 0, write, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingWrite);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());
    }
}
//