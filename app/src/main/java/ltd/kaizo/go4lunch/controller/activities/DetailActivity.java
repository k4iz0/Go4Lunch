package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import ltd.kaizo.go4lunch.BuildConfig;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.views.Adapter.JoiningMatesAdapter;

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
    private RecyclerView.Adapter joiningMatesAdapter;
    private Restaurant restaurant;
    private ArrayList<User> userList = new ArrayList<>();
    private String TAG = getClass().getSimpleName();

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void configureDesign() {
        this.getPlaceFormaterFromIntent();
        this.updateUiWithPlaceData();
        this.configureRecycleView();
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
                provideRecycleViewWithData();
            }
        });
    }

    private void addUserToRestaurant() {
        Log.i("detailActivity", "addUserToRestaurant: user = "+getCurrentUser().getUid()+" and placeId = "+place.getId());
        RestaurantHelper.updateRestauranUserList(place.getId(), place,getCurrentUser().getUid()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "an error has occurred " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), getCurrentUser().getDisplayName()+" has choose " + place.getPlaceName(), Toast.LENGTH_SHORT).show();            }
        });
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
    public void configureRecycleView() {


        this.joiningMatesAdapter = new JoiningMatesAdapter(this.provideRecycleViewWithData(), Glide.with(this));
        this.joiningMatesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(positionStart);
            }
        });
        this.recyclerView.setAdapter(joiningMatesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private ArrayList<User> provideRecycleViewWithData() {
        RestaurantHelper.getRestaurant(place.getId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                restaurant =  documentSnapshot.toObject(Restaurant.class);

            }
        });
        if (this.restaurant != null) {
            userList.clear();
            for (String user : this.restaurant.getUserList()) {
                Log.i(TAG, "onSuccess: userId = "+user);
                UserHelper.getUser(user).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        userList.add(documentSnapshot.toObject(User.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: fail to retrieve user");
                    }
                });
            }
            joiningMatesAdapter.notifyDataSetChanged();
        }
        return userList;
    }

}
