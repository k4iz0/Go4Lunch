package ltd.kaizo.go4lunch.models.API.nearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Plus code.
 */
class PlusCode {
    /**
     * The Compound code.
     */
    @SerializedName("compound_code")
    @Expose
    private String compoundCode;
    /**
     * The Global code.
     */
    @SerializedName("global_code")
    @Expose
    private String globalCode;

    /**
     * Gets compound code.
     *
     * @return the compound code
     */
    public String getCompoundCode() {
        return compoundCode;
    }

    /**
     * Sets compound code.
     *
     * @param compoundCode the compound code
     */
    public void setCompoundCode(String compoundCode) {
        this.compoundCode = compoundCode;
    }

    /**
     * Gets global code.
     *
     * @return the global code
     */
    public String getGlobalCode() {
        return globalCode;
    }

    /**
     * Sets global code.
     *
     * @param globalCode the global code
     */
    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }
}
