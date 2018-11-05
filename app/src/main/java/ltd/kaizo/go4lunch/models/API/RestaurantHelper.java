package ltd.kaizo.go4lunch.models.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;

public class RestaurantHelper {

    public static final String COLLECTION_NAME = "restaurants";

    public static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //-- CREATE
    public static Task<Void> createRestaurant(String placeId, PlaceFormater placeFormater) {
        Restaurant restaurantsToCreate = new Restaurant(placeId, placeFormater, false);
        return RestaurantHelper.getRestaurantsCollection().document(placeId).set(restaurantsToCreate);
    }

    //GET
    public static Task<DocumentSnapshot> getRestaurant(String placeId) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).get();
    }
// --- GET All RESTAURANT---

    public static Query getAllRestaurants(){

        return RestaurantHelper.getRestaurantsCollection().orderBy("placeFormater.placeDistance");

    }
    // --- UPDATE ---
//
//    public static Task<Void> addUserToList(String userId, String placeId) {
//
//        return RestaurantHelper.getRestaurantsCollection().document(placeId).update("userList", isMentor);
//
//    }


     //--- DELETE ---


    public static Task<Void> deleteAllRestaurantFromList() {

        return RestaurantHelper.getRestaurantsCollection().document().delete();

    }

}
