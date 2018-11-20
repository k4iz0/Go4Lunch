package ltd.kaizo.go4lunch.controller.fragment;


import android.content.Intent;
import android.support.design.widget.Snackbar;
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
import ltd.kaizo.go4lunch.views.Adapter.MatesRecycleAdapter;
import ltd.kaizo.go4lunch.views.MatesViewHolder;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantListFromSharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatesFragment extends BaseFragment {
    @BindView(R.id.fragment_mates_recycleView)
    RecyclerView recyclerView;
    private RecyclerView.Adapter matesAdapter;

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
    protected void updateDesign() {

    }

    public void configureRecycleView() {
        this.matesAdapter = new MatesRecycleAdapter(generateOptionsForAdapter(UserHelper.getAllUser()), Glide.with(this));
        this.recyclerView.setAdapter(matesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.place_fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        User selectedUSer = ((FirestoreRecyclerAdapter<User, MatesViewHolder>) matesAdapter).getItem(position);
                        if (!selectedUSer.getChosenRestaurant().equalsIgnoreCase("")) {

                            ArrayList<PlaceFormater> restaurantList = getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY);
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
