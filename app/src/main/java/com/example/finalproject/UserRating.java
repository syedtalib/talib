package com.example.finalproject;

/**
 * Created by Lenovo on 12/20/2017.
 */

public class UserRating {
    String uName;
    String uId;
    float rating;
    String comment;


    public UserRating() {
    }

    public UserRating(String uName, String uId, float rating, String comment) {
        this.uName = uName;
        this.uId = uId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
