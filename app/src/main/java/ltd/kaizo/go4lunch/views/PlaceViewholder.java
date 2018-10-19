package ltd.kaizo.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;

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


    public PlaceViewholder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }

    public void updateViewWithRestaurant(List<PlaceFormater> restaurantList, int position, RequestManager glide) {
        placeName.setText(restaurantList.get(position).getPlaceName());
//        placeAdress.setText(restaurantList.get(position).);

    }
}