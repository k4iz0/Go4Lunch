package ltd.kaizo.go4lunch.models.API.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Opening hours.
 */
public class OpeningHours {

    /**
     * The Open now.
     */
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    /**
     * Gets open now.
     *
     * @return the open now
     */
    public Boolean getOpenNow() {
        return openNow;
    }

    /**
     * Sets open now.
     *
     * @param openNow the open now
     */
    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

}