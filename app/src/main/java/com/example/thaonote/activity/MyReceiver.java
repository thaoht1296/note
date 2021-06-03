package com.example.thaonote.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.thaonote.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "170";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("toDoAction") != null &&
                intent.getStringExtra("toDoAction").equals("toDoNotify")
                && intent.getStringExtra("toDoTitle")!=null
                && intent.getStringExtra("toDoContent")!=null ){
            Log.e("Rev","rev");
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_ID,
                        "Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("This is Channel 1");
                manager.createNotificationChannel(channel1);
            }
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(intent.getStringExtra("toDoTitle"))
                    .setContentText(intent.getStringExtra("toDoContent"))
                    .setColor(Color.RED)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Intent i = new Intent(context, PendingActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            i,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            builder.setContentIntent(pendingIntent);
            manager.notify(12345, builder.build());
        }
    }
}
