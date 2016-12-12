package tw.edu.ntust.stockoraclet.firebasepackage;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import tw.edu.ntust.stockoraclet.R;

/**
 * Created by user on 2016/8/4.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        //設計通知內容
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String title = "Stockforecasting";
        String content = "Verification completed";
        //Email email = new Email(title, content);

        /*
        Intent intent = new Intent(this, Subscription.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("email", email);
        intent.putExtras(bundle);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        */
        Log.d("no", "here");
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.logo2)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
