package ltd.kaizo.go4lunch.models.API.NearbySearch;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Geometry.
 */
public class Geometry {

    /**
     * The Location.
     */
    @SerializedName("location")
    @Expose
    private Location location;
    /**
     * The Viewport.
     */
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets viewport.
     *
     * @return the viewport
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Sets viewport.
     *
     * @param viewport the viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }


}
