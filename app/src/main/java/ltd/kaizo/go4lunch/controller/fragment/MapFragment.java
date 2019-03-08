package ltd.kaizo.go4lunch.controller.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.models.API.RestaurantHelper;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.Restaurant;
import timber.log.Timber;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LATITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LONGITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_AROUND_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_DETAIL_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    /**
     * The constant DEFAULT_ZOOM.
     */
    public static final float DEFAULT_ZOOM = 15f;
    /**
     * The Default location.
     */
    public static final LatLng DEFAULT_LOCATION = new LatLng(48.858093, 2.294694); //PARIS
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
     * The Current location.
     */
    private LatLng currentLocation;

    /**
     * The Place detail list.
     */
    private ArrayList<PlaceFormater> placeDetailList = new ArrayList<>();
    /**
     * The Place around list.
     */
    private ArrayList<PlaceFormater> placeAroundList = new ArrayList<>();
    /**
     * The current latitude
     */
    private Double currentLatitude;
    /**
     * The current longitude
     */
    private Double currentLongitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeAroundList = getArguments().getParcelableArrayList(RESTAURANT_LIST_AROUND_KEY);
            placeDetailList = getArguments().getParcelableArrayList(RESTAURANT_LIST_DETAIL_KEY);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.fragment_map_mapview);
        floatingActionButton = rootView.findViewById(R.id.fragment_map_fab);
        mapView.onCreate(savedInstanceState);
        this.initMap();
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
        this.getCurrentLocation();
        this.configureListAndMarker(placeDetailList);
        this.setCorrectDistance();
        this.configureFloatingButton();
        this.moveCameraToCurrentLocation(currentLocation);
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
     * Gets current location.
     */
    private void getCurrentLocation() {
        currentLatitude = read(CURRENT_LATITUDE_KEY, DEFAULT_LOCATION.latitude);
        currentLongitude = read(CURRENT_LONGITUDE_KEY, DEFAULT_LOCATION.longitude);
        currentLocation = new LatLng(currentLatitude, currentLongitude);
    }

    /**
     * Move camera to current location.
     *
     * @param currentLocation the current location
     */
    private void moveCameraToCurrentLocation(LatLng currentLocation) {
        moveCamera(new LatLng(currentLocation.latitude, currentLocation.longitude), DEFAULT_ZOOM);
    }

    /**
     * Move camera.
     *
     * @param latLng the lat lng
     * @param zoom   the zoom
     */
    public void moveCamera(LatLng latLng, float zoom) {
        Timber.d("moveCamera: moving the camera to : lat : " + latLng.latitude + ", long : " + latLng.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.setMapViewStyle();
        this.googleMap.setMyLocationEnabled(false);
        this.configureGoogleMap();
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
                    Timber.e("Style parsing failed.");
                }
            }
        } catch (Resources.NotFoundException e) {
            Timber.e(e, "Can't find style. Error: ");
        }
    }


    /**
     * Sets correct distance.
     */
    private void setCorrectDistance() {
        for (int i = 0; i < placeDetailList.size(); i++) {
            placeDetailList.get(i).setDistance(placeAroundList.get(i).getPlaceDistance());
        }
    }

    /****************************
     *********   MARKER   ********
     *****************************/
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
     * Configure list and marker.
     *
     * @param restaurantList the restaurant list
     */
    public void configureListAndMarker(ArrayList<PlaceFormater> restaurantList) {
        //add marker on map
        for (PlaceFormater place : restaurantList) {
            Timber.i("place " + place.getPlaceName() + " et visibility = " + place.getVisible());
            place.setMarker();

            this.googleMap.addMarker(place.getMarker());
        }
        //configure click event
        configureOnMarkerClick(restaurantList);
        switchMarkerColor();
    }

    /**
     * Switch marker color base on a firebase listener
     */
    private void switchMarkerColor() {
        RestaurantHelper.getRestaurantsCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) return;

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    Restaurant tmp = documentSnapshot.toObject(Restaurant.class);

                    for (PlaceFormater place : placeDetailList) {
                        if (place.getId().equalsIgnoreCase(tmp.getPlaceFormater().getId())) {
                            place.setJoining(!tmp.getUserList().isEmpty());
                            place.setMarker();
                            googleMap.addMarker(place.getMarker());
                        }
                    }
                }
            }
        });

    }

}
