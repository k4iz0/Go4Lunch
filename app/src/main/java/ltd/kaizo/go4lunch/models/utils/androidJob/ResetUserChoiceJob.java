package ltd.kaizo.go4lunch.models.utils.androidJob;

import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.concurrent.TimeUnit;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;
import timber.log.Timber;

public class ResetUserChoiceJob extends DailyJob {
    static final String TAG = "resetUserChoiceJob_job_tag";

    public static int schedulePeriodic() {
        return new JobRequest.Builder(ResetUserChoiceJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(18) + TimeUnit.MINUTES.toMillis(33), TimeUnit.MINUTES.toMillis(20))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        this.resetUserRestaurantChoiceFromFirestore();
        return DailyJobResult.SUCCESS;
    }

    /**
     * Reset user 's choice restaurant from firestore.
     */
    private void resetUserRestaurantChoiceFromFirestore() {
        Timber.i("resetUserRestaurantChoiceFromFirestore: entree dans la fonction");
        UserHelper.getAllUser().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    List<User> tmpUser = task.getResult().toObjects(User.class);
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (User user : tmpUser) {
                        if (user.getUid().equalsIgnoreCase(currentUserId)) {
                            UserHelper.updateChosenRestaurant("", user.getUid());
                            Timber.i("deleting user's choice");
                        }
                    }
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.i("onFailure: an error as occurred " + e);
            }
        });

        RestaurantHelper.getAllRestaurantsFromFirestore().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {

                    for (Restaurant restaurant : queryDocumentSnapshots.toObjects(Restaurant.class)) {
                        Timber.i("resetUserRestaurantChoiceFromFirestore: RestaurantHelper.updateUserFromRestaurant");

                        RestaurantHelper.updateUserFromRestaurant(restaurant.getPlaceId(), "");
                    }
                }
            }
        });

    }

}
