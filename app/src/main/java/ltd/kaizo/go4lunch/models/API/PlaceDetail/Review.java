package ltd.kaizo.go4lunch.models.API.PlaceDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Review.
 */
public class Review {
    /**
     * The Author name.
     */
    @SerializedName("author_name")
    @Expose
    private String authorName;
    /**
     * The Author url.
     */
    @SerializedName("author_url")
    @Expose
    private String authorUrl;
    /**
     * The Language.
     */
    @SerializedName("language")
    @Expose
    private String language;
    /**
     * The Profile photo url.
     */
    @SerializedName("profile_photo_url")
    @Expose
    private String profilePhotoUrl;
    /**
     * The Rating.
     */
    @SerializedName("rating")
    @Expose
    private Integer rating;
    /**
     * The Relative time description.
     */
    @SerializedName("relative_time_description")
    @Expose
    private String relativeTimeDescription;
    /**
     * The Text.
     */
    @SerializedName("text")
    @Expose
    private String text;
    /**
     * The Time.
     */
    @SerializedName("time")
    @Expose
    private Integer time;

    /**
     * Gets author name.
     *
     * @return the author name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets author name.
     *
     * @param authorName the author name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Gets author url.
     *
     * @return the author url
     */
    public String getAuthorUrl() {
        return authorUrl;
    }

    /**
     * Sets author url.
     *
     * @param authorUrl the author url
     */
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    /**
     * Gets language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets language.
     *
     * @param language the language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets profile photo url.
     *
     * @return the profile photo url
     */
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    /**
     * Sets profile photo url.
     *
     * @param profilePhotoUrl the profile photo url
     */
    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * Gets relative time description.
     *
     * @return the relative time description
     */
    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    /**
     * Sets relative time description.
     *
     * @param relativeTimeDescription the relative time description
     */
    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public Integer getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(Integer time) {
        this.time = time;
    }

}
