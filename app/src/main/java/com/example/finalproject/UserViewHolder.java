package com.example.finalproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Lenovo on 2/22/2018.
 */

class UserViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView comment;
    TextView userRatingNum;
    RatingBar rating;
    TextView uAlpha;

    public UserViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.userName);
        comment = (TextView) itemView.findViewById(R.id.userComment);
        userRatingNum = (TextView) itemView.findViewById(R.id.userRatingNum);
        rating = (RatingBar) itemView.findViewById(R.id.userRating);
        uAlpha = (TextView) itemView.findViewById(R.id.uAlpha);
    }
}
