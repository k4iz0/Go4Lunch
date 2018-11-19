package ltd.kaizo.go4lunch.models;

import java.util.ArrayList;

public class Restaurant {
    private String PlaceId;
    private PlaceFormater placeFormater;
    private Boolean isChoosen;
    private ArrayList<User> userList;

    public Restaurant() {
    }

    public Restaurant(String placeId, PlaceFormater placeFormater, Boolean isChoosen) {
        PlaceId = placeId;
        this.placeFormater = placeFormater;
        this.isChoosen = isChoosen;
        this.userList = new ArrayList<>();
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public PlaceFormater getPlaceFormater() {
        return placeFormater;
    }

    public void setPlaceFormater(PlaceFormater placeFormater) {
        this.placeFormater = placeFormater;
    }

    public Boolean getChoosen() {
        return isChoosen;
    }

    public void setChoosen(Boolean choosen) {
        isChoosen = choosen;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }
}
