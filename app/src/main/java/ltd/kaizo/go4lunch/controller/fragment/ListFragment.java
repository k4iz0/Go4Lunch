package ltd.kaizo.go4lunch.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.utils.ItemClickSupport;
import ltd.kaizo.go4lunch.views.adapter.PlaceRecycleAdapter;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_DETAIL_KEY;

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
     * The RestaurantList.
     */
    private List<PlaceFormater> restaurantList;

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
        this.configureRecycleView();
        this.configureOnClickRecyclerView();
    }

    @Override
    protected void updateDesign() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantList = getArguments().getParcelableArrayList(RESTAURANT_LIST_DETAIL_KEY);
        }
    }
    /**
     * Configure recycle view.
     */
    public void configureRecycleView() {
        this.adapter = new PlaceRecycleAdapter(restaurantList, Glide.with(this));
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
                        detailActivity.putExtra("PlaceFormater", restaurantList.get(position));
                        startActivity(detailActivity);
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
