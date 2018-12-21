package ltd.kaizo.go4lunch.models.API;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import ltd.kaizo.go4lunch.models.User;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * The type User helper.
 */
public class UserHelper {
    /**
     * The constant COLLECTION_NAME.
     */
    private static final String COLLECTION_NAME = "users";


    // --- COLLECTION REFERENCE ---


    /**
     * Get users collection collection reference.
     *
     * @return the collection reference
     */
    public static CollectionReference getUsersCollection(){

        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

    }


    // --- CREATE ---


    /**
     * Create user task.
     *
     * @param uid        the uid
     * @param username   the username
     * @param urlPicture the url picture
     * @return the task
     */
    public static Task<Void> createUser(String uid, String username, String urlPicture, String email) {

        User userToCreate = new User(uid, username, urlPicture, email);
        Log.i(TAG, "createUser: uid = "+uid+" and username = "+username);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);

    }


    // --- GET ---


    /**
     * Get user task.
     *
     * @param uid the uid
     * @return the task
     */
    public static Task<DocumentSnapshot> getUser(String uid){

        return UserHelper.getUsersCollection().document(uid).get();

    }

    // --- GET All USERS---

    /**
     * Gets all user.
     *
     * @return the all user
     */
    public static Query getAllUser() {

        return UserHelper.getUsersCollection()
                .orderBy("chosenRestaurant",Query.Direction.DESCENDING);

    }


    // --- UPDATE ---


    /**
     * Update chosen restaurant task.
     *
     * @param restaurantId the restaurant id
     * @param uid          the uid
     * @return the task
     */
    public static Task<Void> updateChosenRestaurant(String restaurantId, String uid) {

        return UserHelper.getUsersCollection().document(uid).update("chosenRestaurant", restaurantId);

    }

    /**
     * Update like restaurant task.
     *
     * @param restaurantId the restaurant id
     * @param uid          the uid
     * @return the task
     */
    public static Task<Void> updateLikeRestaurant(String restaurantId, String uid) {

        return UserHelper.getUsersCollection().document(uid).update("restaurantLikeList",FieldValue.arrayUnion(restaurantId));

    }
    // --- UPDATE ---
    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);

    }
 public static Task<Void> updateEmail(String email, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("email", email);

    }

    // --- DELETE ---

    /**
     * Delete user task.
     *
     * @param uid the uid
     * @return the task
     */
    public static Task<Void> deleteUser(String uid) {

        return UserHelper.getUsersCollection().document(uid).delete();

    }

    /**
     * Delete like restaurant task.
     *
     * @param restaurantId the restaurant id
     * @param uid          the uid
     * @return the task
     */
    public static Task<Void> deleteLikeRestaurant(String restaurantId, String uid) {

        return UserHelper.getUsersCollection().document(uid).update("restaurantLikeList",FieldValue.arrayRemove(restaurantId));

    }

    /**
     * Gets user data from id.
     *
     * @param userId      the user id
     * @param allUserList the all user list
     * @return the user data from id
     */
    public static User getUserDataFromId(String userId, List<User> allUserList) {
        User tmpUser = null;
        for (User user : allUserList) {
            if (user.getUid().equalsIgnoreCase(userId)) {
                tmpUser = user;
            }
        }
        return tmpUser;
    }
}
