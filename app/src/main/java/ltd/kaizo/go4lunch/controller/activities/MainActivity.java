package ltd.kaizo.go4lunch.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.fragment.ListFragment;
import ltd.kaizo.go4lunch.controller.fragment.MapFragment;
import ltd.kaizo.go4lunch.controller.fragment.MatesFragment;

public class MainActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 123;
    @BindView(R.id.activity_main_constraint_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        }
        this.configureAndShowMapFragment();
        this.configureBottomNavigationView();
    }

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
                        .setLogo(R.drawable.restau_icon)
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