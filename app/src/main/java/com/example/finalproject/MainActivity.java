package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;


public class MainActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    ProgressDialog pd;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");

        Button LoginBtn = (Button) findViewById(R.id.loginBtn);
        TextView signUpTv = (TextView) findViewById(R.id.signUpTv);
        signUpTv.setPaintFlags(signUpTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        if (mAuth.getCurrentUser() != null) {

            startActivity(new Intent(MainActivity.this, MoviesList.class));
            finish();
        }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                if (email.isEmpty())
                    etEmail.setError("Required Field");
                if (pass.isEmpty())
                    etPassword.setError("Required Field");
                else
                    signIn(email, pass);
            }
        });
    }
    private void signIn(final String email, String password) {
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            currentUserEmail = mAuth.getCurrentUser().getEmail();
                            OneSignal.startInit(MainActivity.this).init();
                            OneSignal.sendTag("User_ID", currentUserEmail);
                            startActivity(new Intent(MainActivity.this, MoviesList.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUpTv(View view) {
        startActivity(new Intent(MainActivity.this, SignUp.class));
    }


}
