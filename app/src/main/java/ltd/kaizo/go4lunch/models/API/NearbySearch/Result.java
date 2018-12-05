package ltd.kaizo.go4lunch.models.API.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The type Result.
 */
public class Result {

    /**
     * The Geometry.
     */
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    /**
     * The Icon.
     */
    @SerializedName("icon")
    @Expose
    private String icon;
    /**
     * The Id.
     */
    @SerializedName("id")
    @Expose
    private String id;
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
     * The Place id.
     */
    @SerializedName("place_id")
    @Expose
    private String placeId;
    /**
     * The Plus code.
     */
    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;
    /**
     * The Rating.
     */
    @SerializedName("rating")
    @Expose
    private Double rating;
    /**
     * The Reference.
     */
    @SerializedName("reference")
    @Expose
    private String reference;
    /**
     * The Scope.
     */
    @SerializedName("scope")
    @Expose
    private String scope;
    /**
     * The Types.
     */
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    /**
     * The Vicinity.
     */
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    /**
     * The Price level.
     */
    @SerializedName("price_level")
    @Expose
    private Integer priceLevel;

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
     * Gets icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
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
     * Gets place id.
     *
     * @return the place id
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Sets place id.
     *
     * @param placeId the place id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * Gets plus code.
     *
     * @return the plus code
     */
    public PlusCode getPlusCode() {
        return plusCode;
    }

    /**
     * Sets plus code.
     *
     * @param plusCode the plus code
     */
    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
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
     * Gets reference.
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets reference.
     *
     * @param reference the reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Gets scope.
     *
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets scope.
     *
     * @param scope the scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Gets types.
     *
     * @return the types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Sets types.
     *
     * @param types the types
     */
    public void setTypes(List<String> types) {
        this.types = types;
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
     * Gets price level.
     *
     * @return the price level
     */
    public Integer getPriceLevel() {
        return priceLevel;
    }

    /**
     * Sets price level.
     *
     * @param priceLevel the price level
     */
    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }
}
