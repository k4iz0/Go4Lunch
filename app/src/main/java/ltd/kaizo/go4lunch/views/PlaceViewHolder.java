package ltd.kaizo.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.views.Adapter.PlaceRecycleAdapter;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static ltd.kaizo.go4lunch.models.API.Stream.PlaceService.apiKey;

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
     * @param restaurant the restaurant
     * @param glide      the glide
     */
    public void updateViewWithRestaurant(Restaurant restaurant, RequestManager glide) {
        placeName.setText(restaurant.getPlaceFormater().getPlaceName());
        placeAdress.setText(restaurant.getPlaceFormater().getPlaceAddress());
        this.displayRatingStars(restaurant.getPlaceFormater().getPlaceRate());
        String photoUrl = "";
        if (!restaurant.getPlaceFormater().getPlacePhoto().equals("")) {
            photoUrl = placePhotoRequestUrl + restaurant.getPlaceFormater().getPlacePhoto() + "&key=" + apiKey;
        }
        placeDistance.setText(String.valueOf(restaurant.getPlaceFormater().getPlaceDistance() + "m"));
        glide.load(photoUrl)
              .apply(bitmapTransform(new RoundedCorners(10)) .fitCenter())
                .into(this.placePhoto);
        //TODO mettre photo par défaut
        placeHours.setText(restaurant.getPlaceFormater().getOpenOrClose());
        if (restaurant.getUserList().size() > 0) {
            this.configureMatesIcon(restaurant.getUserList().size());
        }

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
        personNumber.setText("(" + nb + ")");
        personNumber.setVisibility(View.VISIBLE);
        personIcon.setVisibility(View.VISIBLE);
    }


}
