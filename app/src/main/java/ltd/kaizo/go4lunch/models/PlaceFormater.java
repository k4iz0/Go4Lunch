package ltd.kaizo.go4lunch.models;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ltd.kaizo.go4lunch.models.API.PlaceDetail.PlaceDetailResult;

/**
 * The type Place formater.
 */
public class PlaceFormater implements Parcelable {


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
    private List<String> placeHour;
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
    private  String websiteUrl;
    /**
     * The Phone number.
     */
    private  String phoneNumber;

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
    }


    /**
     * Instantiates a new Place formater.
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
        placeHour = in.createStringArrayList();
        placeRate = in.readInt();
        placePhoto = in.readString();
        placeDistance = in.readInt();
        currentLocation = in.readParcelable(Location.class.getClassLoader());
        websiteUrl = in.readString();
        phoneNumber = in.readString();
    }

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
     * Gets website url.
     *
     * @return the website url
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * Sets website url.
     *
     * @param websiteUrl the website url
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
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
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Gets place distance.
     *
     * @return the place distance
     */
    public int getPlaceDistance() {
        return placeDistance;
    }

    /**
     * Sets place distance.
     */
    private void setPlaceDistance() {

        Log.i("PlaceFormater", "setPlaceDistance: lat = " + this.currentLocation.getLatitude() + " longitude = " + this.currentLocation.getLongitude() + "\n" +
                " place lat = " + this.lat + "\n" +
                "place lng = " + this.lng);
        Long tmp = Math.round(SphericalUtil.computeDistanceBetween(
                new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude()), //from
                new LatLng(this.lat, this.lng)));//to
        this.placeDistance = tmp.intValue();
    }


    /**
     * Format string weekday list string.
     *
     * @return the string
     */
    public String formatStringWeekdayList() {
        String str = "";
        if (this.result != null) {
            if (this.result.getOpeningHours() != null && !this.result.getOpeningHours().getOpenNow()) {
                str = "closed";
            } else if (this.placeHour.size() == 7 || this.placeHour.size() == 0) {
                str = "Open 24/7";
            } else {
                str = "Open until " + this.result.getOpeningHours().getPeriods().get(getDayOfTheWeekNumber()).getClose().getTime();
            }
        }
        return str;
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

    /**
     * Gets place rate.
     *
     * @return the place rate
     */
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

    /**
     * Gets place address.
     *
     * @return the place address
     */
    public String getPlaceAddress() {
        return placeAddress;
    }

    /**
     * Sets place address.
     *
     * @param placeAddress the place address
     */
    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    /**
     * Gets place hour.
     *
     * @return the place hour
     */
    public List<String> getPlaceHour() {
        return placeHour;
    }

    /**
     * Sets place hour.
     */
    private void setPlaceHour() {
        if (this.result.getOpeningHours() != null) {
            this.placeHour = this.result.getOpeningHours().getWeekdayText();
        } else {
            this.placeHour = new ArrayList<>();
        }
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
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
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
     * Sets lng.
     *
     * @param lng the lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
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
     * Sets place name.
     *
     * @param placeName the place name
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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
     * @return the marker
     */
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
                "rating = " + placeRate+"\n" +
                "phone number = "+phoneNumber+" \n" +
                "website = "+websiteUrl;
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
        dest.writeStringList(placeHour);
        dest.writeInt(placeRate);
        dest.writeString(placePhoto);
        dest.writeInt(placeDistance);
        dest.writeParcelable(currentLocation, flags);
        dest.writeString(websiteUrl);
        dest.writeString(phoneNumber);
    }
}
