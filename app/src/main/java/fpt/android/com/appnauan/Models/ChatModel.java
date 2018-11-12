package fpt.android.com.appnauan.Models;

public class ChatModel {
    public String message;
    public boolean userSend;

    public ChatModel(String message, boolean userSend) {
        this.message = message;
        this.userSend = userSend;
    }

    public ChatModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUserSend() {
        return userSend;
    }

    public void setUserSend(boolean userSend) {
        this.userSend = userSend;
    }
}
