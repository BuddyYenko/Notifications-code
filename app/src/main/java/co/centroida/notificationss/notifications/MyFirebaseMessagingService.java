package co.centroida.notificationss.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import co.centroida.notificationss.R;
import co.centroida.notificationss.StartActivity;

/**
 * Created by L on 09/05/2017.
 * Copyright (c) 2017 Centroida. All rights reserved.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        NotificationHelper notificationHelper = new NotificationHelper(this);

        Map<String, String> data = remoteMessage.getData();

        String title = data.get("title");
        String message = data.get("message");

        int notificationId = new Random().nextInt(60000);

        Intent resultIntent = new Intent(this, StartActivity.class);

        notificationHelper.createNotification(notificationId, title, message, resultIntent);
    }

    /*@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent notificationIntent = new Intent(this, StartActivity.class);
        if(StartActivity.isAppRunning){
            //Some action
        }else{
            //Show notification as usual
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        //You should use an actual ID instead
        int notificationId = new Random().nextInt(60000);


        Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url"));

        Intent likeIntent = new Intent(this,LikeService.class);
        likeIntent.putExtra(NOTIFICATION_ID_EXTRA,notificationId);
        likeIntent.putExtra(IMAGE_URL_EXTRA,remoteMessage.getData().get("image-url"));
        PendingIntent likePendingIntent = PendingIntent.getService(this,
                notificationId+1,likeIntent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .setSummaryText(remoteMessage.getData().get("message"))
                            .bigPicture(bitmap))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .addAction(R.drawable.ic_favorite_true,
                            getString(R.string.notification_add_to_cart_button),likePendingIntent)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(notificationId, notificationBuilder.build());

    }*/

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    public class NotificationHelper {

        private Context mContext;
        private NotificationManager mNotificationManager;
        private NotificationCompat.Builder mBuilder;
        public static final String NOTIFICATION_CHANNEL_ID = "10001";

        public NotificationHelper(Context context) {
            mContext = context;
        }

        public void createNotification(int id, String title, String message, Intent resultIntent) {

            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder = new NotificationCompat.Builder(mContext, "goLiftAppNoti");
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    //        .setGroup("S")
                    //        .setGroupSummary(true)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);

            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_GOLIFTAPP", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotificationManager != null;
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                //    mBuilder.setGroup(group);
                //    mBuilder.setGroupSummary(true);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(id, mBuilder.build());
        }
    }
}