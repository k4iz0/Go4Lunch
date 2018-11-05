package ltd.kaizo.go4lunch.models.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurants;

public class RestaurantHelper {

    public static final String COLLECTION_NAME = "restaurants";

    public static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //-- CREATE
    public static Task<Void> createRestaurant(String placeId, PlaceFormater placeFormater) {
        Restaurants restaurantsToCreate = new Restaurants(placeId, placeFormater, false);
        return RestaurantHelper.getRestaurantsCollection().document(placeId).set(restaurantsToCreate);
    }

    //GET
    public static Task<DocumentSnapshot> getRestaurant(String placeId) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).get();
    }

    // --- UPDATE ---
//
//    public static Task<Void> addUserToList(String userId, String placeId) {
//
//        return RestaurantHelper.getRestaurantsCollection().document(placeId).update("userList", isMentor);
//
//    }


    // --- DELETE ---

//
//    public static Task<Void> deleteUserFromList(String uid) {
//
//        return RestaurantHelper.getRestaurantsCollection().document(uid).delete();
//
//    }

}
