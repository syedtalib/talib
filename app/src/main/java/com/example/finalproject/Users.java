package com.example.finalproject;

/**
 * Created by Lenovo on 12/9/2017.
 */

public class Users {
    String id;
    String name;

    public Users() {
    }

    public Users(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
