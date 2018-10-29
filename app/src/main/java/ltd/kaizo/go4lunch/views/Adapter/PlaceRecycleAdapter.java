package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.List;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.views.PlaceViewholder;

public class PlaceRecycleAdapter extends RecyclerView.Adapter<PlaceViewholder> {

    private final List<PlaceFormater> restaurantList;
    private final RequestManager glide;
    public PlaceRecycleAdapter(List<PlaceFormater> restaurantList, RequestManager glide) {
        this.restaurantList = restaurantList;
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
    public void onBindViewHolder(@NonNull PlaceViewholder holder, int position) {
        holder.updateViewWithRestaurant(this.restaurantList, position, this.glide);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


}
