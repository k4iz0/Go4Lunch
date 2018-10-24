package ltd.kaizo.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static ltd.kaizo.go4lunch.models.utils.PlaceService.apiKey;

public class PlaceViewholder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_list_name_textview)
    TextView placeName;
    @BindView(R.id.item_list_place_adress_textview)
    TextView placeAdress;
    @BindView(R.id.item_list_place_opening_hour_textview)
    TextView placeHours;
    @BindView(R.id.item_list_place_distance_textview)
    TextView placeDistance;
    @BindView(R.id.item_list_place_photo_imageview)
    ImageView placePhoto;
    @BindView(R.id.item_list_person_number_textview)
    TextView personNumber;
    @BindView(R.id.item_list_star1_imageview)
    ImageView rateStar1;
    @BindView(R.id.item_list_star2_imageview)
    ImageView rateStar2;
    @BindView(R.id.item_list_star3_imageview)
    ImageView rateStar3;
    private String placePhotoRequestUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=80&maxheight=80&photoreference=";

    public PlaceViewholder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateViewWithRestaurant(List<PlaceFormater> restaurantList, int position, RequestManager glide) {
        for (PlaceFormater place : restaurantList) {
            Log.i("ListFragment", "getRestaurantListFromMap: " + place.toString());
        }
        placeName.setText(restaurantList.get(position).getPlaceName());
        placeAdress.setText(restaurantList.get(position).getPlaceAdress());
        this.displayRatingStars(restaurantList.get(position).getPlaceRate());
            String photoUrl = "";
        if (!restaurantList.get(position).getPlacePhoto().equals("")) {
            photoUrl = placePhotoRequestUrl+restaurantList.get(position).getPlacePhoto()+"&key=" + apiKey;
        }

        glide.load(photoUrl)
                    .apply(RequestOptions.centerCropTransform()).apply(bitmapTransform(new RoundedCorners(15)))
                    .into(this.placePhoto);

//        placeHours.setText(restaurantList.get(position).get);


    }

    private void displayRatingStars(int rate) {
        switch (rate) {
            case 1:
                rateStar1.setVisibility(View.VISIBLE);
                break;
            case 2:
                rateStar1.setVisibility(View.VISIBLE);
                rateStar2.setVisibility(View.VISIBLE);
                break;
            case 3:
                rateStar1.setVisibility(View.VISIBLE);
                rateStar2.setVisibility(View.VISIBLE);
                rateStar3.setVisibility(View.VISIBLE);
                break;
        }
    }
}
