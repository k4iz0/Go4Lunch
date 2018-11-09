package ltd.kaizo.go4lunch.models;

public class User {
    private String uid;
    private String urlPicture;
    private String username;

    public User() {
    }

    public User(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.urlPicture = urlPicture;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}