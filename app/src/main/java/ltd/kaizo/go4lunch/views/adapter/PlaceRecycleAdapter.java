package ltd.kaizo.go4lunch.views.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.views.PlaceViewHolder;

/**
 * The type Place recycle adapter.
 */
public class PlaceRecycleAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    /**
     * The Glide.
     */
    private final RequestManager glide;
    /**
     * The Restaurant list.
     */
    private List<PlaceFormater> restaurantList;

    /**
     * Instantiates a new Place recycle adapter.
     *
     * @param restaurantList the restaurant list
     * @param glide          the glide
     */
    public PlaceRecycleAdapter(List<PlaceFormater> restaurantList, RequestManager glide) {
        this.glide = glide;
        this.restaurantList = restaurantList;
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
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.updateViewWithRestaurant(restaurantList.get(position), this.glide);
      }


    @Override
    public int getItemCount() {
        if (restaurantList != null) {
            return restaurantList.size();
        } else {
            return 0;
        }

    }


    /**
     * Update data.
     *
     * @param restaurantList the restaurant list
     */
    public void updateData(ArrayList<PlaceFormater> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }
}
