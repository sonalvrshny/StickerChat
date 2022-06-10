package edu.neu.stickerchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<Users> usersList;
    private boolean backPressed = false;

    private void logOut() {
        DatabaseReference reference = db.getReference().child("user");
        DatabaseReference user = reference.child(auth.getUid());
        SharedPrefUtils.saveEmail(null, this);
        SharedPrefUtils.savePassword(null, this);
        auth.signOut();
        this.finish();
    }

//    @Override
//    public void onBackPressed() {
//        if (backPressed) {
////            logOut();
//            super.onBackPressed();
//        } else {
//            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG).show();
//            backPressed = true;
//            new Handler().postDelayed(() -> {
//                backPressed = false;
//            }, 1500);
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usersList = new ArrayList<>();

        String username = getIntent().getStringExtra("user");
        DatabaseReference reference = db.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    assert user != null;
                    Log.d("HomeActivity", user.getUsername() + " " + username);
                    if (!user.getUsername().equals(username)) {
                        usersList.add(user);
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView = findViewById(R.id.ChatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeAdapter = new HomeAdapter(HomeActivity.this, usersList);
        recyclerView.setAdapter(homeAdapter);
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("Home", item.toString());
        if (item.getItemId() == R.id.logout) {
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}