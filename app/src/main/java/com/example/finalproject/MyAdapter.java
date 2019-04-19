package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 12/9/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    ArrayList<Movie> movies;
    movieItemListener listener;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DecimalFormat df;
    DatabaseReference uIdReference;
    private FirebaseAuth mAuth;
    String uidFromDb;


    public MyAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public MyAdapter(@NonNull Context context, ArrayList<Movie> movies, movieItemListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        uIdReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        df = new DecimalFormat("#.#");
        final Movie movie = movies.get(position);

        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        final String saveTime = movie.getCurrentDateTime();

        holder.tvRatings.setText(df.format(movie.getAvgRating()));
        holder.tvDate.setText(getTimeDiff(currentTime, saveTime));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("movies");

        databaseReference.child(movie.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object avg = dataSnapshot.child("avgRating").getValue();
                Object viewsCount = dataSnapshot.child("viewsCount").getValue();
                if (avg != null)
                    holder.tvRatings.setText(df.format(avg));
                if (viewsCount!=null) {
                    String strViews = viewsCount.toString() + (Long.valueOf(viewsCount.toString()) == 1 ? " view" : " views");
                    holder.tvViews.setText(strViews);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.tvTitle.setText(movie.getTitle());

        Glide.with(holder.movieImage.getContext())
                .load(movie.getImage())
                .into(holder.movieImage);

        holder.txtContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = movie.getTitle();
                String desc = movie.getDesc();
                String url = movie.getUrl();
                String image = movie.getImage();
                String id = movie.getId();
                String cast = movie.getCast();
                String genre = movie.getGenre();

                context.startActivity(new Intent(context, MovieDetail.class)
                        .putExtra("title", title)
                        .putExtra("desc", desc)
                        .putExtra("url", url)
                        .putExtra("image", image)
                        .putExtra("movieId", id)
                        .putExtra("genre", genre)
                        .putExtra("movieCast", cast));
            }
        });
        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = movie.getTitle();
                String desc = movie.getDesc();
                String url = movie.getUrl();
                String image = movie.getImage();
                String id = movie.getId();
                String cast = movie.getCast();
                String genre = movie.getGenre();

                context.startActivity(new Intent(context, MovieDetail.class)
                        .putExtra("title", title)
                        .putExtra("desc", desc)
                        .putExtra("url", url)
                        .putExtra("image", image)
                        .putExtra("movieId", id)
                        .putExtra("genre", genre)
                        .putExtra("movieCast", cast));
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position, movie);
            }
        });

        uIdReference.child("users").child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    uidFromDb = dataSnapshot.getValue().toString();

                    if (!mAuth.getCurrentUser().getUid().equals(uidFromDb)) {
                        holder.overflow.setVisibility(View.GONE);
                    } else {
                        holder.overflow.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getTimeDiff(String currentTime, String saveTime) {
        String diff = "";

        DateFormat df = DateFormat.getDateTimeInstance();
        try {
            if (currentTime != null && saveTime != null) {
                Date date1 = df.parse(currentTime);
                Date date2 = df.parse(saveTime);

                diff = printDifference(date1, date2);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }


    public String printDifference(Date startDate, Date endDate) {

        long different = startDate.getTime() - endDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;

        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        String days = String.valueOf(elapsedDays);
        String hours = String.valueOf(elapsedHours);
        String minutes = String.valueOf(elapsedMinutes);
        String seconds = String.valueOf(elapsedSeconds);

        String elapse = seconds + (elapsedSeconds == 1 ? " second" : " seconds");
        if (elapsedDays > 0)
            elapse = days + (elapsedDays == 1 ? " day" : " days");
        else if (elapsedHours > 0)
            elapse = hours + (elapsedHours == 1 ? " hour" : " hours");
        else if (elapsedMinutes > 0)
            elapse = minutes + (elapsedMinutes == 1 ? " minute" : " minutes");
        return elapse + " ago";
    }

    private void showPopupMenu(View view, final int position, final Movie movie) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.movie_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        listener.onUpdate(movie);
                        notifyDataSetChanged();
                        return true;
                    case R.id.action_delete:
                        listener.onDelete(movie, position);
                        return true;
                    default:
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setFilter(ArrayList<Movie> filter) {
        movies = new ArrayList<>();
        movies.addAll(filter);
        notifyDataSetChanged();
    }
}