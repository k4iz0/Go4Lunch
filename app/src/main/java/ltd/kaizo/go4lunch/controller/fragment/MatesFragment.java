package ltd.kaizo.go4lunch.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.models.utils.ItemClickSupport;
import ltd.kaizo.go4lunch.views.MatesViewHolder;
import ltd.kaizo.go4lunch.views.adapter.MatesRecycleAdapter;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_DETAIL_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatesFragment extends BaseFragment {
    /**
     * The Recycler view.
     */
    @BindView(R.id.fragment_mates_recycleView)
    RecyclerView recyclerView;
    /**
     * The Mates adapter.
     */
    private RecyclerView.Adapter matesAdapter;
    /**
     * The restaurant list
     */
    private ArrayList<PlaceFormater> restaurantList;

    /**
     * Instantiates a new Mates fragment.
     */
    public MatesFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_mates;
    }

    @Override
    protected void configureDesign() {
        this.configureRecycleView();
        this.configureOnClickRecyclerView();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantList = getArguments().getParcelableArrayList(RESTAURANT_LIST_DETAIL_KEY);        }
    }
    @Override
    protected void updateDesign() {}


    /**
     * Configure recycle view.
     */
    public void configureRecycleView() {
        this.matesAdapter = new MatesRecycleAdapter(generateOptionsForAdapter(UserHelper.getAllUser()), Glide.with(this), restaurantList);
        this.recyclerView.setAdapter(matesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * Generate options for adapter firestore recycler options.
     *
     * @param query the query
     * @return the firestore recycler options
     */
    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }

    /**
     * Configure on click recycler view.
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.place_fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        User selectedUSer = ((FirestoreRecyclerAdapter<User, MatesViewHolder>) matesAdapter).getItem(position);
                        if (!selectedUSer.getChosenRestaurant().equalsIgnoreCase("")) {
                            for (PlaceFormater place : restaurantList) {
                                if (selectedUSer.getChosenRestaurant().equalsIgnoreCase(place.getId())) {
                                    Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                                    detailActivity.putExtra("PlaceFormater", place);
                                    startActivity(detailActivity);
                                }
                            }
                        }

                    }
                });
    }
}
