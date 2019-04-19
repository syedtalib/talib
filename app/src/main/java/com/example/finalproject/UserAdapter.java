package com.example.finalproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/20/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    ArrayList<UserRating> users;

    public UserAdapter(ArrayList<UserRating> users) {
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        UserRating userRating = users.get(position);

        if (userRating != null) {

            holder.name.setText(userRating.getuName());
            holder.comment.setText(userRating.getComment());
            holder.rating.setRating(userRating.getRating());
            holder.userRatingNum.setText(String.valueOf(userRating.getRating()));

            StringBuilder stringBuilder=new StringBuilder(userRating.getuName().substring(0,1));
            stringBuilder.setCharAt(0,Character.toUpperCase(stringBuilder.charAt(0)));
            holder.uAlpha.setText(stringBuilder.toString());
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
