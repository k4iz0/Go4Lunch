package ltd.kaizo.go4lunch.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static ltd.kaizo.go4lunch.models.API.stream.PlaceService.apiKey;

/**
 * The type Place view holder.
 */
public class PlaceViewHolder extends RecyclerView.ViewHolder {
    /**
     * The Place name.
     */
    @BindView(R.id.item_list_name_textview)
    TextView placeName;
    /**
     * The Place adress.
     */
    @BindView(R.id.item_list_place_adress_textview)
    TextView placeAdress;
    /**
     * The Place hours.
     */
    @BindView(R.id.item_list_place_opening_hour_textview)
    TextView placeHours;
    /**
     * The Place distance.
     */
    @BindView(R.id.item_list_place_distance_textview)
    TextView placeDistance;
    /**
     * The Place photo.
     */
    @BindView(R.id.item_list_place_photo_imageview)
    ImageView placePhoto;
    /**
     * The Person number.
     */
    @BindView(R.id.item_list_person_number_textview)
    TextView personNumber;
    /**
     * The Person icon.
     */
    @BindView(R.id.item_list_place_person_icon)
    ImageView personIcon;
    /**
     * The Rate star 1.
     */
    @BindView(R.id.item_list_star1_imageview)
    ImageView rateStar1;
    /**
     * The Rate star 2.
     */
    @BindView(R.id.item_list_star2_imageview)
    ImageView rateStar2;
    /**
     * The Rate star 3.
     */
    @BindView(R.id.item_list_star3_imageview)
    ImageView rateStar3;
    /**
     * The Place photo request url.
     */
    private String placePhotoRequestUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=80&maxheight=80&photoreference=";

    /**
     * Instantiates a new Place view holder.
     *
     * @param itemView the item view
     */
    public PlaceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Update view with restaurant.
     *
     * @param place the placeformater
     * @param glide the glide
     */
    public void updateViewWithRestaurant(PlaceFormater place, RequestManager glide) {

        placeName.setText(place.getPlaceName());

        placeAdress.setText(place.getPlaceAddress());

        this.displayRatingStars(place.getPlaceRate());

        placeDistance.setText(String.valueOf(place.getPlaceDistance() + "m"));

        String photoUrl = "";

        if (!place.getPlacePhoto().equals("")) {
            photoUrl = placePhotoRequestUrl + place.getPlacePhoto() + "&key=" + apiKey;
        }

        glide.load(photoUrl)
                .apply(centerCropTransform().error(R.drawable.resto_default))
                .into(this.placePhoto);

        placeHours.setText(place.getOpenOrClose(this.itemView.getContext()));
        RestaurantHelper.getRestaurant(place.getId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Restaurant restaurant = task.getResult().toObject(Restaurant.class);
                    if (restaurant.getUserList() != null) {
                    configureMatesIcon(restaurant.getUserList().size());
                    }
                }
            }
        });
    }

    /**
     * Display rating stars.
     *
     * @param rate the rate
     */
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

    /**
     * Configure mates icon.
     *
     * @param nb the nb
     */
    private void configureMatesIcon(int nb) {
        if (nb > 0) {
            personNumber.setText("(" + nb + ")");
            personNumber.setVisibility(View.VISIBLE);
            personIcon.setVisibility(View.VISIBLE);
        } else {
            personNumber.setText("");
            personNumber.setVisibility(View.INVISIBLE);
            personIcon.setVisibility(View.INVISIBLE);
        }

    }


}
