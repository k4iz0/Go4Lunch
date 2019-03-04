package ltd.kaizo.go4lunch.models.API.nearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The type Place api data.
 */
public class PlaceApiData {


    /**
     * The Html attributions.
     */
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    /**
     * The Next page token.
     */
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    /**
     * The Results.
     */
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    /**
     * The Status.
     */
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * Gets html attributions.
     *
     * @return the html attributions
     */
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * Sets html attributions.
     *
     * @param htmlAttributions the html attributions
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * Gets next page token.
     *
     * @return the next page token
     */
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * Sets next page token.
     *
     * @param nextPageToken the next page token
     */
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * Gets results.
     *
     * @return the results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * Sets results.
     *
     * @param results the results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
