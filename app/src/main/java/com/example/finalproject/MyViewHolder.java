package com.example.finalproject;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Lenovo on 12/9/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvViews;
    ImageView movieImage;
    ImageView overflow;
    TextView tvRatings;
    CardView txtContainer;
    RatingBar avgRatingBar;
    TextView tvDate;
    TextView etGenre;

    public MyViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvViews = (TextView) itemView.findViewById(R.id.tvViews);
        tvRatings = (TextView) itemView.findViewById(R.id.tvRating);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        etGenre = (TextView) itemView.findViewById(R.id.etGenre);
        movieImage = (ImageView) itemView.findViewById(R.id.movieImage);
        txtContainer = (CardView) itemView.findViewById(R.id.txtContainer);
        overflow = (ImageView) itemView.findViewById(R.id.overflow);
        avgRatingBar=(RatingBar) itemView.findViewById(R.id.avgLRating);
    }
}