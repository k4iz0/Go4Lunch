package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.views.PlaceViewholder;

public class PlaceRecycleAdapter extends FirestoreRecyclerAdapter<Restaurant, PlaceViewholder> {

    private final RequestManager glide;
    public PlaceRecycleAdapter(FirestoreRecyclerOptions<Restaurant> options, RequestManager glide) {
        super(options);
        this.glide = glide;
    }

    @NonNull
    @Override
    public PlaceViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.place_fragment_list_item, parent, false);
        return new PlaceViewholder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlaceViewholder holder, int position, @NonNull Restaurant restaurant) {
        holder.updateViewWithRestaurant(restaurant, this.glide);

    }


}
