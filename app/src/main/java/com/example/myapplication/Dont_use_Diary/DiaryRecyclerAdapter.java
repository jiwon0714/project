package com.example.myapplication.Dont_use_Diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

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
        ImageView image;
        TextView title;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.profile);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
        }

        void onBind(DiaryItem item){
            image.setImageResource(item.getResourceId());
            title.setText(item.getTitle());
            date.setText(item.getMessage());
        }
    }
}

