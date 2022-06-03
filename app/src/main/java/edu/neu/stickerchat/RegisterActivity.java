package edu.neu.stickerchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    String TAG = "RegisterActivityDebug";

    TextView login_already_account, signup_button;
    EditText username_signup;
    EditText password_signup;

    ProgressBar progressBar;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // find view by id
        login_already_account = findViewById(R.id.signin_bottom);
        signup_button = findViewById(R.id.signup_btn);
        username_signup = findViewById(R.id.username_signup);
        password_signup = findViewById(R.id.password_signup);
        progressBar = findViewById(R.id.progressBar_signup);

        // firebase instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // clicking on sign up
        signup_button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String username = username_signup.getText().toString().trim();
            String password = password_signup.getText().toString();
            // invalid username
            if (TextUtils.isEmpty(username)) {
                // Toast.makeText(SignupActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
                username_signup.setError("Please enter username");
                username_signup.requestFocus();
                progressBar.setVisibility(View.INVISIBLE);
            }
            // invalid password
            else if (TextUtils.isEmpty(password)) {
                // Toast.makeText(SignupActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
                password_signup.setError("Please enter password");
                password_signup.requestFocus();
                progressBar.setVisibility(View.INVISIBLE);
            }
            // minimum length of password should be 6
            else if (password.length() < 6) {
                // Toast.makeText(SignupActivity.this, "Password should be longer than 6 char", Toast.LENGTH_LONG).show();
                password_signup.setError("Password should be longer than 6 char");
                password_signup.requestFocus();
                progressBar.setVisibility(View.INVISIBLE);
            }
            // valid credentials, create account with username and password
            else {
                // add username with password to firebase
                username += "@gmail.com";
                String usernameDB = username;
                Log.d(TAG, username + " " + password);
                auth.createUserWithEmailAndPassword(usernameDB, password).addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Created an account with credentials");
                        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                        Users user = new Users(auth.getUid(), usernameDB);
                        reference.setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.putExtra("user", usernameDB);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Error creating user account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Log.d("Register", "User already exists");
                        Toast.makeText(RegisterActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            progressBar.setVisibility(View.INVISIBLE);
        });

        // start the LoginActivity page when already a member
        login_already_account.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            RegisterActivity.this.finish();
        });
    }
}