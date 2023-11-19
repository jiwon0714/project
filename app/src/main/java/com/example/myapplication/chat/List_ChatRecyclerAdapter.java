package com.example.myapplication.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class List_ChatRecyclerAdapter extends RecyclerView.Adapter<List_ChatRecyclerAdapter.ViewHolder> {

    private ArrayList<List_FriendItem> mFriendList;

    @NonNull
    @Override
    public List_ChatRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mFriendList.get(position));
    }

    public void setFriendList(ArrayList<List_FriendItem> list){
        this.mFriendList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            message = (TextView) itemView.findViewById(R.id.message);
        }

        void onBind(List_FriendItem item){
            profile.setImageResource(item.getResourceId());
            name.setText(item.getName());
            message.setText(item.getMessage());
        }
    }
}
