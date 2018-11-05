package ltd.kaizo.go4lunch.models;

import java.util.ArrayList;
import java.util.List;

public class Restaurants {
    private String PlaceId;
    private PlaceFormater placeFormater;
    private Boolean isChoosen;
    private List<User> userList;

    public Restaurants(String placeId, PlaceFormater placeFormater, Boolean isChoosen) {
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
