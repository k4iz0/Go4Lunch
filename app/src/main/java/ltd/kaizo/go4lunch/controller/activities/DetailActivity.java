package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import ltd.kaizo.go4lunch.BuildConfig;
import ltd.kaizo.go4lunch.R;
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

    private String placePhotoRequestUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=300&photoreference=";

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void configureDesign() {
        this.getPlaceFormaterFromIntent();
        this.updateUiWithPlaceData();
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
