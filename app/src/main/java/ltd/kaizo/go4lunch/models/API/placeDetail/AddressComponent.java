package ltd.kaizo.go4lunch.models.API.placeDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The type Address component.
 */
public class AddressComponent {

    /**
     * The Long name.
     */
    @SerializedName("long_name")
    @Expose
    private String longName;
    /**
     * The Short name.
     */
    @SerializedName("short_name")
    @Expose
    private String shortName;
    /**
     * The Types.
     */
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    /**
     * Gets long name.
     *
     * @return the long name
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Sets long name.
     *
     * @param longName the long name
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * Gets short name.
     *
     * @return the short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets short name.
     *
     * @param shortName the short name
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
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

}
