package edu.neu.stickerchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String receiverId, receiverName, senderId;
    private TextView receiverDisplayName;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private CardView sendSticker;
    private String senderRoom, receiverRoom;
    private RecyclerView messageRecyclerView;
    private List<Messages> messagesList;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        messagesList = new ArrayList<>();
        receiverId = getIntent().getStringExtra("uid");
        senderId = auth.getUid();
        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;
        receiverName = getIntent().getStringExtra("name");
        receiverDisplayName = findViewById(R.id.receiver_display);
        receiverDisplayName.setText(receiverName.split("@")[0]);
    }
}