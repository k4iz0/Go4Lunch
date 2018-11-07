package ltd.kaizo.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.Restaurant;

import static ltd.kaizo.go4lunch.models.API.Stream.PlaceService.apiKey;

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
    private OnItemClickListener listener;

    public PlaceViewholder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                }
            }
        });
    }

    public void updateViewWithRestaurant(Restaurant restaurant, RequestManager glide) {
        Log.i("PlaceViewHolder", "updateViewWithRestaurant: " + restaurant.getPlaceFormater().getPlaceName());
        placeName.setText(restaurant.getPlaceFormater().getPlaceName());
        placeAdress.setText(restaurant.getPlaceFormater().getPlaceAddress());
        this.displayRatingStars(restaurant.getPlaceFormater().getPlaceRate());
        String photoUrl = "";
        if (!restaurant.getPlaceFormater().getPlacePhoto().equals("")) {
            photoUrl = placePhotoRequestUrl + restaurant.getPlaceFormater().getPlacePhoto() + "&key=" + apiKey;
        }
        placeDistance.setText(String.valueOf(restaurant.getPlaceFormater().getPlaceDistance() + "m"));
        glide.load(photoUrl)
        //      .apply(RequestOptions.centerCropTransform()).apply(bitmapTransform(new RoundedCorners(15)))
                .into(this.placePhoto);
        placeHours.setText(restaurant.getPlaceFormater().formatStringWeekdayList());


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

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
