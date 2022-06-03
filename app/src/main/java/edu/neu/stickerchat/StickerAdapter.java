package edu.neu.stickerchat;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private Context context;
    private List<Pair<String, String>> mData;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String senderRoom, receiverRoom;
    private String senderId, receiverId;
    private List<Messages> messagesList;

    public StickerAdapter(Context context, List<Pair<String, String>> mData, String receiverId) {
        this.context = context;
        this.mData = mData;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sticker_card, parent, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        messagesList = new ArrayList<>();
        senderId = auth.getUid();
        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;
        return new StickerAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = mData.get(position).second;
        String emojiName = mData.get(position).first;
        Uri uri = Uri.parse(s);
        Log.e("Getting URI", "" + uri);
        Picasso.get().load(uri).into(holder.mImage);

        holder.itemView.setOnClickListener(v -> {
            Date date = new Date();
            Messages message = new Messages(s, senderId, date.getTime());

//            DatabaseReference receiverRef = database.getReference().child("user").child(receiverId);
//            DatabaseReference senderRef = database.getReference().child("user").child(senderId);

            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .push()
                    .setValue(message).addOnCompleteListener(task -> database.getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .push()
                    .setValue(message));
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.sticker);
        }
    }
}
