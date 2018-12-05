package ltd.kaizo.go4lunch.models.API.PlaceDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Close.
 */
public class Close {

    /**
     * The Day.
     */
    @SerializedName("day")
    @Expose
    private Integer day;
    /**
     * The Time.
     */
    @SerializedName("time")
    @Expose
    private String time;

    /**
     * Gets day.
     *
     * @return the day
     */
    public Integer getDay() {
        return day;
    }

    /**
     * Sets day.
     *
     * @param day the day
     */
    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(String time) {
        this.time = time;
    }
}
