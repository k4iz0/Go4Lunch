package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evernote.android.job.JobManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.fragment.ListFragment;
import ltd.kaizo.go4lunch.controller.fragment.MapFragment;
import ltd.kaizo.go4lunch.controller.fragment.MatesFragment;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.models.utils.androidJob.AndroidJobCreator;
import ltd.kaizo.go4lunch.models.utils.androidJob.ResetUserChoiceJob;
import ltd.kaizo.go4lunch.views.Adapter.PlaceAutoCompleteArrayAdapter;
import timber.log.Timber;

import static ltd.kaizo.go4lunch.controller.fragment.MapFragment.DEFAULT_ZOOM;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantListFromSharedPreferences;

/**
 * The type Main activity.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * The constant PLACE_AUTOCOMPLETE_REQUEST_CODE.
     */
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    /**
     * The constant RC_SIGN_IN.
     */
    private static final int RC_SIGN_IN = 123;
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
     * The Chosen restaurant id.
     */
    private String chosenRestaurantId = "";
    /**
     * The Current user.
     */
    private User currentUser;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void configureDesign() {
        // if user's logged in show the map, otherwise show identification page
        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        } else {
            this.configureCurrentUser();
            this.configureBottomNavigationView();
            this.configureAndShowMapFragment();
            this.configureToolbar();
            this.configureNavigationView();
            this.configureDrawerLayout();
            this.configureAutoCompleteFocus();
            this.configureAndroidJob();
        }
    }

    private void configureAndroidJob() {
        Timber.i("create task job");
        JobManager.create(getApplicationContext()).addJobCreator(new AndroidJobCreator());
       ResetUserChoiceJob.schedulePeriodic();
    }

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
    //****************************
    //*******   TOOLBAR   ********
    //****************************

    /**
     * Configure toolbar.
     */
    private void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_main_searchview) {
            if (autocompleteLayout.getVisibility() == View.GONE) {
                this.updateAutoCompleteDesign();
                final ArrayList<PlaceFormater> placeFormaterList = new ArrayList<>();
                if (getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY) != null) {
                    placeFormaterList.clear();
                    placeFormaterList.addAll(getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY));
                }
                PlaceAutoCompleteArrayAdapter adapter;
                adapter = new PlaceAutoCompleteArrayAdapter(this, placeFormaterList);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
                        detailActivity.putExtra("PlaceFormater", placeFormaterList.get(position));
                        startActivity(detailActivity);
                    }
                });
            } else {
                this.updateAutoCompleteDesign();

            }
        }
        return true;
    }

    //****************************
    //****  NAVIGATION DRAWER ****
    //****************************

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
            username = "no username found";
        } else {
            username = currentUser.getUsername();
        }
        if (TextUtils.isEmpty(currentUser.getEmail())) {
            email = "no email found";
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
                                        Timber.i("onComplete: chosenrestaurant " + chosenRestaurant.getPlaceFormater().getPlaceName());
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


    //****************************
    //*******   DESIGN   *********
    //****************************

    /**
     * Configure auto complete focus.
     */
    private void configureAutoCompleteFocus() {
        autocompleteLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                updateAutoCompleteDesign();

            }
        });
    }

    /**
     * Update auto complete design.
     */
    private void updateAutoCompleteDesign() {
        if (autocompleteLayout.getVisibility() == View.VISIBLE) {
            autocompleteLayout.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        } else {
            autocompleteLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Configure navigation view.
     */
    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    /**
     * Configure and show map fragment.
     */
    private void configureAndShowMapFragment() {
        this.mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, this.mapFragment)
                .commit();
    }

    /**
     * Configure and show list fragment.
     */
    private void configureAndShowListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, new ListFragment())
                .commit();
    }

    /**
     * Configure and show workmates fragment.
     */
    private void configureAndShowWorkmatesFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, new MatesFragment())
                .commit();

    }

    /**
     * Configure bottom navigation view.
     */
    private void configureBottomNavigationView() {
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


    /**
     * Show snack bar.
     *
     * @param coordinatorLayout the coordinator layout
     * @param message           the message
     */
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    //****************************
    //*******   FIREBASE   *******
    //****************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                this.handleResponseAfterSignIn(resultCode, data);
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                this.handlePlaceAutoCompleteResponse(resultCode, data);
                break;

        }
    }

    /**
     * Start sign in activity.
     */
    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_home)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

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
                    finish();
                    startActivity(getIntent());
                }
            }
        };
    }

    /**
     * Handle response after sign in.
     *
     * @param resultCode the result code
     * @param data       the data
     */
    private void handleResponseAfterSignIn(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {//SUCCESS
            if (this.getCurrentUser() != null) {
                if (this.getCurrentUser().getPhotoUrl() == null) {
                    //email & password account no picture
                    UserHelper.createUser(this.getCurrentUser().getUid(), this.getCurrentUser().getDisplayName(), "", this.getCurrentUser().getEmail()).addOnFailureListener(this.onFailureListener());
                } else {
                    UserHelper.createUser(this.getCurrentUser().getUid(), this.getCurrentUser().getDisplayName(), this.getCurrentUser().getPhotoUrl().toString(), this.getCurrentUser().getEmail()).addOnFailureListener(this.onFailureListener());
                }
            }
            showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            this.configureDesign();

        } else {//ERROR
            if (response == null) {
                showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
            } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));

            }
        }


    }

    /**
     * Handle place auto complete response.
     *
     * @param resultCode the result code
     * @param data       the data
     */
    private void handlePlaceAutoCompleteResponse(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            Timber.i("PlaceApiData: " + place.getName() + " lat/long = " + place.getLatLng());
            this.updateMapWithPlace(place.getLatLng());
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
            Timber.i(status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            Timber.i("handlePlaceAutoCompleteResponse: cancel");
            finish();
            startActivity(getIntent());
        }


    }

    /**
     * Update map with place.
     *
     * @param latLng the lat lng
     */
    private void updateMapWithPlace(LatLng latLng) {
        this.mapFragment.moveCamera(latLng, DEFAULT_ZOOM);
    }
}