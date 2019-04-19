package com.example.finalproject;

/**
 * Created by Lenovo on 3/1/2018.
 */

public class MovieView {

    String userId;

    public MovieView() {
    }

    public MovieView(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
