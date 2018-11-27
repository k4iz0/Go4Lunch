package ltd.kaizo.go4lunch.models;

import java.util.ArrayList;

public class Restaurant {
    private String PlaceId;
    private PlaceFormater placeFormater;
    private Boolean isChosen;
    private ArrayList<String> userList;

    public Restaurant() {
    }

    public Restaurant(String placeId, PlaceFormater placeFormater, Boolean isChosen) {
        PlaceId = placeId;
        this.placeFormater = placeFormater;
        this.isChosen = isChosen;
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
        return isChosen;
    }

    public void setChoosen(Boolean choosen) {
        isChosen = choosen;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }
}
