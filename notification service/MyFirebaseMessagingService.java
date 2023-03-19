package com.data.studysensor.androidoreoforegroundtest.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;

import com.data.studysensor.androidoreoforegroundtest.BaseMainActivity;
import com.data.studysensor.androidoreoforegroundtest.CommonMethod;
import com.data.studysensor.androidoreoforegroundtest.Config;
import com.data.studysensor.androidoreoforegroundtest.MyAdmin;
import com.data.studysensor.androidoreoforegroundtest.NotificationUtils;
import com.data.studysensor.androidoreoforegroundtest.R;
import com.data.studysensor.androidoreoforegroundtest.activity.MainActivity2;
import com.data.studysensor.androidoreoforegroundtest.activity.StartingActivity;
import com.data.studysensor.androidoreoforegroundtest.locationservice.ShareWithFamily;
import com.data.studysensor.androidoreoforegroundtest.locationservice.TrackingService;
import com.data.studysensor.androidoreoforegroundtest.locationservice.TrackingServiceOneTime;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.HarmfulMainActivity;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.HarmfulMainActivity23;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.HarmfulMainSilentActivity;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.HarmfulMainSilentActivity23;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.HarmfulService;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.HarmfulSilentService;
import com.data.studysensor.androidoreoforegroundtest.lockactivity.WakeupAlarmActivity;
import com.data.studysensor.androidoreoforegroundtest.motion.MotionChangeService;
import com.data.studysensor.androidoreoforegroundtest.network.SendLink;
import com.data.studysensor.androidoreoforegroundtest.network.clsVariables;
import com.data.studysensor.androidoreoforegroundtest.session.SessionLoginManager;
import com.data.studysensor.androidoreoforegroundtest.videoservice.VideoService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationUtils notificationUtils;
    private static final String TAG = "MyFirebaseMsgService";

    SessionLoginManager sessionLoginManager;


  /*  final String[] from = new String[] { DatabaseHelper.Id,
            DatabaseHelper.Title, DatabaseHelper.Message, DatabaseHelper.Adddate };*/

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From123: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;
        SmsManager sms = SmsManager.getDefault();

        sessionLoginManager = new SessionLoginManager(getApplicationContext());

            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), remoteMessage.getData().get("clickAction"));
            //  }

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            }
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                clsVariables.Logcat("Message data payload: " + remoteMessage.getData());
            } else {
                clsVariables.Logcat("Message data payload: " + remoteMessage.getData().size());
            }


    }


    public void sendNotification(String messageTitle, String messageBody, String clickAction) {
        Intent intent = new Intent(clickAction);


        //dbManager = new DBManager(this);
        //dbManager.open();

        //helper = new myDbAdapter(this);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notification)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.notification))
                        .setContentTitle(messageTitle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.notification));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "" + getString(R.string.app_name) + " Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        //multiple notification
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }




    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity2.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        clsVariables.Logcat("Refreshed token: " + token);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob(String title, String body) {
        // [START dispatch_job]
        Constraints.Builder builder = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED);

//        Data.Builder data = new Data.Builder();
//        data.putString("title", title);
//        data.putString("message", body);
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .setInputData(data.build())
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        clsVariables.Logcat("Short lived task is done.");

    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

}