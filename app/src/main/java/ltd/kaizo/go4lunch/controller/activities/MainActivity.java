package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.fragment.ListFragment;
import ltd.kaizo.go4lunch.controller.fragment.MapFragment;
import ltd.kaizo.go4lunch.controller.fragment.MatesFragment;
import ltd.kaizo.go4lunch.models.API.UserHelper;

import static ltd.kaizo.go4lunch.controller.fragment.MapFragment.DEFAULT_ZOOM;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int RC_SIGN_IN = 123;
    private static final int SIGN_OUT_TASK = 10;
    @BindView(R.id.activity_main_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;
    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    TextView usernameTextview;
    TextView emailTextview;
    ImageView avatarImageView;
    private String TAG = "MainActivity";
    private MapFragment mapFragment;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void configureDesign() {

        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        } else {
            this.configureBottomNavigationView();
            this.configureAndShowMapFragment();
            this.configureToolbar();
            this.configureDrawerLayout();
            this.configureNavigationView();
            this.updateNavHeaderDesign();
        }

    }

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
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                Log.e(TAG, "onOptionsItemSelected: GooglePlayServicesRepairableException " + e);
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e(TAG, "onOptionsItemSelected: GooglePlayServicesNotAvailableException " + e);
            }

        }

        return true;
    }

    //****************************
    //****  NAVIGATION DRAWER ****
    //****************************
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_lunch:
                Toast.makeText(this, "lunch", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_main_drawer_settings:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();

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

    @Override

    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    //****************************
    //*******   DESIGN   *********
    //****************************

    private void configureAndShowMapFragment() {
        this.mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, this.mapFragment)
                .commit();
    }

    private void configureAndShowListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, new ListFragment())
                .commit();
    }

    private void configureAndShowWorkmatesFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, new MatesFragment())
                .commit();

    }

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

    private void updateNavHeaderDesign() {
        usernameTextview = navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        emailTextview = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        avatarImageView = navigationView.getHeaderView(0).findViewById(R.id.nav_header_avatar);
        String username;
        String email;
        String avatarUrl;
        if (TextUtils.isEmpty(getCurrentUser().getDisplayName())) {
            username = "no username found";
        } else {
            username = getCurrentUser().getDisplayName();
        }
        if (TextUtils.isEmpty(getCurrentUser().getEmail())) {
            email = "no email found";
        } else {
            email = getCurrentUser().getEmail();
        }
        if (TextUtils.isEmpty(getCurrentUser().getDisplayName())) {
            avatarUrl = "";
        } else {
            avatarUrl = getCurrentUser().getPhotoUrl().toString();
        }
        Log.i(TAG, "updateNavHeaderDesign: username = " + username + "\n" +
                "email = " + email + "\n" +
                "avatarUrl = " + avatarUrl);
        //TODO voir pour mettre les infos lors de la 1ere connection
        this.usernameTextview.setText(username);
        this.emailTextview.setText(email);
        Glide.with(this)
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImageView);

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

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.food)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

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

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {//SUCCESS
            if (this.getCurrentUser() != null) {
                UserHelper.createUser(this.getCurrentUser().getUid(), this.getCurrentUser().getDisplayName(), this.getCurrentUser().getPhotoUrl().toString()).addOnFailureListener(this.onFailureListener());
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

    private void handlePlaceAutoCompleteResponse(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            Log.i(TAG, "PlaceApiData: " + place.getName() + " lat/long = " + place.getLatLng());
            this.updateMapWithPlace(place.getLatLng());
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
            Log.i(TAG, status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            Log.i(TAG, "handlePlaceAutoCompleteResponse: cancel");
            finish();
            startActivity(getIntent());
        }


    }

    private void updateMapWithPlace(LatLng latLng) {
        this.mapFragment.moveCamera(latLng, DEFAULT_ZOOM);
    }
}