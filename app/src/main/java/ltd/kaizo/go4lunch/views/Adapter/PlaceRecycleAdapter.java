package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.views.PlaceViewHolder;
import timber.log.Timber;

/**
 * The type Place recycle adapter.
 */
public class PlaceRecycleAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    /**
     * The Glide.
     */
    private final RequestManager glide;
    private List<PlaceFormater> restaurantList;

    public PlaceRecycleAdapter(List<PlaceFormater> restaurantList, RequestManager glide) {
        this.glide = glide;
        this.restaurantList = restaurantList;
    }
//    /**
//     * Instantiates a new Place recycle adapter.
//     *
//     * @param options the options
//     * @param glide   the glide
//     */
//    public PlaceRecycleAdapter(FirestoreRecyclerOptions<Restaurant> options, RequestManager glide) {
//        super(options);
//        this.glide = glide;
//
//    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.place_fragment_list_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.updateViewWithRestaurant(restaurantList.get(position), this.glide);
      }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }




}
