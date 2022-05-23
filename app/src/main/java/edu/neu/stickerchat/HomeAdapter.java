package edu.neu.stickerchat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context homeActivity;
    List<Users> usersList;

    public HomeAdapter(HomeActivity homeActivity, List<Users> usersList) {
        this.homeActivity = homeActivity;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.user_home_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = usersList.get(position);
        String[] name = user.getUsername().split("@");
        holder.username.setText(name[0]);
        // TODO - putting user image

        holder.itemView.setOnClickListener(v -> {
            Log.d("HomeAdapter", user.getUsername());
            Intent intent = new Intent(homeActivity, ChatActivity.class);
            intent.putExtra("name", user.getUsername());
            intent.putExtra("uid", user.getUid());
            homeActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userImage;
        TextView username;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            userImage=itemView.findViewById(R.id.user_image);
            username=itemView.findViewById(R.id.username);
        }
    }
}
