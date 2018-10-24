package ltd.kaizo.go4lunch.models.utils;

import android.util.Log;

import ltd.kaizo.go4lunch.models.API.PlaceDetail.Photo;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailResult;

public class PlaceFormater {

    private Double lat;
    private Double lng;
    private String placeName;
    private String placeAdress;
    private String placeHour;
    private int placeRate;
    private String placePhoto;
    private PlaceDetailResult result;

    public PlaceFormater(PlaceDetailResult placeDetailResult) {
        this.result = placeDetailResult;
        this.lat = result.getGeometry().getLocation().getLat();
        this.lng = result.getGeometry().getLocation().getLng();
        this.placeAdress = result.getVicinity();
        this.placeName = result.getName();
        setPlaceRate();
        setPlacePhoto();
//        this.placeHour = result.getOpeningHours().getPeriods()
//        this.placePhoto = result.getPhotos();

    }

    public int getPlaceRate() {
        return placeRate;
    }

    /**
     * convert google rating to int 0,1,2,3
     * to match the stars to display in the list
     */
    private void setPlaceRate() {
        int tmp = 0;
        double googleRating;
        if (this.result.getRating() != null) {
            googleRating = this.result.getRating();
            if (googleRating < 1) {
                tmp = 0;
            } else if (googleRating <= 2) {
                tmp = 1;
            } else if (googleRating <= 4) {
                tmp = 2;
            } else {
                tmp = 3;
            }
        }
        this.placeRate = tmp;
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


    public String getPlacePhoto() {
        return placePhoto;
    }

    private void setPlacePhoto() {
        if (this.result.getPhotos() != null && this.result.getPhotos().size() > 0) {
            for (Photo reference : result.getPhotos()) {
            Log.i("PaceFormater", "setPlacePhoto: "+reference.getPhotoReference());

            }
            this.placePhoto = this.result.getPhotos().get(0).getPhotoReference();
        } else {
            this.placePhoto = "";
        }
    }

    @Override
    public String toString() {
        return "placeName = " + placeName + "\n" +
                "placeLat = " + lat + "\n" +
                "placeLng = " + lng + "\n" +
                "photoUrl = " + placePhoto + "\n" +
                "rating = " + placeRate;
    }
}
