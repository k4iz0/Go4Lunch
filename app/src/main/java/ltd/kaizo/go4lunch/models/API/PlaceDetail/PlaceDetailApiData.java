package ltd.kaizo.go4lunch.models.API.PlaceDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The type Place detail api data.
 */
public class PlaceDetailApiData {
    /**
     * The Html attributions.
     */
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    /**
     * The Result.
     */
    @SerializedName("result")
    @Expose
    private PlaceDetailResult result;
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
     * Gets result.
     *
     * @return the result
     */
    public PlaceDetailResult getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(PlaceDetailResult result) {
        this.result = result;
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
