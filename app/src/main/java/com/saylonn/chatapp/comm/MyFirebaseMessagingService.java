package com.saylonn.chatapp.comm;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.saylonn.chatapp.MainActivity;
import com.saylonn.chatapp.R;
import com.saylonn.chatapp.chathandler.Chat;

import com.saylonn.chatapp.chathandler.ChatDao;
import com.saylonn.chatapp.chathandler.ChatDatabase;
import com.saylonn.chatapp.chathandler.Message;
import com.saylonn.chatapp.chathandler.MessageDao;


import java.util.Map;
import java.util.Objects;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "CAPP";

    /**
     * wird ausgeführt wenn eine Nachricht vom FirebaseMessagingService eintrifft
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String fromUser = "";
        String title;
        String fromEmail = "";

        super.onMessageReceived(remoteMessage);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> jsonData = remoteMessage.getData();
            Log.d(TAG, "map: " + jsonData);
            Log.d(TAG, "from user: " + jsonData.get("fromUsername"));
            fromUser = jsonData.get("fromUsername");
            fromEmail = jsonData.get("fromEmail");

            ChatDatabase chatDatabase = Room.databaseBuilder(this, ChatDatabase.class, "ChatDatabase")
                    .allowMainThreadQueries().build();

            ChatDao chatDao = chatDatabase.chatDao();

            // Wenn der User noch keinen Tabelleneintrag hat, wird er erst hinzugefügt
            if(!chatDao.isRowIsExist(fromEmail)){
                Chat chat = new Chat(fromUser, fromEmail, jsonData.get("messageText"));
                chatDao.insert(chat);
            }

            // Es wird eine neue Nachricht erstellt und in die Message Tabelle hinzugefügt
            MessageDao messageDao = chatDatabase.messageDao();
            Message message = new Message("remoteUser", fromEmail, jsonData.get("messageText"));
            messageDao.insert(message);

            // Zum benachrichtigen der RecycleViewer
            Intent myIntent = new Intent("FBR-update-chats");
            this.sendBroadcast(myIntent);

        }
        // Wenn sich die App im Hintergrund befindet soll eine Notification angezeigt werden
        if(!foregrounded()) {
            if (remoteMessage.getNotification() != null) {
                int notificationID = sharedPreferences.getInt(String.valueOf(R.string.notification_id), 0);
                notificationID++;
                if(notificationID > 15) {
                    notificationID = 0;
                }
                sharedPreferences.edit().putInt(String.valueOf(R.string.notification_id), notificationID).apply();
                Log.d(TAG, "Message Notificationo Body: " + remoteMessage.getNotification().getBody());

                String notificationBody = remoteMessage.getNotification().getBody();
                if (notificationBody != null) {
                    title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();

                    Log.d(TAG, "send Notification");
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    String channelId = "fcm_default_channel";
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    if(title.equals("New message from")){
                        title = title + " " + fromUser;
                    }else{
                        title = remoteMessage.getNotification().getTitle();
                    }
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(this, channelId)
                                    .setSmallIcon(R.drawable.main_launcher_icon_foreground)
                                    .setContentTitle(title)
                                    .setContentText(notificationBody)
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(pendingIntent);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    NotificationChannel channel = new NotificationChannel(channelId, "Messenger Channel", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.notify(notificationID, notificationBuilder.build());
                }
            }
        }
    }

    /**
     * Wird aufgerufen wenn die App einen neuen fcm_Token erhält. Das passiert eigentlich nur Neuinstallation oder cache Löschung
      * @param token eindeutiger Token der vom FCM Framework übermittelt wird
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed Token: " + token);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(getString(R.string.token_key), token).apply();
    }

    // prüft ob sich die App im Vordergrund befindet oder nicht
    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

}
