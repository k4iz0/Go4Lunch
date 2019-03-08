package ltd.kaizo.go4lunch.models.utils;

import android.location.Location;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import static ltd.kaizo.go4lunch.controller.fragment.MapFragment.DEFAULT_LOCATION;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LATITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LONGITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.write;

/**
 * The type Utils.
 */
public class Utils {

    /**
     * Configure current location .
     *
     * @param currentLocation the current location
     * @return the boolean
     */
    public  static void configureCurrentLocation(Location currentLocation) {
        Double previousLocationLat = read(CURRENT_LATITUDE_KEY, 0.0);
        Double previousLocationLng = read(CURRENT_LONGITUDE_KEY, 0.0);
        if (previousLocationLat == 0.0 || previousLocationLat != (currentLocation.getLatitude())) {
            write(CURRENT_LATITUDE_KEY, currentLocation.getLatitude());
        }
        if (previousLocationLng == 0.0 || previousLocationLng != (currentLocation.getLongitude())) {
            write(CURRENT_LONGITUDE_KEY, currentLocation.getLongitude());
        }
    }

    /**
     * Format location to string string.
     *
     * @param currentLocation the current location
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
