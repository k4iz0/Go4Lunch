package ltd.kaizo.go4lunch.controller.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.List;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.utils.ItemClickSupport;
import ltd.kaizo.go4lunch.views.Adapter.PlaceRecycleAdapter;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantListFromSharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment {
    /**
     * The Recycler view.
     */
    @BindView(R.id.fragment_list_recycleview)
    RecyclerView recyclerView;
    /**
     * The Adapter.
     */
    private PlaceRecycleAdapter adapter;
    /**
     * The Restaurantlist.
     */
    private List<PlaceFormater> restaurantlist;

    /**
     * Instantiates a new List fragment.
     */
    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_list;
    }

    @Override
    protected void configureDesign() {
        this.getRestaurantListFromFirestore();
        this.configureRecycleView();
        this.configureOnClickRecyclerView();
    }

    @Override
    protected void updateDesign() {

    }

    /**
     * Configure recycle view.
     */
    public void configureRecycleView() {
        this.adapter = new PlaceRecycleAdapter(generateOptionsForAdapter(RestaurantHelper.getAllRestaurants()), Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * Configure on click recycler view.
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.place_fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                        detailActivity.putExtra("PlaceFormater", restaurantlist.get(position));
                        startActivity(detailActivity);
                    }
                });
    }

    /**
     * Gets restaurant list from firestore.
     */
    private void getRestaurantListFromFirestore() {
        this.restaurantlist = getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY);
    }

    /**
     * Generate options for adapter firestore recycler options.
     *
     * @param query the query
     * @return the firestore recycler options
     */
    private FirestoreRecyclerOptions<Restaurant> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Restaurant>()
                .setQuery(query, Restaurant.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
