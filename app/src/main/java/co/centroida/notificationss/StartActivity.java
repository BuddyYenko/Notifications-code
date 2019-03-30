package co.centroida.notificationss;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import co.centroida.notificationss.net.BundleServer;
import co.centroida.notificationss.net.ServerConnect;

public class StartActivity extends AppCompatActivity {

    //private static final String SUBSCRIPTION_STAFF = "staff";
    //private static final String SUBSCRIPTION_PUBLIC = "public";

    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1",NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2",NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);

        }

        //String t = FirebaseMessaging.getInstance().
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("StartActivity", "Refreshed token: -----------------------------------------" + refreshedToken);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    private void send() {

        String url = "https://www.projecttools.co.za/bcmapp-android-api/sendJustNoti.php";

        //BundleServer bundleServer = new BundleServer(new String[]{}, new String[]{});

        ServerConnect.connectPost(getApplicationContext(), url, BundleServer.getInstance(), 1, new ServerConnect.OnMultiReturnedResultsListener() {
            @Override
            public void onResultsReturned(@Nullable String results, int queAt) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }
}
