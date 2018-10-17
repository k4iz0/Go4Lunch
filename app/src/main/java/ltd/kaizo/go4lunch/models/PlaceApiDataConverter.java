package ltd.kaizo.go4lunch.models;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ltd.kaizo.go4lunch.models.API.Result;
import ltd.kaizo.go4lunch.models.utils.PlaceFormater;

public class PlaceApiDataConverter {
    ArrayList<Result> resultList;

    public PlaceApiDataConverter(ArrayList<Result> resultList) {
        this.resultList = resultList;
    }
    private ArrayList<PlaceFormater> getFormatedListOfPlace() {
        ArrayList<PlaceFormater> formatedList = new ArrayList<>();
        for (Result result : this.resultList) {
            formatedList.add(new PlaceFormater(result));
        }
        return formatedList;
    }
    private void addMarkerFromList(GoogleMap googleMap, PlaceFormater formatedPlace) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(formatedPlace.getLat(), formatedPlace.getLng());
        // Position of Marker on Map
        markerOptions.position(latLng);
        // Adding Title to the Marker
        markerOptions.title(formatedPlace.getPlaceName() + " : " + formatedPlace.getPlaceVicinity());
        // Adding Marker to the Camera.
        Marker m = googleMap.addMarker(markerOptions);
        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }
}
