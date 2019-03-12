package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evernote.android.job.JobManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.fragment.ListFragment;
import ltd.kaizo.go4lunch.controller.fragment.MapFragment;
import ltd.kaizo.go4lunch.controller.fragment.MatesFragment;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.API.nearbySearch.PlaceApiData;
import ltd.kaizo.go4lunch.models.API.nearbySearch.Result;
import ltd.kaizo.go4lunch.models.API.placeDetail.PlaceDetailApiData;
import ltd.kaizo.go4lunch.models.API.stream.PlaceStream;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.models.utils.androidJob.AndroidJobCreator;
import ltd.kaizo.go4lunch.models.utils.androidJob.ResetUserChoiceJob;
import ltd.kaizo.go4lunch.views.adapter.PlaceAutocompleteAdapter;
import timber.log.Timber;

import static ltd.kaizo.go4lunch.R.string.error_unknown_error;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RADIUS_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_AROUND_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_DETAIL_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;
import static ltd.kaizo.go4lunch.models.utils.Utils.formatLocationToString;
import static ltd.kaizo.go4lunch.models.utils.Utils.showSnackBar;

/**
 * The type Main activity.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    /**
     * The constant SIGN_OUT_TASK.
     */
    private static final int SIGN_OUT_TASK = 10;
    /**
     * The Coordinator layout.
     */
    @BindView(R.id.activity_main_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    /**
     * The Bottom navigation view.
     */
    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    /**
     * The Drawer layout.
     */
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;
    /**
     * The progress bar
     */
    @BindView(R.id.activity_main_progress_bar)
    ProgressBar progressBar;
    /**
     * The Navigation view.
     */
    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;
    /**
     * The Toolbar.
     */
    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    /**
     * The Auto complete text view.
     */
    @BindView(R.id.autocomplete_textview)
    AutoCompleteTextView autoCompleteTextView;
    /**
     * The Autocomplete layout.
     */
    @BindView(R.id.autocomplete_textview_constraint_layout)
    ConstraintLayout autocompleteLayout;
    /**
     * The Username textview.
     */
    TextView usernameTextview;
    /**
     * The Email textview.
     */
    TextView emailTextview;
    /**
     * The Avatar image view.
     */
    ImageView avatarImageView;
    /**
     * The Map fragment.
     */
    private MapFragment mapFragment;
    /**
     * The List fragment.
     */
    private ListFragment listFragment;
    /**
     * The Chosen restaurant id.
     */
    private String chosenRestaurantId = "";
    /**
     * The Current user.
     */
    private User currentUser;
    /**
     * The Place temp list.
     */
    private ArrayList<PlaceFormater> placeAroundList = new ArrayList<>();
    /**
     * The Place detail list.
     */
    private ArrayList<PlaceFormater> placeDetailList = new ArrayList<>();
    /**
     * The Place detail list.
     */
    private ArrayList<String> restauranIdList = new ArrayList<>();
    /**
     * The Disposable.
     */
    private Disposable disposable;
    /**
     * The Current location.
     */
    private Location currentLocation;

    /**
     * The Bounds.
     */
    private LatLngBounds bounds;
    /**
     * The Adapter.
     */
    private PlaceAutocompleteAdapter adapter;
    /**
     * The Radius.
     */
    private String radius;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void configureDesign() {
        this.getDataFromIntent();
        this.launchStreamRequest();
        this.configureCurrentUser();
        this.configureToolbarAndProgressBar();
        this.configureNavigationView();
        this.configureDrawerLayout();
        this.configureAndroidJob();
    }

    private void launchStreamRequest() {
        if (currentLocation != null) {
            this.streamFetchNearbyRestaurant();
            this.streamFetchNearbyRestaurantAndGetPlaceDetail();
        } else {
            showSnackBar(coordinatorLayout, getString(R.string.unable_get_location));
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        currentLocation = intent.getParcelableExtra("LOCATION");
        //radius
        radius = read(RADIUS_KEY, "1500");
    }


    /**
     * Configure android job.
     */
    private void configureAndroidJob() {
        Timber.i("create task job");
        JobManager.create(getApplicationContext()).addJobCreator(new AndroidJobCreator());
        ResetUserChoiceJob.schedulePeriodic();
    }

    //****************************
    //*******   TOOLBAR   ********
    //****************************

    /**
     * Configure toolbar and progressbar
     */
    private void configureToolbarAndProgressBar() {
        //toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);
        //progressBar color
        progressBar.getIndeterminateDrawable().setColorFilter(
                Color.parseColor("#008577"), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    /****************************
     ********   FRAGMENT  ********
     *****************************/

    /**
     * Configure and show map fragment.
     */
    private void configureAndShowMapFragment() {
        this.mapFragment = new MapFragment();
        mapFragment.setArguments(saveDataToBundle());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, this.mapFragment)
                .commit();
    }

    /**
     * Configure and show list fragment.
     */
    private void configureAndShowListFragment() {
        listFragment = new ListFragment();
        listFragment.setArguments(saveDataToBundle());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, listFragment)
                .commit();
    }

    /**
     * Configure and show workmates fragment.
     */
    private void configureAndShowWorkmatesFragment() {
        MatesFragment matesFragment = new MatesFragment();
        matesFragment.setArguments(saveDataToBundle());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, matesFragment)
                .commit();
    }

    /**
     * Save data to bundle bundle.
     *
     * @return the bundle
     */
    private Bundle saveDataToBundle() {
        Bundle args = new Bundle();
        args.putParcelableArrayList(RESTAURANT_LIST_DETAIL_KEY, placeDetailList);
        args.putParcelableArrayList(RESTAURANT_LIST_AROUND_KEY, placeAroundList);
        return args;
    }

    //****************************
    //*******   DESIGN   *********
    //****************************

    /**
     * Update auto complete design.
     */
    private void updateAutoCompleteDesign() {
        if (autocompleteLayout.getVisibility() == View.VISIBLE) {
            autocompleteLayout.setVisibility(View.GONE);
            autoCompleteTextView.setText("");

            toolbar.setVisibility(View.VISIBLE);
            if (placeDetailList != null) {
                this.updateListAndMarker(placeDetailList);
            }
        } else {
            autocompleteLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Configure bottom navigation view.
     */
    private void configureBottomNavigationView() {
        progressBar.setVisibility(View.INVISIBLE);
        configureAndShowMapFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bottom_navigation_item_mapview:
                        configureAndShowMapFragment();
                        break;
                    case R.id.bottom_navigation_item_list:
                        configureAndShowListFragment();
                        break;
                    case R.id.bottom_navigation_item_workmates:
                        configureAndShowWorkmatesFragment();
                        break;
                }
                return true;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_main_searchview) {
            this.updateAutoCompleteDesign();
        }
        return true;
    }

    /****************************
     *******  AUTOCOMPLETE  ******
     *****************************/

    /**
     * Configure autocomplete.
     */
    private void configureAutocomplete() {

        GoogleApiClient geoDataClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        LatLng position = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        bounds = new LatLngBounds(position, position);
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
        this.adapter = new PlaceAutocompleteAdapter(this, geoDataClient, bounds, filter, placeDetailList, restauranIdList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setDropDownHeight(0);
    }

    /**
     * Update list and marker.
     *
     * @param restaurantList the restaurant list
     */
    public void updateListAndMarker(ArrayList<PlaceFormater> restaurantList) {
        if (listFragment != null) {
            listFragment.updateUI(restaurantList);
        }
        if (mapFragment != null) {
            mapFragment.configureListAndMarker(restaurantList);
        }
    }
    //****************************
    //****  NAVIGATION DRAWER ****
    //****************************

    /**
     * Configure navigation view.
     */
    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    /**
     * Configure drawer layout.
     */
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //refresh user info when open
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                configureCurrentUser();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_lunch:
                this.getChosenRestaurant();
                break;
            case R.id.activity_main_drawer_settings:
                Intent settingActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingActivity);
                break;
            case R.id.activity_main_drawer_logout:
                this.signOutUserFromFirebase();
                showSnackBar(coordinatorLayout, getString(R.string.user_logout));
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Update nav header design.
     */
    private void updateNavHeaderDesign() {
        usernameTextview = navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        emailTextview = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        avatarImageView = navigationView.getHeaderView(0).findViewById(R.id.nav_header_avatar);
        String username;
        String email;
        String avatarUrl;
        if (TextUtils.isEmpty(currentUser.getUsername())) {
            username = getString(R.string.no_username);
        } else {
            username = currentUser.getUsername();
        }
        if (TextUtils.isEmpty(currentUser.getEmail())) {
            email = getString(R.string.no_email);
        } else {
            email = currentUser.getEmail();
        }
        if (TextUtils.isEmpty(currentUser.getUrlPicture())) {
            avatarUrl = "";
        } else {
            avatarUrl = currentUser.getUrlPicture();
        }
        this.usernameTextview.setText(username);
        this.emailTextview.setText(email);
        Glide.with(this)
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImageView);

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (autocompleteLayout.getVisibility() == View.VISIBLE) {
            this.updateAutoCompleteDesign();
        } else {
            super.onBackPressed();
        }
    }
    /****************************
     *******   FIRESTORE   *******
     *****************************/

    /**
     * Configure current user.
     */
    private void configureCurrentUser() {
        UserHelper.getUser(getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    currentUser = task.getResult().toObject(User.class);
                    updateNavHeaderDesign();
                }
            }
        });
    }

    /**
     * Get chosen restaurant.
     */
    private void getChosenRestaurant() {
        UserHelper.getUser(getCurrentUser().getUid()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar(coordinatorLayout, "Favorite restaurant not found !");
                chosenRestaurantId = "";
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (task.getResult().getData() != null) {
                        chosenRestaurantId = task.getResult().toObject(User.class).getChosenRestaurant();
                        if (!chosenRestaurantId.equalsIgnoreCase("")) {
                            RestaurantHelper.getRestaurant(chosenRestaurantId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        Restaurant chosenRestaurant = task.getResult().toObject(Restaurant.class);
                                        Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
                                        detailActivity.putExtra("PlaceFormater", chosenRestaurant.getPlaceFormater());
                                        startActivity(detailActivity);
                                    }
                                }
                            });
                        } else {
                            showSnackBar(coordinatorLayout, getString(R.string.no_choice));
                        }
                    }
                }
            }
        });
    }


    //****************************
    //******  DATA STREAMS *******
    //****************************

    /**
     * Execute stream fetch nearby restaurant
     */
    private void streamFetchNearbyRestaurant() {
        this.disposable = PlaceStream.INSTANCE.streamFetchNearbyRestaurant(formatLocationToString(currentLocation), radius)
                .subscribeWith(new DisposableObserver<PlaceApiData>() {
                    @Override
                    public void onNext(PlaceApiData placeApiData) {
                        if (placeApiData != null) {
                            for (Result result : placeApiData.getResults()) {
                                restauranIdList.add(result.getPlaceId());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showSnackBar(coordinatorLayout, getString(R.string.error_occurred));
                    }

                    @Override
                    public void onComplete() {
                        configureAutocomplete();
                    }
                });

    }

    /**
     * Execute stream fetch nearby restaurant and get place detail.
     */
    private void streamFetchNearbyRestaurantAndGetPlaceDetail() {
        progressBar.setVisibility(View.VISIBLE);
        this.disposable = PlaceStream.INSTANCE.streamFetchNearbyRestaurantAndGetPlaceDetail(formatLocationToString(currentLocation), radius)
                .subscribeWith(new DisposableObserver<PlaceDetailApiData>() {

                    @Override
                    public void onNext(PlaceDetailApiData placeDetailApiData) {
                        if (placeDetailApiData != null) {
                            PlaceFormater place = new PlaceFormater(placeDetailApiData.getResult(), currentLocation);
                            Timber.i("adding to placeAround %s", place.getPlaceName());
                            placeAroundList.add(place);
                        } else {
                            showSnackBar(coordinatorLayout, getString(R.string.no_place_found));
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i(getString(error_unknown_error) + " " + e);
                        showSnackBar(coordinatorLayout, getString(R.string.error_occurred));
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        //add to firestore if not already in
                        for (final PlaceFormater place : placeAroundList) {
                            RestaurantHelper.getRestaurant(place.getId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult() != null) {
                                        if (task.getResult().exists()) {
                                            //add place from Firestore to list
                                            addPlaceFromFirestore(task);
                                        } else {
                                            //add to firestore
                                            addRestaurantToFirestore(place);
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
    }

    private void addRestaurantToFirestore(final PlaceFormater place) {
        RestaurantHelper.createRestaurant(place.getId(), place).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    placeDetailList.add(place);
                    if (placeAroundList.size() == placeDetailList.size()) {
                        configureBottomNavigationView();
                    }
                }
            }
        });
    }

    private void addPlaceFromFirestore(@NonNull Task<DocumentSnapshot> task) {
        Restaurant restaurant = task.getResult().toObject(Restaurant.class);
        if (restaurant != null) {
            placeDetailList.add(restaurant.getPlaceFormater());
        }
        if (placeAroundList.size() == placeDetailList.size()) {
            configureBottomNavigationView();
        }
    }

    /****************************
     *********   LOGOUT   ********
     *****************************/

    /**
     * Sign out user from firebase.
     */
    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    /**
     * Update ui after rest requests completed on success listener.
     *
     * @param origin the origin
     * @return the on success listener
     */
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {

        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (origin == SIGN_OUT_TASK) {
                    Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                }
            }
        };
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}