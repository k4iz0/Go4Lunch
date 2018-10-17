package ltd.kaizo.go4lunch.controller.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.PlaceApiData;
import ltd.kaizo.go4lunch.models.API.Result;
import ltd.kaizo.go4lunch.models.PlaceApiDataConverter;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;
import ltd.kaizo.go4lunch.models.utils.PlaceStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, LocationListener {
    public static final float DEFAULT_ZOOM = 15f;
    static final LatLng DEFAULT_LOCATION = new LatLng(48.858093, 2.294694); //PARIS
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private MapView mapView;
    private FloatingActionButton floatingActionButton;
    private GoogleMap googleMap;
    private Boolean locationPermissionsGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String TAG = getClass().getSimpleName();
    private Location currentLocation;
    private PlaceDetectionClient placeDetectionClient;
    private GeoDataClient geoDataClient;
    private Disposable disposable;

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    protected void configureDesign() {

    }

    @Override
    protected void updateDesign() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.fragment_map_mapview);
        floatingActionButton = rootView.findViewById(R.id.fragment_map_fab);
        mapView.onCreate(savedInstanceState);
        this.configureGoogleMap();
        mapView.onResume();
        return rootView;

    }

    //****************************
    //*******  GOOGLE MAP ********
    //****************************

    private void configureGoogleMap() {
        if (isServiceOK()) {
            this.getLocationPermission();
            this.configureFloatingButton();
            // Construct a GeoDataClient.
            this.geoDataClient = Places.getGeoDataClient(getContext());

            // Construct a PlaceDetectionClient.
            this.placeDetectionClient = Places.getPlaceDetectionClient(getContext());

            // Construct a FusedLocationProviderClient.
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        }
    }

    private void configureFloatingButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToCurrentLocation(currentLocation);
            }
        });
    }

    private void iniMap() {
        mapView.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.d("googleMap", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted = true;
                this.iniMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionsGranted = true;
                }
        }

    }

    private void updateLocationUI() {
        if (this.googleMap == null) {
            return;
        }
        try {
            if (locationPermissionsGranted) {
                this.googleMap.setMyLocationEnabled(true);
                this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                this.googleMap.setMyLocationEnabled(false);
                this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("update location error ", e.getMessage());
        }

    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionsGranted) {
                Task location = this.fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: found location !");
                            currentLocation = (Location) task.getResult();
                            moveCameraToCurrentLocation(currentLocation);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("getDeviceLocation", "security exception : " + e.getMessage());
        }
    }

    private void moveCameraToCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        if (this.currentLocation != null) {
            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
            this.executeStreamFetchNearbyRestaurant();
        } else {
            Toast.makeText(getContext(), "Unable to get your location, moving to default location", Toast.LENGTH_SHORT).show();

            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
        }
    }

    public void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to : lat : " + latLng.latitude + ", long : " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * check if the Google Play services are available to make map request
     *
     * @return
     */
    private boolean isServiceOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
        } else {
            Toast.makeText(getContext(), "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (locationPermissionsGranted) {
            getDeviceLocation();
            this.googleMap.setMyLocationEnabled(false);
            googleMap.clear();

//            showCurrentPlace();

        }

    }

    private void executeStreamFetchNearbyRestaurant() {
        this.disposable = PlaceStream.streamFetchNearbyRestaurant(formatLocationToString())
                .subscribeWith(new DisposableObserver<PlaceApiData>() {
                    @Override
                    public void onNext(PlaceApiData placeApiData) {
                      if (placeApiData.getResults().size() > 0) {
                            Log.i(TAG, "onNext: result found !");
                          PlaceApiDataConverter placeApiDataConverter = new PlaceApiDataConverter(placeApiData.getResults());
                          for (PlaceFormater place : placeApiDataConverter.getFormatedListOfPlace()) {
                              placeApiDataConverter.addMarkerFromList(googleMap,place);
                              Log.i(TAG, "onNext: place id"+place.getPlaceId()+ "name = "+place.getPlaceName()+"location = "+currentLocation);
                          }

                        } else {
                            Snackbar.make(getView(), "No article found !", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("StreamInfo", "search error : " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("StreamInfo", "search complete");
                    }
                });

    }

    private String formatLocationToString() {
        if (this.currentLocation != null) {
            return this.currentLocation.getLatitude() + "," + currentLocation.getLongitude();

        } else {
            return DEFAULT_LOCATION.latitude + "," + DEFAULT_LOCATION.longitude;
        }

    }

    private void showCurrentPlace() {


        try {

            ArrayList<String> filter = new ArrayList<>();
            filter.add(String.valueOf(Place.TYPE_RESTAURANT));
            PlaceFilter placeFilter = new PlaceFilter(false, filter); //true or false to filtering by "open now"
            Task<PlaceLikelihoodBufferResponse> placeResult = this.placeDetectionClient.getCurrentPlace(placeFilter);

            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        Log.i(TAG, String.format("PlaceApiData '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                    likelyPlaces.release();
                }

            });

        } catch (SecurityException e) {
            Log.i(TAG, "onFailure: " + e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
//        this.executeStreamFetchNearbyRestaurant();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
