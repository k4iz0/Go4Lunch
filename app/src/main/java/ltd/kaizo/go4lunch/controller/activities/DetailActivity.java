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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
    @BindView(R.id.activity_detail_phone_btn)
    ImageButton phoneBtn;
    @BindView(R.id.activity_detail_star_btn)
    ImageButton likeBtn;
    @BindView(R.id.activity_detail_web_btn)
    ImageButton webBtn;
    private String placePhotoRequestUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=300&photoreference=";
    private JoiningMatesAdapter joiningMatesAdapter;
    private Restaurant restaurant;
    private ArrayList<User> userList = new ArrayList<>();
    private List<User> allUserList = new ArrayList<>();
    private String TAG = getClass().getSimpleName();
    private User currentUser;
    private Boolean isFabPressed = false;

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

    private void configureUserListFromFirebase() {
        UserHelper.getAllUser().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    allUserList = task.getResult().toObjects(User.class);
                }
            }
        });
    }

    private void getPlaceFormaterFromIntent() {
        Intent intent = getIntent();
        place = intent.getParcelableExtra("PlaceFormater");
    }

    //****************************
    //*********** UI *************
    //****************************

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

    private void updateUiWithPlaceData() {
        placename.setText(place.getPlaceName());
        placeAdress.setText(place.getPlaceAddress());
        Glide.with(this).load(placePhotoRequestUrl + place.getPlacePhoto() + "&key=" + BuildConfig.ApiKey)
                .apply(RequestOptions.centerCropTransform())
                .into(this.placePhoto);
        displayRatingStars(place.getPlaceRate());
        this.configureButtons();
    }

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
                // TODO: 13/11/2018 implement like function on click
            }
        });
    }

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

    private boolean isTelephonyEnabled() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

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
    private void firebaseListener() {
        RestaurantHelper.getRestaurantsCollection().addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) return;

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    List<String> tmp = documentSnapshot.toObject(Restaurant.class).getUserList();
                    switch (dc.getType()) {
                        case ADDED:
                            Log.i(TAG, "onEvent: added");
                            userList.clear();
                            for (String userId : tmp) {
                                userList.add(getUserDataFromId(userId));
                            }
                            joiningMatesAdapter.setUserList(userList);
                            break;
                        case REMOVED:
                            Log.i(TAG, "onEvent: removed");
                            for (String userId : tmp) {
                                    userList.remove(getUserDataFromId(userId));
                            }
                            joiningMatesAdapter.setUserList(userList);
                            break;
                        case MODIFIED:
                            Log.i(TAG, "onEvent: modified");
                            Log.i(TAG, "onEvent: size = " + tmp.size());
                            userList.clear();
                            for (String userId : tmp) {

                                userList.add(getUserDataFromId(userId));

                            }
                            joiningMatesAdapter.setUserList(userList);
                            break;
                    }
                }
            }
        });

    }

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
                    RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId()).update("userList", FieldValue.arrayUnion(currentUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void removeUserFromRestaurant() {
        Log.i(TAG, "removeUserFromRestaurant: " + currentUser.getUsername());

        RestaurantHelper.getRestaurant(currentUser.getChosenRestaurant()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    final Restaurant restaurant = task.getResult().toObject(Restaurant.class);
                    RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId()).update("userList", FieldValue.arrayRemove(currentUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), getCurrentUser().getDisplayName() + " remove from " + restaurant.getPlaceFormater().getPlaceName(), Toast.LENGTH_SHORT).show();
                            joiningMatesAdapter.setUserList(userList);
                        }
                    });
                }
            }
        });

    }

    private void addRestaurantToUser() {
        UserHelper.updateChosenRestaurant(place.getId(), getCurrentUser().getUid()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "an error has occurred " + e, Toast.LENGTH_SHORT).show();

            }
        });
    }
    //****************************
    //****** RECYCLE VIEW ********
    //****************************

    public void configureRecycleView() {
        this.provideRecycleViewWithData();
        this.joiningMatesAdapter = new JoiningMatesAdapter(this.userList, Glide.with(this));
        this.recyclerView.setAdapter(joiningMatesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

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
                    for (String userId : restaurant.getUserList()) {
                        userList.add(getUserDataFromId(userId));
                    }
                }
            }
        });

    }

}
