package ltd.kaizo.go4lunch.controller.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailApiData;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.API.Stream.PlaceStream;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LATITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LONGITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.write;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    /**
     * The constant DEFAULT_ZOOM.
     */
    public static final float DEFAULT_ZOOM = 15f;
    /**
     * The Default location.
     */
    static final LatLng DEFAULT_LOCATION = new LatLng(48.858093, 2.294694); //PARIS
    /**
     * The constant ERROR_DIALOG_REQUEST.
     */
    private static final int ERROR_DIALOG_REQUEST = 9001;
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
     * The Map view.
     */
    private MapView mapView;
    /**
     * The Floating action button.
     */
    private FloatingActionButton floatingActionButton;
    /**
     * The Google map.
     */
    private GoogleMap googleMap;
    /**
     * The Location permissions granted.
     */
    private Boolean locationPermissionsGranted = false;
    /**
     * The Fused location provider client.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;
    /**
     * The Tag.
     */
    private String TAG = getClass().getSimpleName();
    /**
     * The Current location.
     */
    private Location currentLocation;
    /**
     * The Disposable.
     */
    private Disposable disposable;
    /**
     * The Place detail list.
     */
    private ArrayList<PlaceFormater> placeDetailList;
    /**
     * The Gson.
     */
    private Gson gson = new Gson();

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

    /**
     * Configure google map.
     */
    private void configureGoogleMap() {
        if (isServiceOK()) {
            this.getLocationPermission();
            this.configureFloatingButton();
            // Construct a FusedLocationProviderClient.
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        }
    }

    /**
     * Configure floating button.
     */
    private void configureFloatingButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToCurrentLocation(currentLocation);
            }
        });
    }

    /**
     * Init map.
     */
    private void initMap() {
        mapView.getMapAsync(this);
    }

    /**
     * Gets location permission.
     */
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
                this.initMap();
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
                    //TODO tester si ça fonctionne
                    getActivity().recreate();
                }
        }

    }

    /**
     * check if the Google Play services are available to make map request
     *
     * @return Boolean boolean
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
                            Log.i(TAG, "isCurrentLocationChange: " + isCurrentLocationChange(currentLocation));
                            //if the location change since last time we search for new restaurant or first time we launch the app
                            if (isCurrentLocationChange(currentLocation) || read(CURRENT_LATITUDE_KEY, 0.0) == 0.0) {
                                executeStreamFetchNearbyRestaurantAndGetPlaceDetail();
                                Log.d(TAG, "onComplete: found location ! lat = " + currentLocation.getLatitude() + " et long = " + currentLocation.getLongitude());

                            } else {
                                addAllRestaurantFromFirestoreToList();
                            }
                            moveCameraToCurrentLocation(currentLocation);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Unable to get current location \nhave you enable localisation on your device ?", Toast.LENGTH_LONG).show();

                            }
                        }


                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("getDeviceLocation", "security exception : " + e.getMessage());
        }
    }

    /**
     * Is current location change boolean.
     *
     * @param currentLocation the current location
     * @return the boolean
     */
    private Boolean isCurrentLocationChange(Location currentLocation) {
        Double previousLocationLat = read(CURRENT_LATITUDE_KEY, 0.0);
        Double previousLocationLng = read(CURRENT_LONGITUDE_KEY, 0.0);
        Log.i(TAG, "isCurrentLocationChange: current location lat = " + currentLocation.getLatitude() + " lng = " + currentLocation.getLongitude() + "\n" +
                "previous location lat = " + previousLocationLat + " and lng " + previousLocationLng);
        if (previousLocationLat == 0.0 || previousLocationLat != (currentLocation.getLatitude())) {
            write(CURRENT_LATITUDE_KEY, currentLocation.getLatitude());
            return true;
        }
        if (previousLocationLng == 0.0 || previousLocationLng != (currentLocation.getLongitude())) {
            write(CURRENT_LONGITUDE_KEY, currentLocation.getLongitude());
            return true;
        }
        return false;

    }

    /**
     * Move camera to current location.
     *
     * @param currentLocation the current location
     */
    private void moveCameraToCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        if (this.currentLocation != null) {
            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
        } else {
            Toast.makeText(getContext(), "Unable to get your location, moving to default location", Toast.LENGTH_SHORT).show();
            moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM);
        }
    }

    /**
     * Move camera.
     *
     * @param latLng the lat lng
     * @param zoom   the zoom
     */
    public void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to : lat : " + latLng.latitude + ", long : " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.setMapViewStyle();
        if (locationPermissionsGranted) {
            getDeviceLocation();
            this.googleMap.setMyLocationEnabled(false);

        }

    }

    /**
     * Configure on marker click.
     *
     * @param placeDetailList the place detail list
     */
    private void configureOnMarkerClick(final ArrayList<PlaceFormater> placeDetailList) {
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (PlaceFormater place : placeDetailList) {
                    if (marker.getTitle().equalsIgnoreCase(place.getPlaceName())) {
                        Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                        detailActivity.putExtra("PlaceFormater", place);
                        startActivity(detailActivity);

                    }
                }
                return true;
            }
        });
    }

    /**
     * Sets map view style.
     */
    private void setMapViewStyle() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            if (getContext() != null) {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this.getContext(), R.raw.style_json));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    //****************************
    //******  DATA STREAMS *******
    //****************************

    /**
     * Execute stream fetch nearby restaurant and get place detail.
     */
    private void executeStreamFetchNearbyRestaurantAndGetPlaceDetail() {

        this.disposable = PlaceStream.INSTANCE.streamFetchNearbyRestaurantAndGetPlaceDetail(formatLocationToString())
                .subscribeWith(new DisposableObserver<PlaceDetailApiData>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        resetRestaurantListFromFirestore();
                        placeDetailList = new ArrayList<>();
                    }

                    @Override
                    public void onNext(PlaceDetailApiData placeDetailApiData) {
                        if (placeDetailApiData != null) {
                            PlaceFormater place = new PlaceFormater(placeDetailApiData.getResult(), currentLocation);
                            //add place to list
                            placeDetailList.add(place);
                            // add marker on map
                            place.addMarkerFromList(googleMap, place, false);

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
                        Collections.sort(placeDetailList, PlaceFormater.compareToByDistance());
                        for (PlaceFormater place : placeDetailList) {
                            Log.i(TAG, "onComplete: create placedetailList = \n" +
                                    place.getPlaceName() + " " + place.getPlaceDistance());
                        }
                        // save the list
                        write(RESTAURANT_LIST_KEY, gson.toJson(placeDetailList));
                        //add place to firestore
                        for (PlaceFormater place : placeDetailList) {
                            Log.i(TAG, "onComplete: create restaurantList = \n" +
                                    place.getPlaceName() + " " + place.getPlaceDistance());
                            RestaurantHelper.createRestaurant(place.getId(), place).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "an error has occurred " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        //configure click event
                        configureOnMarkerClick(placeDetailList);
                        switchMarkerColor();
                    }
                });
    }

    /**
     * Reset restaurant list from firestore.
     */
    private void resetRestaurantListFromFirestore() {
        Log.i(TAG, "resetRestaurantListFromFirestore: deleting the list");
        RestaurantHelper.getAllRestaurantsFromFirestore().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    RestaurantHelper.deleteRestaurantsFromList(queryDocumentSnapshots.getDocuments());
                } else {
                    Log.i(TAG, "onSuccess: no data found");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: an error as occurred " + e);
            }
        });
    }

    /**
     * Add all restaurant from firestore to list.
     */
    private void addAllRestaurantFromFirestoreToList() {
        placeDetailList = new ArrayList<>();
        RestaurantHelper.getAllRestaurants().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    for (Restaurant restaurant : queryDocumentSnapshots.toObjects(Restaurant.class)) {
                        placeDetailList.add(restaurant.getPlaceFormater());
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (PlaceFormater place : placeDetailList) {
                        // add marker on map
                        place.addMarkerFromList(googleMap, place, false);
                    }
                    write(RESTAURANT_LIST_KEY, gson.toJson(placeDetailList));
                    configureOnMarkerClick(placeDetailList);
                    switchMarkerColor();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                executeStreamFetchNearbyRestaurantAndGetPlaceDetail();
            }
        });
    }

    /**
     * Switch marker color.
     */
    private void switchMarkerColor() {
        RestaurantHelper.getAllRestaurantsFromFirestore().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    List<Restaurant> restaurantArrayList = task.getResult().toObjects(Restaurant.class);
                    for (Restaurant restaurant : restaurantArrayList) {
                        if (restaurant.getUserList().size() > 0) {
                            restaurant.getPlaceFormater().addMarkerFromList(googleMap, restaurant.getPlaceFormater(), true);
                        }
                    }
                }
            }
        });


    }

    /**
     * Format location to string string.
     *
     * @return the string
     */
    private String formatLocationToString() {
        if (this.currentLocation != null) {
            return this.currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        } else {
            return DEFAULT_LOCATION.latitude + "," + DEFAULT_LOCATION.longitude;
        }
    }
}
