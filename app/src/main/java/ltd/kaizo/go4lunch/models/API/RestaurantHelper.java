package ltd.kaizo.go4lunch.models.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;

/**
 * The type Restaurant helper.
 */
public class RestaurantHelper {

    /**
     * The constant COLLECTION_NAME.
     */
    public static final String COLLECTION_NAME = "restaurants";

    /**
     * Gets restaurants collection.
     *
     * @return the restaurants collection
     */
    public static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**
     * Create restaurant task.
     *
     * @param placeId       the place id
     * @param placeFormater the place formater
     * @return the task
     */
//-- CREATE
    public static Task<Void> createRestaurant(String placeId, PlaceFormater placeFormater) {
        Restaurant restaurantsToCreate = new Restaurant(placeId, placeFormater, false);
        return RestaurantHelper.getRestaurantsCollection().document(placeId).set(restaurantsToCreate);
    }

    /**
     * Gets restaurant.
     *
     * @param placeId the place id
     * @return the restaurant
     */
//GET RESTAURANT
    public static Task<DocumentSnapshot> getRestaurant(String placeId) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).get();
    }

    // --- GET All RESTAURANT---

    /**
     * Gets all restaurants.
     *
     * @return the all restaurants
     */
    public static Query getAllRestaurants() {

        return RestaurantHelper.getRestaurantsCollection().orderBy("placeFormater.placeDistance");

    }

    /**
     * Gets all restaurants from firestore.
     *
     * @return the all restaurants from firestore
     */
    public static Task<QuerySnapshot> getAllRestaurantsFromFirestore() {

        return RestaurantHelper.getRestaurantsCollection().get();

    }


    /**
     * Update user from restaurant task.
     *
     * @param placeId the place id
     * @param uid     the uid
     * @return the task
     */
//UPDATE
    public static Task<Void> updateUserFromRestaurant(String placeId, String uid) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).update("userList", FieldValue.arrayUnion(uid));
    }

    public static Task<Void> deleteAllUsersFromRestaurant(Restaurant restaurant) {
        ArrayList emptyArray = new ArrayList();
        restaurant.setUserList(emptyArray);
        return RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId()).set(restaurant);
    }

    //--- DELETE ---

    /**
     * Delete restaurants from list task.
     *
     * @param restoList the resto list
     * @return the task
     */
    public static Task<Void> deleteRestaurantsFromList(List<DocumentSnapshot> restoList) {
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        if (restoList != null) {

            for (DocumentSnapshot doc : restoList) {
                Restaurant restaurant = doc.toObject(Restaurant.class);
                DocumentReference placeRef = RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId());
                batch.delete(placeRef);
            }
        }
        return batch.commit();
    }

    /**
     * Delete user from restaurant task.
     *
     * @param placeId the place id
     * @param uid     the uid
     * @return the task
     */
    public static Task<Void> deleteAllUsersFromRestaurant(String placeId, String uid) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).update("userList", FieldValue.arrayRemove(uid));
    }

}
