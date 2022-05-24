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

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private Context context;
    private List<Pair<String, String>> mData;
    private FirebaseDatabase database;
    private FirebaseDatabase auth;
    private String senderRoom, receiverRoom;
    private String senderId, receiverId;

    public StickerAdapter(Context context, List<Pair<String, String>> mData, String receiverId) {
        this.context = context;
        this.mData = mData;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sticker_card, parent, false);
        return new StickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = mData.get(position).second;
        String emojiName = mData.get(position).first;
        Uri uri = Uri.parse(s);
        Log.e("Getting URI", "" + uri);
        Picasso.get().load(uri).into(holder.mImage);
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
