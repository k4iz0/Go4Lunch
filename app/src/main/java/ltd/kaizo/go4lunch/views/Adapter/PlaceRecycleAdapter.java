package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.views.PlaceViewHolder;

/**
 * The type Place recycle adapter.
 */
public class PlaceRecycleAdapter extends FirestoreRecyclerAdapter<Restaurant, PlaceViewHolder> {

    /**
     * The Glide.
     */
    private final RequestManager glide;

    /**
     * The interface Listener.
     */
    public interface Listener {

        /**
         * On click get place id string.
         *
         * @param position the position
         * @return the string
         */
        String onClickGetPlaceId(int position);
    }

    /**
     * Instantiates a new Place recycle adapter.
     *
     * @param options the options
     * @param glide   the glide
     */
    public PlaceRecycleAdapter(FirestoreRecyclerOptions<Restaurant> options, RequestManager glide) {
        super(options);
        this.glide = glide;

    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.place_fragment_list_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlaceViewHolder holder, int position, @NonNull Restaurant restaurant) {
        holder.updateViewWithRestaurant(restaurant, this.glide);

    }


}
