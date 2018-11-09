package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import ltd.kaizo.go4lunch.BuildConfig;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;

public class DetailActivity extends BaseActivity {
    @BindView(R.id.activity_detail_photo_imageview)
    ImageView placePhoto;
    @BindView(R.id.activity_detail_place_name_textview)
    TextView placename;
    @BindView(R.id.activity_detail_place_address_textview)
    TextView placeAdress;
    @BindView(R.id.activity_detail_star1_imageview)
    ImageView ratingStar1;
    @BindView(R.id.activity_detail_star2_imageview)
    ImageView ratingStar2;
    @BindView(R.id.activity_detail_star3_imageview)
    ImageView ratingStar3;
    PlaceFormater place;
    @BindView(R.id.activity_detail_recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_detail_fab)
    FloatingActionButton floatingActionButton;
    private String placePhotoRequestUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=300&photoreference=";

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void configureDesign() {
        this.getPlaceFormaterFromIntent();
        this.updateUiWithPlaceData();
        this.configureFloatingButton();
    }

    private void getPlaceFormaterFromIntent() {
        Intent intent = getIntent();
        place = intent.getParcelableExtra("PlaceFormater");
    }

    private void updateUiWithPlaceData() {
        placename.setText(place.getPlaceName());
        placeAdress.setText(place.getPlaceAddress());
         Glide.with(this).load(placePhotoRequestUrl+place.getPlacePhoto()+"&key="+BuildConfig.ApiKey)
                    .apply(RequestOptions.centerCropTransform())
                .into(this.placePhoto);
         displayRatingStars(place.getPlaceRate());
    }
    private void configureFloatingButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToRestaurant();
            }
        });
    }

    private void addUserToRestaurant() {
        Log.i("detailActivity", "addUserToRestaurant: user = "+getCurrentUser().getUid()+" and placeId = "+place.getId());
        RestaurantHelper.updateRestauranUserList(getCurrentUser().getUid(), place.getId());
    }

    private void displayRatingStars(int rate) {
        switch (rate) {
            case 1:
                ratingStar1.setVisibility(View.VISIBLE);
                break;
            case 2:
                ratingStar1.setVisibility(View.VISIBLE);
                ratingStar2.setVisibility(View.VISIBLE);
                break;
            case 3:
                ratingStar1.setVisibility(View.VISIBLE);
                ratingStar2.setVisibility(View.VISIBLE);
                ratingStar3.setVisibility(View.VISIBLE);
                break;
        }
    }
//    public void configureRecycleView() {
//        this.adapter = new PlaceRecycleAdapter(this.restaurantlist, Glide.with(this));
//        this.recyclerView.setAdapter(adapter);
//        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//    }
}
