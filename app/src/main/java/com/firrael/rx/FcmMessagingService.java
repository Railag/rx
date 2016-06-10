package com.firrael.rx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firrael.rx.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Railag on 09.06.2016.
 */
public class FcmMessagingService extends FirebaseMessagingService {
    private static final String TAG = FcmMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.i(TAG, notification.getBody());

        sendPN(notification);
    }

    private void sendPN(RemoteMessage.Notification notification) {
        Intent resultIntent = new Intent(getBaseContext(), MainActivity.class);


        PendingIntent resultPendingIntent = PendingIntent
                .getActivity(getBaseContext(), 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


        //Intent intent = new Intent(CANCEL_NOTIFICATION_ACTION);
        //PendingIntent deleteIntent = PendingIntent.getBroadcast(context, 0,
        //        intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                //   .setSound(RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(resultPendingIntent)
                .setLights(Color.GREEN, 1000, 4000)
                .setSmallIcon(R.drawable.ic_menu_send);
        //   .setDeleteIntent(deleteIntent);


        int mNotificationId = 1;
        NotificationManager mNotifyMgr = (NotificationManager) getBaseContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification appNotification = builder.build();
        mNotifyMgr.notify(mNotificationId, appNotification);
    }
}