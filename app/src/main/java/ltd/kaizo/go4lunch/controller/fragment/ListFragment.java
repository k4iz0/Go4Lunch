package ltd.kaizo.go4lunch.controller.fragment;


import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantListFromSharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment {
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

    }

    @Override
    protected void updateDesign() {

    }

    private void getRestaurantListFromMap() {
        this.restaurantlist = getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY).getFormatedListOfPlace();
    }
}
