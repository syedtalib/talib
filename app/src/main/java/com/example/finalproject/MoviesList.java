package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MoviesList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference uIdReference;
    DatabaseReference nameReference;
    DatabaseReference viewReference;
    ProgressDialog pd;
    String uidFromDb;
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<Movie> movies;
    public static String uName = "";
    FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference().child("movies");
        nameReference = firebaseDatabase.getReference().child("SinUpUsers");
        uIdReference = firebaseDatabase.getReference();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addBtn = (FloatingActionButton) findViewById(R.id.addMovie);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();

        movies = new ArrayList();
        adapter = new MyAdapter(this, movies, new movieItemListener() {
            @Override
            public void onUpdate(Movie movie) {
                startActivity(new Intent(MoviesList.this, AddMovie.class)
                        .putExtra("title", movie.getTitle())
                        .putExtra("desc", movie.getDesc())
                        .putExtra("url", movie.getUrl())
                        .putExtra("movieCast", movie.getCast())
                        .putExtra("genre", movie.getGenre())
                        .putExtra("image", movie.getImage())
                        .putExtra("id", movie.getId()));
            }

            @Override
            public void onDelete(Movie movie, int position) {
                databaseReference.child(movie.getId()).setValue(null);
                movies.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        uIdReference.child("users").child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    uidFromDb = dataSnapshot.getValue().toString();
                    pd.dismiss();

                    if (!mAuth.getCurrentUser().getUid().equals(uidFromDb)) {
                        addBtn.setVisibility(View.GONE);
                    } else {
                        addBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = mAuth.getCurrentUser().getUid();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot1.getValue(Users.class);
                    if (users.getId().equals(id)) {
                        uName = users.getName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movie movie = dataSnapshot.getValue(Movie.class);
                movies.add(movie);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Movie movie = dataSnapshot.getValue(Movie.class);
                int indexOfItem = movies.indexOf(movie);
                if (indexOfItem >= 0) {
                    movies.set(indexOfItem, movie);

                }
                adapter.notifyDataSetChanged();
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
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoviesList.this, AddMovie.class));
                addBtn.setVisibility(View.VISIBLE);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu, menu);
        getMenuInflater().inflate(R.menu.search_movie, menu);

        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAuth.signOut();
        startActivity(new Intent(MoviesList.this, MainActivity.class));
        finish();
        return true;
    }

    public ArrayList<Movie> Search(ArrayList<Movie> studentList, String query) {
        query = query.toLowerCase();
        final ArrayList<Movie> searchList = new ArrayList<>();
        for (Movie s : studentList) {
            final String name = s.getTitle().toLowerCase();
            if (name.contains(query)) {
                searchList.add(s);
            }
        }
        return searchList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<Movie> searchList = Search(movies, newText);

        if (searchList.size() > 0) {
            adapter = new MyAdapter(searchList);
            recyclerView.setAdapter(adapter);
            adapter.setFilter(searchList);
            return true;
        } else {
            Toast.makeText(MoviesList.this, "No Record Found", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
            return false;
        }
    }
}


