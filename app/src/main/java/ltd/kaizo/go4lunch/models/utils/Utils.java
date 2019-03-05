package ltd.kaizo.go4lunch.models.utils;

import android.location.Location;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import static ltd.kaizo.go4lunch.controller.fragment.MapFragment.DEFAULT_LOCATION;

public class Utils {
    /**
     * Format location to string string.
     *
     * @return the string
     */
    public static String formatLocationToString(Location currentLocation) {
        if (currentLocation != null) {
            return currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        } else {
            return DEFAULT_LOCATION.latitude + "," + DEFAULT_LOCATION.longitude;
        }
    }

    /**
     * Show snack bar.
     *
     * @param coordinatorLayout the coordinator layout
     * @param message           the message
     */
    public static void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

}
