package net.fitken.simplechat;

/**
 * Created by Ken on 3/2/2017.
 */

public class MessageObject {
    private String message;
    private String userId;
    private String imgProfile;

    public MessageObject() {
    }

    public MessageObject(String message, String userId, String imgProfile) {
        this.message = message;
        this.userId = userId;
        this.imgProfile = imgProfile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }
}
