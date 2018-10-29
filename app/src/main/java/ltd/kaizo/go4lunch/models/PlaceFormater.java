package ltd.kaizo.go4lunch.models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ltd.kaizo.go4lunch.controller.activities.DetailActivity;
import ltd.kaizo.go4lunch.controller.activities.MainActivity;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.Photo;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailResult;

public class PlaceFormater implements Parcelable {

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

    protected PlaceFormater(Parcel in) {
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
        placeName = in.readString();
        placeAdress = in.readString();
        placeHour = in.readString();
        placeRate = in.readInt();
        placePhoto = in.readString();
    }

    public static final Creator<PlaceFormater> CREATOR = new Creator<PlaceFormater>() {
        @Override
        public PlaceFormater createFromParcel(Parcel in) {
            return new PlaceFormater(in);
        }

        @Override
        public PlaceFormater[] newArray(int size) {
            return new PlaceFormater[size];
        }
    };

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
    public Marker addMarkerFromList(GoogleMap googleMap, PlaceFormater formatedPlace) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(formatedPlace.getLat(), formatedPlace.getLng());
        // Position of Marker on Map
        markerOptions.position(latLng);
        // Adding Title to the Marker
        markerOptions.title(formatedPlace.getPlaceName());
        // Adding Marker to the Camera.
        Marker m = googleMap.addMarker(markerOptions);
        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        return m;
    }
    @Override
    public String toString() {
        return "placeName = " + placeName + "\n" +
                "placeLat = " + lat + "\n" +
                "placeLng = " + lng + "\n" +
                "photoUrl = " + placePhoto + "\n" +
                "rating = " + placeRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lng);
        }
        dest.writeString(placeName);
        dest.writeString(placeAdress);
        dest.writeString(placeHour);
        dest.writeInt(placeRate);
        dest.writeString(placePhoto);
    }
}
