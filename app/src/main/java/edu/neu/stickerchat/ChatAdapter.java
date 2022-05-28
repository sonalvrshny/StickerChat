package edu.neu.stickerchat;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    private Context context;
    List<Messages> messagesList;
    private static final int SEND = 1;
    private static final int RECEIVE = 2;

    public ChatAdapter(Context context, List<Messages> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ChatAd", "viewType: " + viewType);
        if (viewType == SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_card, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_card, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = messagesList.get(position);
        Log.d("ChatAd", holder.getClass().toString());
        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            Uri uri = Uri.parse(message.getMessage());
            Log.d("ChatAdapter", uri.toString());
            Picasso.get().load(uri).into(senderViewHolder.senderImage);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(message.getTimestamp());
            Date d = c.getTime();
            DateFormat dateFormat = DateFormat.getDateInstance();
            DateFormat timeFormat = DateFormat.getTimeInstance();
            senderViewHolder.senderDate.setText(dateFormat.format(d));
            senderViewHolder.senderTime.setText(timeFormat.format(d));
        } else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            Uri uri = Uri.parse(message.getMessage());
            Picasso.get().load(uri).into(receiverViewHolder.receiverImage);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(message.getTimestamp());
            Date d = c.getTime();
            DateFormat dateFormat = DateFormat.getDateInstance();
            DateFormat timeFormat = DateFormat.getTimeInstance();
            receiverViewHolder.receiverDate.setText(dateFormat.format(d));
            receiverViewHolder.receiverTime.setText(timeFormat.format(d));
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages message = messagesList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())) {
            return SEND;
        } else {
            return RECEIVE;
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ImageView receiverImage;
        TextView receiverDate;
        TextView receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverImage = itemView.findViewById(R.id.receiver_image);
            receiverDate = itemView.findViewById(R.id.receiver_date);
            receiverTime = itemView.findViewById(R.id.receiver_time);
        }
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        ImageView senderImage;
        TextView senderDate;
        TextView senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderImage = itemView.findViewById(R.id.sender_image);
            senderDate = itemView.findViewById(R.id.sender_date);
            senderTime = itemView.findViewById(R.id.sender_time);
        }
    }
}
