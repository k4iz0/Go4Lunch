package ltd.kaizo.go4lunch.models.utils.androidJob;

import android.support.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.Restaurant;
import timber.log.Timber;

public class ResetUserChoiceJob extends DailyJob {
    static final String TAG = "resetUserChoiceJob_job_tag";

    public static int schedulePeriodic() {
        return new JobRequest.Builder(ResetUserChoiceJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(15) + TimeUnit.MINUTES.toMillis(47))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        Timber.i("launching task job");
        this.resetUserRestaurantChoiceFromFirestore();
        return DailyJobResult.SUCCESS;
    }

    /**
     * Reset user 's choice restaurant from firestore.
     */
    private void resetUserRestaurantChoiceFromFirestore() {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserHelper.updateChosenRestaurant("", currentUserId);
        Timber.i("deleting user's choice");


        RestaurantHelper.getAllRestaurantsFromFirestore().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {

                    for (Restaurant restaurant : queryDocumentSnapshots.toObjects(Restaurant.class)) {
                        Timber.i("resetUserRestaurantChoiceFromFirestore: RestaurantHelper.updateUserFromRestaurant");
                        RestaurantHelper.deleteAllUsersFromRestaurant(restaurant);
                    }
                }
            }
        });

    }

}
