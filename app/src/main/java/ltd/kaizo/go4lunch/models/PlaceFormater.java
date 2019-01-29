package ltd.kaizo.go4lunch.models;

import android.content.res.Resources;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.joda.time.DateTime;

import java.util.Comparator;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.OpeningHours;
import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailResult;

/**
 * The type Place formater.
 */
public class PlaceFormater implements Parcelable {


    /**
     * The constant CREATOR.
     */
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
    /**
     * The Id.
     */
    private String id;
    /**
     * The Lat.
     */
    private Double lat;
    /**
     * The Lng.
     */
    private Double lng;
    /**
     * The Place name.
     */
    private String placeName;
    /**
     * The Place address.
     */
    private String placeAddress;
    /**
     * The Place hour.
     */
    private OpeningHours placeHour;
    /**
     * The Place rate.
     */
    private int placeRate;
    /**
     * The Place photo.
     */
    private String placePhoto;
    /**
     * The Result.
     */
    private PlaceDetailResult result;
    /**
     * The Place distance.
     */
    private int placeDistance;
    /**
     * The Current location.
     */
    private Location currentLocation;
    /**
     * The Website url.
     */
    private String websiteUrl;
    /**
     * The Phone number.
     */
    private String phoneNumber;
    /**
     * The Open or close.
     */
    private String openOrClose;

    /**
     * Instantiates a new Place formater.
     */
    public PlaceFormater() {
    }


    /**
     * Instantiates a new Place formater.
     *
     * @param placeDetailResult the place detail result
     * @param currentLocation   the current location
     */
    public PlaceFormater(PlaceDetailResult placeDetailResult, Location currentLocation) {
        this.result = placeDetailResult;
        this.id = (this.result.getName() + this.result.getVicinity().toLowerCase());
        this.id = this.id.replaceAll("[^\\x20-\\x7e]", "").replaceAll("/", "");
        this.lat = result.getGeometry().getLocation().getLat();
        this.lng = result.getGeometry().getLocation().getLng();
        this.placeAddress = result.getVicinity();
        this.placeName = result.getName();
        this.currentLocation = currentLocation;
        this.phoneNumber = result.getFormattedPhoneNumber();
        this.websiteUrl = result.getWebsite();
        setPlaceDistance();
        setPlaceHour();
        setPlaceRate();
        setPlacePhoto();
        setOpenOrClose();
    }


    /**
     * Compare to by distance comparator.
     *
     * @return the comparator
     */
    public static Comparator<PlaceFormater> compareToByDistance() {
        Comparator comp = new Comparator<PlaceFormater>() {
            @Override
            public int compare(PlaceFormater place1, PlaceFormater place2) {
                return place1.getPlaceDistance() - (place2.getPlaceDistance());
            }


        };
        return comp;
    }

    /****************************
    *********   GETTER   ********
    *****************************/

    /**
     * Gets website url.
     *
     * @return the website url
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets open or close.
     *
     * @return the open or close
     */
    public String getOpenOrClose() {
        return openOrClose;
    }

    /**
     * Gets place distance.
     *
     * @return the place distance
     */
    public int getPlaceDistance() {
        return placeDistance;
    }

    /**
     * Gets place address.
     *
     * @return the place address
     */
    public String getPlaceAddress() {
        return placeAddress;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * Gets lng.
     *
     * @return the lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     * Gets place name.
     *
     * @return the place name
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Gets place photo.
     *
     * @return the place photo
     */
    public String getPlacePhoto() {
        return placePhoto;
    }

    /**
     * Gets day of the week number.
     *
     * @return the day of the week number
     */
    private int getDayOfTheWeekNumber() {
        DateTime dt = new DateTime();
        return dt.getDayOfWeek() - 1;
    }

    /****************************
     ********   SETTER   ********
     ****************************/

    /**
     * Gets place rate.
     *
     * @return the place rate
     */
    public int getPlaceRate() {
        return placeRate;
    }

    /**
     * set place distance
     *
     * @return the place distance
     */
    public void setDistance(int distance) {
        this.placeDistance = distance;
    }

    /**
     * determine place distance.
     */
    public void setPlaceDistance() {

        Log.i("PlaceFormater", "setPlaceDistance: lat = " + this.currentLocation.getLatitude() + " longitude = " + this.currentLocation.getLongitude() + "\n" +
                " place lat = " + this.lat + "\n" +
                "place lng = " + this.lng);
        Long tmp = Math.round(SphericalUtil.computeDistanceBetween(
                new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude()), //from
                new LatLng(this.lat, this.lng)));//to
        this.placeDistance = tmp.intValue();
    }

    /**
     * Sets place hour.
     */
    private void setPlaceHour() {
        this.placeHour = this.result.getOpeningHours();
    }

    /**
     * Format string weekday list string.
     *
     * @return the string
     */
    public void setOpenOrClose() {
        String str = "close";
        if (this.placeHour != null) {
            if (this.placeHour.getWeekdayText().size() == 0) {
                str = "Open 24/7";
            } else {
                if (this.placeHour.getPeriods().size() > 1) {
                    String hour = this.placeHour.getPeriods().get(getDayOfTheWeekNumber()).getClose().getTime().substring(0, 2);
                    String min = this.placeHour.getPeriods().get(getDayOfTheWeekNumber()).getClose().getTime().substring(2);
                    str = "Open until " + hour + "H" + min;
                }
            }
        }
        this.openOrClose = str;
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

    /**
     * Sets place photo.
     */
    private void setPlacePhoto() {
        if (this.result.getPhotos() != null && this.result.getPhotos().size() > 0) {
            this.placePhoto = this.result.getPhotos().get(0).getPhotoReference();
        } else {
            this.placePhoto = "";
        }
    }

    /**
     * Add marker from list marker.
     *
     * @param googleMap     the google map
     * @param formatedPlace the formated place
     * @param isJoining     the is joining
     * @return the marker
     */
    public Marker addMarkerFromList(GoogleMap googleMap, PlaceFormater formatedPlace, Boolean isJoining) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(formatedPlace.getLat(), formatedPlace.getLng());
        // Position of Marker on Map
        markerOptions.position(latLng);
        // Adding Title to the Marker
        markerOptions.title(formatedPlace.getPlaceName());
        // Adding colour to the marker
        if (isJoining) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_map_icon_green));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_map_icon));
        }
        // Adding Marker to the Camera.
        return googleMap.addMarker(markerOptions);
    }

    @Override
    public String toString() {
        return "placeName = " + placeName + "\n" +
                "placeLat = " + lat + "\n" +
                "placeLng = " + lng + "\n" +
                "photoUrl = " + placePhoto + "\n" +
                "rating = " + placeRate + "\n" +
                "phone number = " + phoneNumber + " \n" +
                "website = " + websiteUrl;
    }

    /****************************
    *******   PARCELABLE   ******
    *****************************/

    /**
     * Parcelable implementation.
     *
     * @param in the in
     */
    protected PlaceFormater(Parcel in) {
        id = in.readString();
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
        placeRate = in.readInt();
        placePhoto = in.readString();
        placeDistance = in.readInt();
        currentLocation = in.readParcelable(Location.class.getClassLoader());
        websiteUrl = in.readString();
        phoneNumber = in.readString();
        openOrClose = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
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
        dest.writeInt(placeRate);
        dest.writeString(placePhoto);
        dest.writeInt(placeDistance);
        dest.writeParcelable(currentLocation, flags);
        dest.writeString(websiteUrl);
        dest.writeString(phoneNumber);
        dest.writeString(openOrClose);
    }

}
