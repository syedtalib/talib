package com.example.finalproject;

/**
 * Created by Lenovo on 12/9/2017.
 */

public interface movieItemListener {

    void onUpdate(Movie contact);

    void onDelete(Movie contact, int pos);
}
