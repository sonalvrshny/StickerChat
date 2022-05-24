package edu.neu.stickerchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        sendSticker = findViewById(R.id.enter_sticker_button);
        messageRecyclerView = findViewById(R.id.MessageRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(ChatActivity.this, messagesList);
        messageRecyclerView.setAdapter(chatAdapter);

//        DatabaseReference reference = database.getReference().child("user").child(senderId);
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages message = dataSnapshot.getValue(Messages.class);
                    messagesList.add(message);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendSticker.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, StickerActivity.class);
            intent.putExtra("recId", receiverId);
            startActivity(intent);
        });
    }
}