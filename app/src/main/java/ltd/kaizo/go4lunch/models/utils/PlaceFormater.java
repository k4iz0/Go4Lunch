package ltd.kaizo.go4lunch.models.utils;

import java.util.ArrayList;
import java.util.List;

import ltd.kaizo.go4lunch.models.API.Photo;
import ltd.kaizo.go4lunch.models.API.Result;

public class PlaceFormater {
    String placeId;
    Double lat;
    Double lng;
    String placeName;
    String placeVicinity;
    List<Photo> placePhoto;

    public PlaceFormater(Result result) {
        this.placeId = result.getPlaceId();
        this.lat = result.getGeometry().getLocation().getLat();
        this.lng = result.getGeometry().getLocation().getLng();
        this.placeName = result.getName();
        this.placeVicinity = result.getVicinity();
        this.placePhoto = result.getPhotos();
    }

    public PlaceFormater(ArrayList<Result> resultList) {

        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceVicinity() {
        return placeVicinity;
    }

    public void setPlaceVicinity(String placeVicinity) {
        this.placeVicinity = placeVicinity;
    }

    public List<Photo> getPlacePhoto() {
        return placePhoto;
    }

    public void setPlacePhoto(List<Photo> placePhoto) {
        this.placePhoto = placePhoto;
    }

}
