package ltd.kaizo.go4lunch.models.utils;

import java.util.List;

import ltd.kaizo.go4lunch.models.API.NearbySearch.Photo;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailResult;

public class PlaceFormater {

    Double lat;
    Double lng;
    String placeName;
    String placeAdress;
    String placeHour;
    List<Photo> placePhoto;

    public PlaceFormater(PlaceDetailResult result) {
        this.lat = result.getGeometry().getLocation().getLat();
        this.lng = result.getGeometry().getLocation().getLng();
        this.placeAdress = result.getVicinity();
        this.placeName = result.getName();
//        this.placeHour = result.getOpeningHours().getPeriods()
//        this.placePhoto = result.getPhotos();

    }

    public String getPlaceAdress() {
        return placeAdress;
    }

    public void setPlaceAdress(String placeAdress) {
        this.placeAdress = placeAdress;
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


    public List<Photo> getPlacePhoto() {
        return placePhoto;
    }

    public void setPlacePhoto(List<Photo> placePhoto) {
        this.placePhoto = placePhoto;
    }

    @Override
    public String toString() {
        return          "placeName = " + placeName + "\n" +
                        "placeLat = " + lat + "\n" +
                        "placeLng = " + lng;
    }
}
