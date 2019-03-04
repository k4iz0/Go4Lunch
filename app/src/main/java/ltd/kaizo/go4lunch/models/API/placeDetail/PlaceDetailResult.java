package ltd.kaizo.go4lunch.models.API.placeDetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Place detail result.
 */
public class PlaceDetailResult {

    /**
     * The Formatted address.
     */
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    /**
     * The Formatted phone number.
     */
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    /**
     * The Geometry.
     */
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    /**
     * The Name.
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * The Opening hours.
     */
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    /**
     * The Photos.
     */
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    /**
     * The Rating.
     */
    @SerializedName("rating")
    @Expose
    private Double rating;
    /**
     * The Vicinity.
     */
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    /**
     * The Website.
     */
    @SerializedName("website")
    @Expose
    private String website;

    /**
     * Gets formatted address.
     *
     * @return the formatted address
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * Sets formatted address.
     *
     * @param formattedAddress the formatted address
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     * Gets formatted phone number.
     *
     * @return the formatted phone number
     */
    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    /**
     * Sets formatted phone number.
     *
     * @param formattedPhoneNumber the formatted phone number
     */
    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    /**
     * Gets geometry.
     *
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Sets geometry.
     *
     * @param geometry the geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets opening hours.
     *
     * @return the opening hours
     */
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    /**
     * Sets opening hours.
     *
     * @param openingHours the opening hours
     */
    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    /**
     * Gets photos.
     *
     * @return the photos
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * Sets photos.
     *
     * @param photos the photos
     */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * Gets vicinity.
     *
     * @return the vicinity
     */
    public String getVicinity() {
        return vicinity;
    }

    /**
     * Sets vicinity.
     *
     * @param vicinity the vicinity
     */
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    /**
     * Gets website.
     *
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets website.
     *
     * @param website the website
     */
    public void setWebsite(String website) {
        this.website = website;
    }


}