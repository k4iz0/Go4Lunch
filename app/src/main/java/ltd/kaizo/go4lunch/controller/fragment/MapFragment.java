package ltd.kaizo.go4lunch.controller.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.NearbySearch.PlaceApiData;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailApiData;
import ltd.kaizo.go4lunch.models.PlaceApiDataConverter;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;
import ltd.kaizo.go4lunch.models.utils.PlaceStream;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.write;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    public static final float DEFAULT_ZOOM = 15f;
    static final LatLng DEFAULT_LOCATION = new LatLng(48.858093, 2.294694); //PARIS
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
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
    private PlaceApiDataConverter restaurantList;
    private ArrayList<PlaceFormater> placeDetailList;
    private Gson gson = new Gson();
    private ArrayList<String> placeIdList;

    @Override
    protected int getFragmentLayout() {
        return R.id.fragment_map_layout;
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

    //****************************
    //*******  PERMISSIONS *******
    //****************************
    private void getLocationPermission() {

        Log.d("googleMap", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                            executeStreamFetchNearbyRestaurant();
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
                            Snackbar.make(getView(), "Unable to get current location \nhave you enable localisation on your device ?", 5).show();
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
        } else {
            Toast.makeText(getContext(), "Unable to get your location, moving to default location", Toast.LENGTH_SHORT).show();
            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
        }
    }

    public void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to : lat : " + latLng.latitude + ", long : " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (locationPermissionsGranted) {
            getDeviceLocation();
            this.googleMap.setMyLocationEnabled(false);

        }

    }

    //****************************
    //******  DATA STREAMS *******
    //****************************

    private void executeStreamFetchNearbyRestaurant() {
        this.disposable = PlaceStream.INSTANCE.streamFetchNearbyRestaurant(formatLocationToString())
                .subscribeWith(new DisposableObserver<PlaceApiData>() {
                    @Override
                    public void onNext(PlaceApiData placeApiData) {
                        if (placeApiData.getResults().size() > 0) {
                            Log.i(TAG, "onNext: result found !");
                            restaurantList = new PlaceApiDataConverter(placeApiData.getResults());
                            placeIdList = restaurantList.getListOfPlaceID();

                        } else {
                            Snackbar.make(getView(), "No place found !", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("StreamFetchNearby", "search error : " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("StreamFetchNearby", "search complete");
                        placeDetailList = new ArrayList<>();
                        for (String id : placeIdList) {
                            Log.i(TAG, "StreamFetchNearby onComplete: executeStreamFetchPlaceDetail for  " + id );
                            executeStreamFetchPlaceDetail(id);
                        }


                    }
                });

    }

    private void executeStreamFetchPlaceDetail(String placeId) {
        this.disposable = PlaceStream.INSTANCE.streamFetchPlaceDetail(placeId)
                .subscribeWith(new DisposableObserver<PlaceDetailApiData>() {
                    @Override
                    public void onNext(PlaceDetailApiData placeDetailApiData) {
                        if (placeDetailApiData.getResult() != null) {
                            PlaceFormater place = new PlaceFormater(placeDetailApiData.getResult());
                            //add place to list
                            placeDetailList.add(place);
                            // add marker on map
                            restaurantList.addMarkerFromList(googleMap,place);
                            // save the list
                            restaurantList.setPlaceDetailList(placeDetailList);
                            write(RESTAURANT_LIST_KEY,gson.toJson(restaurantList));

                        } else {
                            Snackbar.make(getView(), "No place found !", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("StreamFetchPlaceDetail ", "search error : " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("StreamFetchPlaceDetail", "search complete ");
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


}
