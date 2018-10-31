package ltd.kaizo.go4lunch.models.API;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ltd.kaizo.go4lunch.models.User;

import static com.firebase.ui.auth.AuthUI.TAG;

public class UserHelper {
    private static final String COLLECTION_NAME = "users";


    // --- COLLECTION REFERENCE ---


    public static CollectionReference getUsersCollection(){

        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

    }


    // --- CREATE ---


    public static Task<Void> createUser(String uid, String username, String urlPicture) {

        User userToCreate = new User(uid, username, urlPicture);
        Log.i(TAG, "createUser: uid = "+uid+" and username = "+username);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);

    }


    // --- GET ---


    public static Task<DocumentSnapshot> getUser(String uid){

        return UserHelper.getUsersCollection().document(uid).get();

    }


    // --- UPDATE ---


    public static Task<Void> updateUsername(String username, String uid) {

        return UserHelper.getUsersCollection().document(uid).update("username", username);

    }


    // --- DELETE ---


    public static Task<Void> deleteUser(String uid) {

        return UserHelper.getUsersCollection().document(uid).delete();

    }

}
