package ltd.kaizo.go4lunch.models.API.nearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Viewport.
 */
class Viewport {
    /**
     * The Northeast.
     */
    @SerializedName("northeast")
    @Expose
    private Northeast northeast;
    /**
     * The Southwest.
     */
    @SerializedName("southwest")
    @Expose
    private Southwest southwest;

    /**
     * Gets northeast.
     *
     * @return the northeast
     */
    public Northeast getNortheast() {
        return northeast;
    }

    /**
     * Sets northeast.
     *
     * @param northeast the northeast
     */
    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    /**
     * Gets southwest.
     *
     * @return the southwest
     */
    public Southwest getSouthwest() {
        return southwest;
    }

    /**
     * Sets southwest.
     *
     * @param southwest the southwest
     */
    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }
}
