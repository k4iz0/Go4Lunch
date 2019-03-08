package ltd.kaizo.go4lunch.models;

import java.util.ArrayList;

/**
 * The type Restaurant.
 */
public class Restaurant {
    /**
     * The Place id.
     */
    private String PlaceId;
    /**
     * The Place formater.
     */
    private PlaceFormater placeFormater;

    /**
     * The User list.
     */
    private ArrayList<String> userList;

    /**
     * Instantiates a new Restaurant.
     */
    public Restaurant() {
    }

    /**
     * Instantiates a new Restaurant.
     *
     * @param placeId       the place id
     * @param placeFormater the place formater
     */
    public Restaurant(String placeId, PlaceFormater placeFormater) {
        PlaceId = placeId;
        this.placeFormater = placeFormater;
        this.userList = new ArrayList<>();
    }

    /**
     * Gets place id.
     *
     * @return the place id
     */
    public String getPlaceId() {
        return PlaceId;
    }

    /**
     * Sets place id.
     *
     * @param placeId the place id
     */
    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    /**
     * Gets place formater.
     *
     * @return the place formater
     */
    public PlaceFormater getPlaceFormater() {
        return placeFormater;
    }

    /**
     * Sets place formater.
     *
     * @param placeFormater the place formater
     */
    public void setPlaceFormater(PlaceFormater placeFormater) {
        this.placeFormater = placeFormater;
    }

    /**
     * Gets user list.
     *
     * @return the user list
     */
    public ArrayList<String> getUserList() {
        return userList;
    }

    /**
     * Sets user list.
     *
     * @param userList the user list
     */
    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }
}
