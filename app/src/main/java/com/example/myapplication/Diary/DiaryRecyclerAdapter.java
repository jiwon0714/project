package com.example.myapplication.Diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.chat.FriendItem;
import com.example.myapplication.chat.MyRecyclerAdapter;

import java.util.ArrayList;

public class DiaryRecyclerAdapter extends RecyclerView.Adapter<DiaryRecyclerAdapter.ViewHolder> {
    private ArrayList<DiaryItem> mDiaryList;

    @NonNull
    @Override
    public DiaryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item_recyclerview, parent, false);
        return new DiaryRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(mDiaryList.get(position));
    }

    public void setDiaryList(ArrayList<DiaryItem> list){
        this.mDiaryList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView title;
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (ImageView) itemView.findViewById(R.id.profile);
            title = (TextView) itemView.findViewById(R.id.title);
            message = (TextView) itemView.findViewById(R.id.message);
        }

        void onBind(DiaryItem item){
            profile.setImageResource(item.getResourceId());
            title.setText(item.getTitle());
            message.setText(item.getMessage());
        }
    }
}

