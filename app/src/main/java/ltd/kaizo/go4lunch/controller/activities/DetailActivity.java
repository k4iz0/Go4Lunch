package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ltd.kaizo.go4lunch.BuildConfig;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.views.Adapter.JoiningMatesAdapter;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.ALL_USER_LIST;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.write;

/**
 * The type Detail activity.
 */
public class DetailActivity extends BaseActivity {
    /**
     * The Place photo.
     */
    @BindView(R.id.activity_detail_photo_imageview)
    ImageView placePhoto;
    /**
     * The Placename.
     */
    @BindView(R.id.activity_detail_place_name_textview)
    TextView placename;
    /**
     * The Place adress.
     */
    @BindView(R.id.activity_detail_place_address_textview)
    TextView placeAdress;
    /**
     * The Rating star 1.
     */
    @BindView(R.id.activity_detail_star1_imageview)
    ImageView ratingStar1;
    /**
     * The Rating star 2.
     */
    @BindView(R.id.activity_detail_star2_imageview)
    ImageView ratingStar2;
    /**
     * The Rating star 3.
     */
    @BindView(R.id.activity_detail_star3_imageview)
    ImageView ratingStar3;
    /**
     * The Place.
     */
    PlaceFormater place;
    /**
     * The Recycler view.
     */
    @BindView(R.id.activity_detail_recycleview)
    RecyclerView recyclerView;
    /**
     * The Floating action button.
     */
    @BindView(R.id.fragment_detail_fab)
    FloatingActionButton floatingActionButton;
    /**
     * The Phone btn.
     */
    @BindView(R.id.activity_detail_phone_btn)
    ImageButton phoneBtn;
    /**
     * The Like btn.
     */
    @BindView(R.id.activity_detail_star_btn)
    ImageButton likeBtn;
    /**
     * The Web btn.
     */
    @BindView(R.id.activity_detail_web_btn)
    ImageButton webBtn;
    /**
     * The Place photo request url.
     */
    private String placePhotoRequestUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&maxheight=500&photoreference=";
    /**
     * The Joining mates adapter.
     */
    private JoiningMatesAdapter joiningMatesAdapter;
    /**
     * The Restaurant.
     */
    private Restaurant restaurant;
    /**
     * The User list.
     */
    private ArrayList<User> userList = new ArrayList<>();
    /**
     * The All user list.
     */
    private List<User> allUserList = new ArrayList<>();
    /**
     * The Tag.
     */
    private String TAG = getClass().getSimpleName();
    /**
     * The Current user.
     */
    private User currentUser;
    /**
     * Boolean is fab pressed.
     */
    private Boolean isFabPressed = false;
    /**
     * Boolean like button pressed
     */
    private Boolean isLikePressed = false;
    /**
     * The Gson.
     */
    private Gson gson = new Gson();

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void configureDesign() {
        this.configureUserListFromFirebase();
        this.getPlaceFormaterFromIntent();
        this.configureCurrentUser();
        this.configureRecycleView();

    }

    /**
     * Configure currentUser with data from Firestore
     */
    private void configureCurrentUser() {
        UserHelper.getUser(getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    currentUser = task.getResult().toObject(User.class);
                    updateUiWithPlaceData();
                    firebaseListener();
                }
            }
        });
    }

    /**
     * Configure user list from firebase.
     */
    private void configureUserListFromFirebase() {
        UserHelper.getAllUser().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    allUserList = task.getResult().toObjects(User.class);
                    write(ALL_USER_LIST, gson.toJson(allUserList));
                }
            }
        });
    }

    /**
     * Gets place formater from intent.
     */
    private void getPlaceFormaterFromIntent() {
        Intent intent = getIntent();
        place = intent.getParcelableExtra("PlaceFormater");
    }

    //****************************
    //*********** UI *************
    //****************************

    /**
     * Display rating stars.
     * @param rate the rate
     */
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

    /**
     * Update ui with place data.
     */
    private void updateUiWithPlaceData() {
        placename.setText(place.getPlaceName());
        placeAdress.setText(place.getPlaceAddress());
        Glide.with(this).load(placePhotoRequestUrl + place.getPlacePhoto() + "&key=" + BuildConfig.ApiKey)
                .apply(RequestOptions.centerCropTransform())
                .into(this.placePhoto);
        displayRatingStars(place.getPlaceRate());
        if (currentUser.getRestaurantLikeList() != null) {

            for (String restauId : currentUser.getRestaurantLikeList()) {
                if (restauId.equalsIgnoreCase(place.getId())) {
                    likeBtn.setImageResource(R.drawable.ic_star_yellow);
                    isLikePressed = true;
                }
            }
        }
        this.configureButtons();
    }

    /**
     * Configure buttons.
     */
    private void configureButtons() {
        this.configureFloatingActionButton();
        //PhoneButton
        if (place.getPhoneNumber() != null && isTelephonyEnabled()) {

            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + place.getPhoneNumber()));

                    startActivity(callIntent);
                }
            });
        }
        //WebsiteButton
        if (place.getWebsiteUrl() != null) {
            webBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getWebsiteUrl()));
                    Log.i(TAG, "phoneOnclick: website  = " + place.getWebsiteUrl());
                    startActivity(browserIntent);
                }
            });
        } else {
            Toast.makeText(this, "website Url unavailable", Toast.LENGTH_SHORT).show();
        }
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLikePressed) {
                    unlikeRestaurant();
                    isLikePressed = false;
                    likeBtn.setImageResource(R.drawable.ic_star);
                } else {
                    likeRestaurant();
                    isLikePressed = true;
                    likeBtn.setImageResource(R.drawable.ic_star_yellow);
                }
            }
        });
    }

    /**
     * Configure floating action button.
     */
    private void configureFloatingActionButton() {
        //floatingActionButton
        if (currentUser.getHasChoose(place.getId())) {
            isFabPressed = true;
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check));
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabPressed) {
                    removeUserFromRestaurant();
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_plus_one));
                    isFabPressed = false;
                } else {
                    addUserToRestaurant();
                    addRestaurantToUser();
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check));
                    isFabPressed = true;
                }
            }
        });
    }

    /**
     * Is telephony enabled boolean.
     *
     * @return the boolean
     */
    private boolean isTelephonyEnabled() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * Gets user data from id.
     *
     * @param userId the user id
     * @return the user data from id
     */
    private User getUserDataFromId(String userId) {
        User tmpUser = null;
        for (User user : allUserList) {
            if (user.getUid().equalsIgnoreCase(userId)) {
                tmpUser = user;
            }
        }
        return tmpUser;
    }
    //****************************
    //******** FIREBASE **********
    //****************************

    /**
     * Firebase listener.
     */
    private void firebaseListener() {
        RestaurantHelper.getRestaurantsCollection().addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) return;

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    Restaurant tmp = documentSnapshot.toObject(Restaurant.class);
                    switch (dc.getType()) {
                        case MODIFIED:
                            Log.i(TAG, "onEvent: modified");
                            Log.i(TAG, "onEvent: size = " + tmp.getUserList().size());

                            if (tmp.getPlaceId().equalsIgnoreCase(place.getId())) {
                                userList.clear();
                                for (String userId : tmp.getUserList()) {
                                    userList.add(getUserDataFromId(userId));
                                }
                                joiningMatesAdapter.setUserList(userList);
                            }
                            break;
                    }
                }
            }
        });

    }

    /**
     * Add user to restaurant.
     */
    private void addUserToRestaurant() {
        if (!currentUser.getChosenRestaurant().equalsIgnoreCase("")) {
            this.removeUserFromRestaurant();
        }

        Log.i("detailActivity", "addUserToRestaurant: user = " + getCurrentUser().getUid() + " and placeId = " + place.getId());
        RestaurantHelper.getRestaurant(place.getId()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "an error has occurred " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    final Restaurant restaurant = task.getResult().toObject(Restaurant.class);
                    RestaurantHelper.updateUserFromRestaurant(place.getId(), currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            currentUser.setChosenRestaurant(restaurant.getPlaceId());
                            Toast.makeText(getApplicationContext(), getCurrentUser().getDisplayName() + " has choose " + place.getPlaceName(), Toast.LENGTH_SHORT).show();
                            joiningMatesAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    /**
     * Remove user from restaurant.
     */
    private void removeUserFromRestaurant() {
        Log.i(TAG, "removeUserFromRestaurant: " + currentUser.getUsername());

        RestaurantHelper.getRestaurant(currentUser.getChosenRestaurant()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    final Restaurant restaurant = task.getResult().toObject(Restaurant.class);
                    RestaurantHelper.deleteUserFromRestaurant(place.getId(), currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), getCurrentUser().getDisplayName() + " remove from " + restaurant.getPlaceFormater().getPlaceName(), Toast.LENGTH_SHORT).show();
                            //remove chosen restaurant from user
                            UserHelper.updateChosenRestaurant("", currentUser.getUid());
                        }
                    });
                }
            }
        });

    }

    /**
     * Add restaurant to user.
     */
    private void addRestaurantToUser() {
        UserHelper.updateChosenRestaurant(place.getId(), getCurrentUser().getUid()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "an error has occurred " + e, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void likeRestaurant() {
        UserHelper.updateLikeRestaurant(place.getId(), currentUser.getUid());
    }

    private void unlikeRestaurant() {
        UserHelper.deleteLikeRestaurant(place.getId(), currentUser.getUid());
    }

    //****************************
    //****** RECYCLE VIEW ********
    //****************************

    /**
     * Configure recycle view.
     */
    public void configureRecycleView() {
        this.provideRecycleViewWithData();
        this.joiningMatesAdapter = new JoiningMatesAdapter(this.userList, Glide.with(this));
        this.recyclerView.setAdapter(joiningMatesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    /**
     * Provide recycle view with data.
     */
    private void provideRecycleViewWithData() {

        RestaurantHelper.getRestaurant(place.getId()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: fail to retrieve restaurant info");
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    restaurant = task.getResult().toObject(Restaurant.class);
                    userList.clear();
                    for (String userId : restaurant.getUserList()) {
                        userList.add(getUserDataFromId(userId));
                    }
                    joiningMatesAdapter.setUserList(userList);
                }
            }
        });

    }

}
