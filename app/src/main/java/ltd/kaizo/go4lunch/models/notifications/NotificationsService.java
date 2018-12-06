package ltd.kaizo.go4lunch.models.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.ALL_USER_LIST;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.NOTIFICATION_ENABLE;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getUserListFromSharedPreferences;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;

/**
 * The type Notifications service.
 */
public class NotificationsService extends FirebaseMessagingService {
    /**
     * The Notification id.
     */
    private final int NOTIFICATION_ID = 007;
    /**
     * The Notification tag.
     */
    private final String NOTIFICATION_TAG = "FIREBASE_GO4LUNCH";
    /**
     * The All user list.
     */
    private List<User> allUserList = new ArrayList<>();
    /**
     * The Current user.
     */
    private User currentUser;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            getLunchInfo();
        }
    }

    /**
     * Send visual notification.
     *
     * @param messageBody   the message body
     * @param placeFormater the place formater
     */
    private void sendVisualNotification(String messageBody, PlaceFormater placeFormater) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("PlaceFormater", placeFormater);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(messageBody);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notitificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.restau_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notificationTitle))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(bigTextStyle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.incoming_message);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notitificationBuilder.build());
    }

    /**
     * Gets lunch info.
     */
    private void getLunchInfo() {
        //get currentUser info
        if (read(NOTIFICATION_ENABLE, true)) {

            UserHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult() != null) {
                        currentUser = task.getResult().toObject(User.class);
                        //get chosenRestaurantInfo
                        if (!currentUser.getChosenRestaurant().equalsIgnoreCase("")) {
                            RestaurantHelper.getRestaurant(currentUser.getChosenRestaurant()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult() != null) {
                                        Restaurant restaurant = task.getResult().toObject(Restaurant.class);
                                        String placeAddress = restaurant.getPlaceFormater().getPlaceAddress();
                                        String placeName = restaurant.getPlaceFormater().getPlaceName();
                                        ArrayList<User> joiningUser = new ArrayList<>();
                                        allUserList = getUserListFromSharedPreferences(ALL_USER_LIST);
                                        for (String userId : restaurant.getUserList()) {
                                            if (!userId.equalsIgnoreCase(currentUser.getUid())) {
                                                joiningUser.add(UserHelper.getUserDataFromId(userId, allUserList));
                                            }
                                        }
                                        //create message
                                        String message = "you're going to lunch at " +
                                                placeName + " \n" +
                                                placeAddress + " \n";

                                        if (joiningUser.size() > 0) {
                                            message += " with ";
                                            for (User user : joiningUser) {
                                                message += " " + user.getUsername() + " \n";
                                            }
                                        }
                                        sendVisualNotification(message, restaurant.getPlaceFormater());
                                    }
                                }
                            });

                        }

                    }
                }
            });
        }
    }

}
