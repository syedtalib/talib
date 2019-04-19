package com.example.finalproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MovieDetail extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, YouTubeThumbnailView.OnInitializedListener {

    TextView title;
    TextView desc;
    TextView tvCast;
    TextView tvGenre;
    TextView viewsTextView;
    TextView rateHeading;
    String movieId;
    EditText comment;
    RatingBar rating;
    ImageView movieImage;
    ImageView genreImageView;
    Button commentSubmit;
    Button rateSubmit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference viewReference;
    DatabaseReference userRatingRef;
    DatabaseReference movieRef;
    private FirebaseAuth mAuth;
    String uid;
    String Name;
    ArrayList<UserRating> userRatings;
    UserAdapter userAdapter;
    RecyclerView recyclerView;
    String mtitle;
    String mdesc;
    String mCast;
    String murl;
    String mGenre;
    String mimage;
    ProgressDialog pd;
    float totalRating;
    RatingBar avgRating;
    TextView avgRatingNum;
    float average;
    float uRating;
    int count;
    long viewsCount;
    long viewsCount1;
    Boolean check;
    ImageView playBtn;
    YouTubeThumbnailView thumbnail;
    YouTubePlayerView player;
    String API_key, VIDEO_ID;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        check = false;
        API_key = "AIzaSyDjTQU7BRCisS9hNirJYgT6BuHgr4LShbM";
        VIDEO_ID = "alQlJDRnQkE";

        thumbnail = (YouTubeThumbnailView) findViewById(R.id.thumbnail);
        player = (YouTubePlayerView) findViewById(R.id.player);
        thumbnail.initialize(API_key, this);

        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
        tvCast = (TextView) findViewById(R.id.tvCast);
        tvGenre = (TextView) findViewById(R.id.genre);
        movieImage = (ImageView) findViewById(R.id.movieImage);
        genreImageView = (ImageView) findViewById(R.id.genreImageView);
        playBtn = (ImageView) findViewById(R.id.playBtn);
        commentSubmit = (Button) findViewById(R.id.commentSubmit);
        rateSubmit = (Button) findViewById(R.id.rateSubmit);
        comment = (EditText) findViewById(R.id.comment);
        rating = (RatingBar) findViewById(R.id.userRatings);
        avgRating = (RatingBar) findViewById(R.id.avgRating);
        avgRatingNum = (TextView) findViewById(R.id.ratingTextView);
        viewsTextView = (TextView) findViewById(R.id.viewsTextView);
        rateHeading = (TextView) findViewById(R.id.rateHeading);

        firebaseDatabase = FirebaseDatabase.getInstance();
        movieRef = firebaseDatabase.getReference().child("movies");
        mAuth = FirebaseAuth.getInstance();
        userRatings = new ArrayList<>();
        pd = new ProgressDialog(this);

        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);


        rateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateSubmit.getText().equals("EDIT")){
                    rateHeading.setText("Rate This Movie");
                    rating.setIsIndicator(false);
                    rating.setRating(0);
                    rateSubmit.setText("SUBMIT");
                }
                if (rating.getRating() != 0) {
                    uRating = rating.getRating();
                    viewFlipper.showNext();
                }
            }

        });

        userAdapter = new UserAdapter(userRatings);
        recyclerView = (RecyclerView) findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mtitle = bundle.getString("title");
            mdesc = bundle.getString("desc");
            murl = bundle.getString("url");
            mCast = bundle.getString("movieCast");
            mGenre = bundle.getString("genre");
            movieId = bundle.getString("movieId");
            mimage = bundle.get("image").toString();

            title.setText(mtitle);
            desc.setText(mdesc);
            tvCast.setText(mCast);
            tvGenre.setText(mGenre);

            Glide.with(movieImage.getContext())
                    .load(bundle.get("image"))
                    .into(movieImage);
        }

        if(mtitle.equals("The Avengers") || mtitle.equals("The Matrix") || mtitle.equals("Batman vs. Two-Face"))
            genreImageView.setImageResource(R.drawable.action);
        else if(mtitle.equals("Hitman"))
            genreImageView.setImageResource(R.drawable.crime);
        else if(mtitle.equals("The Boss Baby")|| mtitle.equals("The Emoji Movie"))
            genreImageView.setImageResource(R.drawable.comedy);
        else if(mGenre.equals("Justice League Dark"))
            genreImageView.setImageResource(R.drawable.fantasy);



        userRatingRef = firebaseDatabase.getReference().child("movies").child(movieId).child("userRatings");
        userRatingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                DecimalFormat df = new DecimalFormat("#.#");
                UserRating userRating = dataSnapshot.getValue(UserRating.class);
                totalRating += userRating.getRating();
                count++;
                average = Float.valueOf(df.format(totalRating / count));
                avgRating.setRating(average);
                avgRatingNum.setText(String.valueOf(average));
                userRatings.add(userRating);
                userAdapter.notifyDataSetChanged();
                movieRef.child(movieId).child("avgRating").setValue(average);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.child("uId").getValue().toString().equals(mAuth.getCurrentUser().getUid()))
                    {
                        rateHeading.setText("You Rated");
                        rating.setRating(Float.valueOf(data.child("rating").getValue().toString()));
                        rating.setIsIndicator(true);
                        rateSubmit.setText("EDIT");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.show();
                uid = mAuth.getCurrentUser().getUid();

                Name = MoviesList.uName;
                if (Name.equals("")) {
                    Name = "admin";
                }

                final String cmt = comment.getText().toString();
                UserRating userRating = new UserRating(Name, uid, uRating, cmt);
                userRatingRef.push().setValue(userRating);
                Toast.makeText(MovieDetail.this, "Posted", Toast.LENGTH_SHORT).show();
                viewFlipper.showPrevious();
                comment.setText("");
                rating.setRating(0);
                pd.dismiss();
            }
        });

        viewReference = firebaseDatabase.getReference().child("movies").child(movieId).child("views");
        viewReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                viewsCount = dataSnapshot.getChildrenCount();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue().equals(mAuth.getCurrentUser().getUid())) {
                        viewsTextView.setText(String.valueOf(viewsCount));
                        movieRef.child(movieId).child("viewsCount").setValue(viewsCount);
                        return;
                    }
                }
                viewReference.push().setValue(mAuth.getCurrentUser().getUid());
                viewsTextView.setText(String.valueOf(viewsCount + 1));
                movieRef.child(movieId).child("viewsCount").setValue(viewsCount+1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playBtn.setVisibility(View.GONE);
                thumbnail.setVisibility(View.GONE);
                player.setVisibility(View.VISIBLE);
                player.initialize(API_key, MovieDetail.this);

            }
        });

    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader thumbnailLoader) {

        youTubeThumbnailLoader = thumbnailLoader;
        thumbnailLoader.setOnThumbnailLoadedListener(new ThumbnailListener());
        youTubeThumbnailLoader.setVideo(murl);

    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

        Toast.makeText(this, "Load Fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.loadVideo(murl);
        youTubePlayer.seekToMillis(1);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private final class ThumbnailListener implements
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView thumbnail, String videoId) {
            /*Toast.makeText(getApplicationContext(),
                    "onThumbnailLoaded", Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView thumbnail,
                                     YouTubeThumbnailLoader.ErrorReason reason) {
            Toast.makeText(getApplicationContext(),
                    "onThumbnailError", Toast.LENGTH_SHORT).show();
        }
    }
}
