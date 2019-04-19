package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText fullName;
    EditText Email;
    EditText Pass;
    Button signUpBtn;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = (EditText) findViewById(R.id.full_name);
        Email = (EditText) findViewById(R.id.email);
        Pass = (EditText) findViewById(R.id.password);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = fullName.getText().toString();
                final String email = Email.getText().toString();
                final String pass = Pass.getText().toString();

                if (validation(name, email, pass)) {
                    signUp(email, pass, name);
                  /*  String key = databaseReference.child("SinUpUsers").push().getKey();
                    Users users = new Users(name, key);
                    databaseReference.child("SinUpUsers").child(key).setValue(users);*/

                }
            }
        });
    }

    private boolean validation(String name, String email, String pass) {

        boolean valid = true;
        if (name.isEmpty() || name.length() < 3) {
            fullName.setError("At least 3 characters");
            valid = false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email address");
            valid = false;
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            Pass.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        }

        return valid;
    }

    private void signUp(String email, String password, final String name) {
        pd.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            String key = databaseReference.child("SinUpUsers").push().getKey();
                            Users users = new Users(name, uid);
                            databaseReference.child("SinUpUsers").child(key).setValue(users);
                            startActivity(new Intent(SignUp.this, MoviesList.class));
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Login Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
