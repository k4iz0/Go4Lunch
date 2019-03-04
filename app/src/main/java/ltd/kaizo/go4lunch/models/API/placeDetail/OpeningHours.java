package ltd.kaizo.go4lunch.models.API.placeDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
     * The Periods.
     */
    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;
    /**
     * The Weekday text.
     */
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

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

    /**
     * Gets periods.
     *
     * @return the periods
     */
    public List<Period> getPeriods() {
        return periods;
    }

    /**
     * Sets periods.
     *
     * @param periods the periods
     */
    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    /**
     * Gets weekday text.
     *
     * @return the weekday text
     */
    public List<String> getWeekdayText() {
        return weekdayText;
    }

    /**
     * Sets weekday text.
     *
     * @param weekdayText the weekday text
     */
    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

}
