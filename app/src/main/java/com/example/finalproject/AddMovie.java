package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class AddMovie extends AppCompatActivity {

    Button addMovie;
    Button addImageBtn;
    EditText etTitle;
    EditText etDesc;
    EditText etUrl;
    EditText etCast;
    EditText etGenre;
    ImageView movieImage;
    Uri selectedimg;
    private final int PICK_IMAGE = 100;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference movieReference;
    FirebaseStorage storage;
    ArrayList<Movie> movies;
    String updateId;
    ProgressDialog pd;
    String downloadUrlStr;
    boolean check;
    String key;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);


        etTitle = (EditText) findViewById(R.id.etTitle);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etUrl = (EditText) findViewById(R.id.etUrl);
        etCast = (EditText) findViewById(R.id.etCast);
        etGenre = (EditText) findViewById(R.id.etGenre);
        movieImage = (ImageView) findViewById(R.id.movieImage);
        addMovie = (Button) findViewById(R.id.addMovie);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        movieReference = databaseReference.child("movies");
        storage = FirebaseStorage.getInstance();
        movies = new ArrayList();
        pd = new ProgressDialog(this);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            check = true;
            etTitle.setText(bundle.getString("title"));
            etDesc.setText(bundle.getString("desc"));
            etUrl.setText(bundle.getString("url"));
            etCast.setText(bundle.getString("movieCast"));
            etGenre.setText(bundle.getString("genre"));
            updateId = bundle.getString("id");

            Glide.with(movieImage.getContext())
                    .load(bundle.get("image"))
                    .into(movieImage);
        }

        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

                title = etTitle.getText().toString();
                String desc = etDesc.getText().toString();
                String url = etUrl.getText().toString();
                String cst = etCast.getText().toString();
                String genre = etGenre.getText().toString();
                savedData(title, desc, url, cst, currentDateTime,genre);
                pd.setTitle("Uploading...");
                pd.show();
                if (check)
                    movieReference.child(updateId).child("image").setValue(bundle.get("image"));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        pd.dismiss();
                        startActivity(new Intent(AddMovie.this, MoviesList.class));
                        if(updateId==null)
                        sendNotification();
                        finish();
                    }
                }, 8000);
            }

        });
    }

    private void sendNotification() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic Y2Q5OWE5YzctODY0Ny00YzhkLWEwOWMtYzQxZTc1MDNkZjcy");
                        con.setRequestMethod("POST");

                        String text="New "+title+" MOvie is added.";

                        String strJsonBody = "{"
                                + "\"app_id\": \"20fd7e44-3d30-424f-8276-45e77eeb8e83\","

                                + "     \"included_segments\": [\"All\"],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \""+ text + "\"}"
                                + "}";

                        //System.out.\println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    private void savedData(String title, String desc, String url, String cast, String time,String genre) {
        key = updateId;
        if (updateId == null) {
            key = databaseReference.push().getKey();
        }

       // uploadImage(selectedimg, key);
        Movie movie = new Movie(title, desc, url, key, cast, time,genre);
        movieReference.child(key).setValue(movie);
        etTitle.setText("");
        etDesc.setText("");
        etUrl.setText("");
        movieImage.setImageResource(0);
        pd.dismiss();
    }

    /*private void uploadImage(Uri selectedimg, final String key) {
        if (selectedimg == null) {
            return;
        }
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child(key + ".jpg");

        UploadTask uploadTask = fileRef.putFile(selectedimg);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                downloadUrlStr = downloadUrl.toString();
                movieReference.child(key).child("image").setValue(downloadUrlStr);
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        // percentage in progress dialog
                        pd.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            selectedimg = data.getData();
            try {
                movieImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
