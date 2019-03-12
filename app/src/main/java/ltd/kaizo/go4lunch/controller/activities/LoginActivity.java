package ltd.kaizo.go4lunch.controller.activities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import butterknife.BindView;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.UserHelper;
import timber.log.Timber;

import static ltd.kaizo.go4lunch.R.string.error_unknown_error;
import static ltd.kaizo.go4lunch.models.utils.Utils.configureCurrentLocation;
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
     * The constant FINE_LOCATION.
     */
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    /**
     * The constant COARSE_LOCATION.
     */
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    /**
     * The constant LOCATION_PERMISSION_REQUEST_CODE.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    /**
     * The constant ERROR_DIALOG_REQUEST.
     */
    private static final int ERROR_DIALOG_REQUEST = 9001;
    /**
     * The Coordinator layout.
     */
    @BindView(R.id.activity_login_coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    /**
     * The Location permissions granted.
     */
    private Boolean locationPermissionsGranted = false;
    /**
     * The Current location.
     */
    private Location currentLocation;
    /**
     * The Fused location provider client.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void configureDesign() {
        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        } else {
            this.configurePermission();
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
    //****************************
    //*******  PERMISSIONS *******
    //****************************

    /**
     * Configure permission.
     */
    private void configurePermission() {
        if (isServiceOK()) {
            Timber.i("service OK : %s", isServiceOK());
            this.getLocationPermission();
            Timber.i("locationpermision = %s", locationPermissionsGranted);
            // Construct a FusedLocationProviderClient.
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            this.getDeviceLocation();
        }
    }

    /**
     * Gets location permission.
     */
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionsGranted = true;
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {
                        getDeviceLocation();
                    }
                }
        }
    }

    /**
     * show an alert dialog asking to enable GPS
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage(R.string.gps_disabled)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        showSnackBar(coordinatorLayout, getString(R.string.no_gps_no_app));
                        try {
                            Thread.sleep(2);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            finish();
                        }

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * check if the Google Play services are available to make map request
     *
     * @return Boolean boolean
     */
    private boolean isServiceOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
        } else {
            showSnackBar(coordinatorLayout, "You can't make map request");
        }
        return false;
    }
    /****************************
     ********   LOCATION  ********
     *****************************/
    /**
     * Gets device location.
     */
    private void getDeviceLocation() {
        try {
            if (locationPermissionsGranted) {

                final Task location = this.fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            currentLocation = (Location) task.getResult();
                            configureCurrentLocation(currentLocation);
                            Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                            mainActivity.putExtra("LOCATION", currentLocation);
                            startActivity(mainActivity);
                            LoginActivity.this.finish();
                        } else {
                            showSnackBar(coordinatorLayout, getString(R.string.unable_get_location));
                        }
                    }
                });
            }else {
                showSnackBar(coordinatorLayout, getString(R.string.unable_get_location));
            }

        } catch (SecurityException e) {
            Timber.e("security exception : %s", e.getMessage());
            showSnackBar(coordinatorLayout, getString(R.string.unable_get_location));
        }
    }
}
