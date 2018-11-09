package ltd.kaizo.go4lunch.models.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

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

    public static Task<QuerySnapshot> getAllRestaurantsFromFirestore(){

        return RestaurantHelper.getRestaurantsCollection().get();

    }

     //--- DELETE ---
     public static Task<Void> deleteRestaurantsFromList (List<DocumentSnapshot> restoList) {
         WriteBatch batch = FirebaseFirestore.getInstance().batch();
         if (restoList != null) {

             for (DocumentSnapshot doc : restoList) {
                 Restaurant restaurant = doc.toObject(Restaurant.class);
                 DocumentReference placeRef = RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId());
                 batch.delete(placeRef);

                 //RestaurantHelper.deleteRestaurantFromList(restaurant.getPlaceId());
             }
         }



         return batch.commit();
     }


    public static Task<Void> deleteRestaurantFromList(String placeId) {

        return RestaurantHelper.getRestaurantsCollection().document(placeId).delete();

    }

}
