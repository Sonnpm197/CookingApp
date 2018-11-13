package fpt.android.com.appnauan.Models;

import fpt.android.com.appnauan.Entities.Food;

public class ChatModel {
    private String message;
    private boolean userSend, clickable; // if clickable = true then this item can be clicked on listView
    private Food food; // food to show on detailActivity

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

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
