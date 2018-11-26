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
import java.util.Map;

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
    private String TAG = getClass().getSimpleName();
    private User currentUser;
    private Boolean isFabPressed = false;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public void configureDesign() {
        this.getPlaceFormaterFromIntent();
        this.configureCurrentUser();
        this.configureRecycleView();
        this.firebaseListener();
    }

    private void configureCurrentUser() {
        UserHelper.getUser(getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    currentUser = task.getResult().toObject(User.class);
                    updateUiWithPlaceData();
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
        if (!currentUser.getChosenRestaurant().equalsIgnoreCase("")) {

            RestaurantHelper.getRestaurant(currentUser.getChosenRestaurant()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);

                            for (User user : restaurant.getUserList()) {
                                if (currentUser.getUid().equalsIgnoreCase(user.getUid())) {
                                    isFabPressed = true;
                                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check));
                                    Log.i(TAG, "onSuccess: isFabPressed = " + isFabPressed);
                                    return;
                                }
                            }

                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.i(TAG, "onSuccess: isFabPressed2 = " + isFabPressed);
                    if (isFabPressed) {
                        floatingActionButton.setBackgroundResource(R.drawable.ic_check);
                    }
                }
            });
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
                    ArrayList<HashMap<String, String>> tmp = (ArrayList<HashMap<String, String>>) documentSnapshot.get("userList");
                    userList.clear();
                    switch (dc.getType()) {
                        case ADDED:
                            Log.i(TAG, "onEvent: added");
                            for (HashMap<String, String> user : tmp) {
                                userList.add(new User(user.get("uid"), user.get("username"), user.get("urlPicture")));
                            }
                            joiningMatesAdapter.setUserList(userList);
                            break;
                        case REMOVED:
                            Log.i(TAG, "onEvent: removed");

                            for (HashMap<String, String> user : tmp) {
                                userList.remove(user.get("userId"));
                            }
                            joiningMatesAdapter.setUserList(userList);
                            break;
                        case MODIFIED:
                            Log.i(TAG, "onEvent: modified");
                            Log.i(TAG, "onEvent: size = " + documentSnapshot.get("userList").toString());


                            for (HashMap<String, String> user : tmp) {
                                userList.add(new User(user.get("uid"), user.get("username"), user.get("urlPicture")));
                            }
                            joiningMatesAdapter.setUserList(userList);
                            break;
                    }
                }
            }
        });

    }

    private void addUserToRestaurant() {
        this.removeUserFromRestaurant();
        Log.i("detailActivity", "addUserToRestaurant: user = " + getCurrentUser().getUid() + " and placeId = " + place.getId());
        RestaurantHelper.getRestaurant(place.getId()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "an error has occurred " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(getApplicationContext(), getCurrentUser().getDisplayName() + " has choose " + place.getPlaceName(), Toast.LENGTH_SHORT).show();
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);

                userList.addAll(restaurant.getUserList());


            }

        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userList.add(currentUser);
                Log.i(TAG, "onComplete: userlist = " + userList.toString() + " size = " + userList.size());
                Map<String, Object> userlistMap = new HashMap<>();
                userlistMap.put("userList", FieldValue.arrayUnion(currentUser.getUid()));
                RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId()).update(userlistMap);
                provideRecycleViewWithData();
            }
        });
        Log.i("JoiningMatesAdapter", "provideRecycleViewWithData: taille de la liste " + this.userList.size());
    }

    private void removeUserFromRestaurant() {
        Log.i(TAG, "removeUserFromRestaurant: " + currentUser.getUsername());
        if (!currentUser.getChosenRestaurant().equalsIgnoreCase("")) {

            RestaurantHelper.getRestaurant(currentUser.getChosenRestaurant()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                        restaurant.getUserList().remove(currentUser);
                        userList.remove(currentUser);
                        Map<String, Object> removeUserMap = new HashMap<>();
                        removeUserMap.put("userList", FieldValue.arrayRemove(currentUser.getUid()));
                        RestaurantHelper.getRestaurantsCollection().document(restaurant.getPlaceId()).update(removeUserMap);
                    }

                }
            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    joiningMatesAdapter.notifyDataSetChanged();
                }
            });
        }
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

        RestaurantHelper.getRestaurant(place.getId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                restaurant = documentSnapshot.toObject(Restaurant.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: fail to retrieve restaurant info");
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (restaurant != null) {
                    userList.addAll(restaurant.getUserList());
                }
                joiningMatesAdapter.notifyDataSetChanged();
            }
        });


    }

}
