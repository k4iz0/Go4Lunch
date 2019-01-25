package ltd.kaizo.go4lunch.models.utils.androidJob;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;

public class ResetUserChoiceJob extends DailyJob {
    static final String TAG = "resetUserChoiceJob_job_tag";

    public static int schedulePeriodic() {
        return new JobRequest.Builder(ResetUserChoiceJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(16)+TimeUnit.MINUTES.toMillis(32)+TimeUnit.SECONDS.toMillis(5),TimeUnit.MINUTES.toMillis(5))
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
        Log.i("resetUserRestaur", "resetUserRestaurantChoiceFromFirestore: entree dans la fonction");
        UserHelper.getAllUser().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    List<User> tmpUser = task.getResult().toObjects(User.class);
                    for (User user : tmpUser) {
                        UserHelper.updateChosenRestaurant("", user.getUid());
                    }
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ResetJob", "onFailure: an error as occurred " + e);
            }
        });

        RestaurantHelper.getAllRestaurantsFromFirestore().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {

                    for (Restaurant restaurant : queryDocumentSnapshots.toObjects(Restaurant.class)) {
                        Log.i("resetUserRestaur", "resetUserRestaurantChoiceFromFirestore: RestaurantHelper.updateUserFromRestaurant");

                        RestaurantHelper.updateUserFromRestaurant(restaurant.getPlaceId(), "");
                    }
                }
            }
        });

    }

}
