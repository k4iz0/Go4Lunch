package ltd.kaizo.go4lunch.controller.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;
import ltd.kaizo.go4lunch.views.Adapter.PlaceRecycleAdapter;
import ltd.kaizo.go4lunch.views.PlaceViewholder;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantListFromSharedPreferences;
/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment {
@BindView(R.id.fragment_list_recycleview)
    RecyclerView recyclerView;
    private PlaceRecycleAdapter adapter;
    private List<PlaceFormater> restaurantlist;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_list;
    }

    @Override
    protected void configureDesign() {
        this.getRestaurantListFromMap();
        this.configureRecycleView();
    }

    @Override
    protected void updateDesign() {

    }

    public void configureRecycleView() {
        this.adapter = new PlaceRecycleAdapter(this.restaurantlist, Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void getRestaurantListFromMap() {
        this.restaurantlist = getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY).getPlaceDetailList();


    }
}
