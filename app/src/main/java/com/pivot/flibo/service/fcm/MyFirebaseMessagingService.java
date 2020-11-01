package com.pivot.flibo.service.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pivot.flibo.R;
import com.pivot.flibo.data.network.model.notification.Action;
import com.pivot.flibo.ui.splash.SplashActivity;
import com.pivot.flibo.utils.AppConstants;
import com.pivot.flibo.utils.AppLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.pivot.flibo.utils.AppConstants.BUTTON_INDEX;
import static com.pivot.flibo.utils.AppConstants.CHANNEL_ID;
import static com.pivot.flibo.utils.AppConstants.CHANNEL_NAME;
import static com.pivot.flibo.utils.AppConstants.NOTIFICATION_ID;
import static com.pivot.flibo.utils.AppConstants.TAPPED_ON;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private int mNotifId;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppLogger.e("newToken", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String content = data.get("content");
        int notifId = Integer.valueOf(data.get("notification_id"));
        mNotifId = notifId;
        String title = data.get("title");
        String image = data.get("image");
        String url = data.get("url");
        Action urlAction = new Action();
        urlAction.setAction("body");
        urlAction.setButtonText("body");
        urlAction.setLink(url);

        Gson gson = new Gson();
        Type actionListType = new TypeToken<ArrayList<Action>>(){}.getType();
        ArrayList<Action> actions = gson.fromJson(data.get("actions"), actionListType);

        PendingIntent pi = getPendingIntentForUrl(urlAction, -1);
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext(), CHANNEL_ID)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_logo_dark)
                        .setContentIntent(pi)
                        .setContentText(content)
                        .setContentTitle(title);

        builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAction));

        int index = 0;
        for (Action action:
             actions) {
            NotificationCompat.Action notificationAction =
                    new NotificationCompat.Action.Builder(
                            0, action.getButtonText(), getPendingIntentForUrl(action, index)
                    ).build();
            builder.addAction(notificationAction);
            index += 1;
        }

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        try {
            NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
            bigPicStyle.bigPicture(Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .submit()
                    .get());
            builder.setStyle(bigPicStyle);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nm.notify(notifId, builder.build());
    }

    private PendingIntent getPendingIntentForUrl(Action action, int index){
        Intent intent = null;
        if(action.getAction().contains("browser")){
            intent = SplashActivity.getStartIntent(getApplicationContext(), action.getLink());
        } else {
            intent = SplashActivity.getStartIntent(getApplicationContext());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Uri uri = Uri.parse(action.getLink());
            intent.setData(uri);
        }
        intent.putExtra(NOTIFICATION_ID, mNotifId);
        if(action.getAction().contains("body")){
            intent.putExtra(TAPPED_ON, "body");
        } else {
            intent.putExtra(TAPPED_ON, "button");
        }
        intent.putExtra(BUTTON_INDEX, index);
        return PendingIntent.getActivity(getApplicationContext(), index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
