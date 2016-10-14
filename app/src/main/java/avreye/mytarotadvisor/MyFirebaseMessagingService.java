package avreye.mytarotadvisor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import avreye.mytarotadvisor.ui.ChatActivityforAdvisor;
import avreye.mytarotadvisor.ui.ChatActivityforUser;
import avreye.mytarotadvisor.ui.MainActivity;
import avreye.mytarotadvisor.ui.MessageCenter;

/**
 * Created by Zeeshan on 16/06/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "StartingAndroid";
    AppController appController;
    static  int nid = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //It is optional
        Log.e(TAG, "From:");
     //   Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getData().toString());

        appController = (AppController) getApplicationContext();
        //Calling method to generate notification
        Log.e("notification111",remoteMessage.getData().get("sender_Id"));
        sendNotification(remoteMessage.getData().get("sender_displayname"),remoteMessage.getData().get("alert"),
                remoteMessage.getData().get("sender_Id"),remoteMessage.getData().get("appType"),remoteMessage.getData().get("sender_displayname"));
    }

    //This method is only generating push notification
    private void sendNotification(String title, String messageBody, String sendid, String apptype,String senderdiplayname) {

        if(appController.GetActivity() != null)
        {

           //return;
        }

        Log.e("notification",sendid);
        Intent intent;
        PendingIntent pendingIntent;
        if(apptype.contains("user"))
        {
             intent = new Intent(this, ChatActivityforAdvisor.class);


            Bundle bundle = new Bundle();
            bundle.putString("advisor_id",sendid);
            bundle.putString("advisor_name",senderdiplayname);
            bundle.putString("from_notification","from_notification");
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
             pendingIntent = PendingIntent.getActivity(this, nid++, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        else
        {
            intent = new Intent(this, ChatActivityforUser.class);
            Bundle bundle = new Bundle();
            bundle.putString("advisor_id",sendid);
            bundle.putString("advisor_name",senderdiplayname);
            bundle.putString("from_notification","from_notification");
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            pendingIntent = PendingIntent.getActivity(this, nid++, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(nid, notificationBuilder.build());
    }
}