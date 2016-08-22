package templates.yogeshbalan.com.navigationdrawertemplate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;

/**
 * Created by kundan on 10/22/2015.
 */
public class PushNotificationService extends GcmListenerService {
    public static String message;
    public static final int MESSAGE_NOTIFICATION_ID = 1;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        message = data.getString("message");
        createNotification("Manda Teachers Portal", message);
    }
    // Creates notification based on title and body received

    private void createNotification(String title, String body) {
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        Intent intent = new Intent(this,LoginPage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notificationincon).setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(m, mBuilder.build());
    }
}
