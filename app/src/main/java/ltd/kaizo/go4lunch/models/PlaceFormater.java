package ltd.kaizo.go4lunch.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import ltd.kaizo.go4lunch.models.API.PlaceDetail.Photo;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailResult;

import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LATITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.CURRENT_LONGITUDE_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.read;

public class PlaceFormater implements Parcelable {

    private Double lat;
    private Double lng;
    private String placeName;
    private String placeAddress;
    private List<String> placeHour;
    private int placeRate;
    private String placePhoto;
    private PlaceDetailResult result;
    private int placeDistance;
    private Location currentLocation;

    public PlaceFormater(PlaceDetailResult placeDetailResult, Location currentLocation) {
        this.result = placeDetailResult;
        this.lat = result.getGeometry().getLocation().getLat();
        this.lng = result.getGeometry().getLocation().getLng();
        this.placeAddress = result.getVicinity();
        this.placeName = result.getName();
        this.currentLocation = currentLocation;
        setPlaceDistance();
        setPlaceHour();
        setPlaceRate();
        setPlacePhoto();
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
        placeAddress = in.readString();
        placeHour = in.createStringArrayList();
        placeRate = in.readInt();
        placePhoto = in.readString();
        placeDistance = in.readInt();
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

    public int getPlaceDistance() {
        return placeDistance;
    }

    private void setPlaceDistance() {

        Log.i("PlaceFormater", "setPlaceDistance: lat = " + this.currentLocation.getLatitude() + " longitude = " + this.currentLocation.getLongitude() + "\n" +
                " place lat = " + this.lat + "\n" +
                "place lng = " + this.lng);
        Long tmp =  Math.round(SphericalUtil.computeDistanceBetween(
                new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude()), //from
                new LatLng(this.lat, this.lng)));//to
        this.placeDistance =tmp.intValue();
    }



    public String formatStringWeekdayList() {
        String str;
        if (this.result.getOpeningHours() != null && !this.result.getOpeningHours().getOpenNow()) {
            str = "closed";
        } else if (this.placeHour.size() == 7 || this.placeHour.size() == 0) {
            str = "Open 24/7";
        } else {
            str = "Open until "+this.result.getOpeningHours().getPeriods().get(getDayOfTheWeekNumber()).getClose().getTime();
        }
    return str;
    }

    private int getDayOfTheWeekNumber() {
        DateTime dt = new DateTime();
        return dt.getDayOfWeek()-1;
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

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public List<String> getPlaceHour() {
        return placeHour;
    }

    private void setPlaceHour() {
        if (this.result.getOpeningHours() != null) {
            this.placeHour = this.result.getOpeningHours().getWeekdayText();
        } else {
            this.placeHour = new ArrayList<>();
        }
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
        dest.writeString(placeAddress);
        dest.writeStringList(placeHour);
        dest.writeInt(placeRate);
        dest.writeString(placePhoto);
        dest.writeDouble(placeDistance);
    }
}
