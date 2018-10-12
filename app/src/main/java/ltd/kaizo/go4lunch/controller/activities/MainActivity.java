package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.fragment.ListFragment;
import ltd.kaizo.go4lunch.controller.fragment.MapFragment;
import ltd.kaizo.go4lunch.controller.fragment.MatesFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RC_SIGN_IN = 123;
    @BindView(R.id.activity_main_constraint_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;
    TextView usernameTextview;
    TextView emailTextview;
    ImageView avatarImageView;


    private void configureAndShowMapFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment_container, new MapFragment())
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

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void configureDesign() {

        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        } else {

            this.configureAndShowMapFragment();
            this.configureBottomNavigationView();
            this.configureDrawerLayout();
            this.configureNavigationView();
            this.updateNavHeaderDesign();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.activity_main_searchview).getActionView();
        searchView.setQueryHint(getString(R.string.search_restaurants));
        configureSearchView(searchView);
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
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();

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

    private void configureSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showSnackBar(constraintLayout, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    //****************************
    //*******   DESIGN   *********
    //****************************


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
            avatarUrl = getCurrentUser().getDisplayName();
        }
        //TODO voir pour mettre les infos lors de la 1ere connection
        this.usernameTextview.setText(username);
        this.emailTextview.setText(email);
        Glide.with(this)
                .load(avatarUrl)
                .into(avatarImageView);

    }

    //****************************
    //*******   FIREBASE   *******
    //****************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.handleResponseAfterSignIn(requestCode, resultCode, data);
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

    private void showSnackBar(ConstraintLayout constraintLayout, String message) {
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {//SUCCESS
//                this.createUserInFirestore();
                showSnackBar(this.constraintLayout, getString(R.string.connection_succeed));
            } else {//ERROR
                if (response == null) {
                    showSnackBar(this.constraintLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.constraintLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.constraintLayout, getString(R.string.error_unknown_error));

                }
            }


        }
    }
}