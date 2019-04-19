package com.example.finalproject;

/**
 * Created by Lenovo on 12/6/2017.
 */

public class Movie {
    String title;
    String desc;
    String url;
    String id;
    String image;
    String cast;
    String genre;
    int viewsCount;
    float avgRating;
    UserRating userRating;
    String currentDateTime;

    public Movie() {
    }

    public Movie(String title, String desc, String url, String id,String cast,String currentDateTime,String genre) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.id = id;
        this.cast=cast;
        this.genre=genre;
        this.currentDateTime=currentDateTime;
    }

    public Movie(String title, String desc, String url, String id, String image, UserRating userRating) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.id = id;
        this.image = image;
        this.userRating = userRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
