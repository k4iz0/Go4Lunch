package ltd.kaizo.go4lunch.controller.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.utils.ItemClickSupport;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.views.Adapter.PlaceRecycleAdapter;

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
        this.configureOnClickRecyclerView();
    }

    @Override
    protected void updateDesign() {

    }

    public void configureRecycleView() {
        this.adapter = new PlaceRecycleAdapter(this.restaurantlist, Glide.with(this));
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
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
    private void getRestaurantListFromMap() {
        this.restaurantlist = getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY);


    }


}
