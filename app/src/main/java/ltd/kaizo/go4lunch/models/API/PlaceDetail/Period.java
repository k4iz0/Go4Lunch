package ltd.kaizo.go4lunch.models.API.PlaceDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Period.
 */
public class Period {

    /**
     * The Close.
     */
    @SerializedName("close")
    @Expose
    private Close close;
    /**
     * The Open.
     */
    @SerializedName("open")
    @Expose
    private Open open;

    /**
     * Gets close.
     *
     * @return the close
     */
    public Close getClose() {
        return close;
    }

    /**
     * Sets close.
     *
     * @param close the close
     */
    public void setClose(Close close) {
        this.close = close;
    }

    /**
     * Gets open.
     *
     * @return the open
     */
    public Open getOpen() {
        return open;
    }

    /**
     * Sets open.
     *
     * @param open the open
     */
    public void setOpen(Open open) {
        this.open = open;
    }

}