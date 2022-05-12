package edu.neu.stickerchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivityDebug";

    TextView register_new_account, signin_button;
    EditText username_signin;
    EditText password_signin;

    ProgressBar progressBar;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find view by id
        register_new_account = findViewById(R.id.signup_bottom);
        signin_button = findViewById(R.id.signin_btn);
        username_signin = findViewById(R.id.username_login);
        password_signin = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progressBar_login);

        // firebase instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // clicking on sign in
        signin_button.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String username = username_signin.getText().toString().trim();
            String password = password_signin.getText().toString();
            // invalid username
            if (TextUtils.isEmpty(username)) {
                // Toast.makeText(SignupActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
                username_signin.setError("Please enter username");
                username_signin.requestFocus();
                progressBar.setVisibility(View.INVISIBLE);
            }
            // invalid password
            else if (TextUtils.isEmpty(password)) {
                // Toast.makeText(SignupActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
                password_signin.setError("Please enter password");
                password_signin.requestFocus();
                progressBar.setVisibility(View.INVISIBLE);
            }
            else {
                username += "@gmail.com";
                String finalUsername = username;
                auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                        Users users = new Users(auth.getUid(), finalUsername);
                        reference.setValue(users).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("user", finalUsername);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Error in logging in", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // start the RegisterActivity page when creating new account
        register_new_account.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            LoginActivity.this.finish();
        });
    }
}