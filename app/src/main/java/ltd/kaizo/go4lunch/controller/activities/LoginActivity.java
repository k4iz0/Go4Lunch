package ltd.kaizo.go4lunch.controller.activities;


import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.UserHelper;

import static ltd.kaizo.go4lunch.R.string.error_unknown_error;
import static ltd.kaizo.go4lunch.models.utils.Utils.showSnackBar;

/**
 * The type Login activity.
 */
public class LoginActivity extends BaseActivity {
    /**
     * The constant RC_SIGN_IN.
     */
    private static final int RC_SIGN_IN = 123;
    /**
     * The Coordinator layout.
     */
    @BindView(R.id.activity_login_coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void configureDesign() {
        // if user's logged in show the map, otherwise show identification page
        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        } else {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
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
                showSnackBar(this.coordinatorLayout, getString(error_unknown_error));

            }
        }
    }
}
