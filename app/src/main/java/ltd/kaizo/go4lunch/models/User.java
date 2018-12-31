package ltd.kaizo.go4lunch.models;

import java.util.ArrayList;

/**
 * The type User.
 */
public class User {
    /**
     * The Uid.
     */
    private String uid;
    /**
     * The Url picture.
     */
    private String urlPicture;
    /**
     * The Username.
     */
    private String username;
    /**
     * The Email.
     */
    private String email;
    /**
     * The Chosen restaurant.
     */
    private String ChosenRestaurant;
    /**
     * Boolean to know if the user has choose a restaurant.
     */
    private Boolean hasChoose;
    /**
     * list of favourite restaurant
     */
    private ArrayList<String> restaurantLikeList;

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param uid        the uid
     * @param username   the username
     * @param urlPicture the url picture
     * @param email      the email
     */
    public User(String uid, String username, String urlPicture, String email) {
        this.email =email;
        this.uid = uid;
        this.urlPicture = urlPicture;
        this.username = username;
        this.ChosenRestaurant = "";
        this.restaurantLikeList = new ArrayList<>();
    }

    /**
     * Gets restaurant like list.
     *
     * @return the restaurant like list
     */
    public ArrayList<String> getRestaurantLikeList() {
        return restaurantLikeList;
    }

    /**
     * Sets restaurant like list.
     *
     * @param restaurantLikeList the restaurant like list
     */
    public void setRestaurantLikeList(ArrayList<String> restaurantLikeList) {
        this.restaurantLikeList = restaurantLikeList;
    }

    /**
     * Gets chosen restaurant.
     *
     * @return the chosen restaurant
     */
    public String getChosenRestaurant() {
        return ChosenRestaurant;
    }

    /**
     * Sets chosen restaurant.
     *
     * @param chosenRestaurant the chosen restaurant
     */
    public void setChosenRestaurant(String chosenRestaurant) {
        ChosenRestaurant = chosenRestaurant;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets has choose.
     *
     * @param placeid the placeid
     * @return the has choose
     */
    public Boolean getHasChoose(String placeid) {
        return this.getChosenRestaurant().equalsIgnoreCase(placeid);
    }

    /**
     * Sets has choose.
     *
     * @param hasChoose the has choose
     */
    public void setHasChoose(Boolean hasChoose) {
        this.hasChoose = hasChoose;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets url picture.
     *
     * @return the url picture
     */
    public String getUrlPicture() {
        return urlPicture;
    }

    /**
     * Sets url picture.
     *
     * @param urlPicture the url picture
     */
    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

}
