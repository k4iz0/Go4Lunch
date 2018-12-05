package ltd.kaizo.go4lunch.models.API.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The type Photo.
 */
public class Photo {

    /**
     * The Height.
     */
    @SerializedName("height")
    @Expose
    private Integer height;
    /**
     * The Html attributions.
     */
    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions = null;
    /**
     * The Photo reference.
     */
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;
    /**
     * The Width.
     */
    @SerializedName("width")
    @Expose
    private Integer width;

    /**
     * Gets height.
     *
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Gets html attributions.
     *
     * @return the html attributions
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * Sets html attributions.
     *
     * @param htmlAttributions the html attributions
     */
    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * Gets photo reference.
     *
     * @return the photo reference
     */
    public String getPhotoReference() {
        return photoReference;
    }

    /**
     * Sets photo reference.
     *
     * @param photoReference the photo reference
     */
    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

}