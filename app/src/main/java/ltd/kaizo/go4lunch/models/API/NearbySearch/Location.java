package ltd.kaizo.go4lunch.models.API.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Location.
 */
public class Location {

    /**
     * The Lat.
     */
    @SerializedName("lat")
    @Expose
    private Double lat;
    /**
     * The Lng.
     */
    @SerializedName("lng")
    @Expose
    private Double lng;

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

}