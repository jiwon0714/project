package com.example.myapplication.sns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {
    private ArrayList<SnsItem> mSnsList;

    @NonNull
    @Override
    public SnsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sns_item_recyclerview, parent, false);
        return new SnsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mSnsList.get(position));
    }

    public void setSnsList(ArrayList<SnsItem> list){
        this.mSnsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSnsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        ImageView main_image;
        TextView name;
        TextView text;
        TextView comment;
        TextView comment_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_name = (TextView) itemView.findViewById(R.id.comment_name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            main_image = (ImageView) itemView.findViewById(R.id.main_image);

            profile = (ImageView) itemView.findViewById(R.id.img_profile);
            name = (TextView) itemView.findViewById(R.id.tv_profile);
            text = (TextView) itemView.findViewById(R.id.sns_main);


        }

        void onBind(SnsItem item){
            profile.setImageResource(item.getResourceId_profile());
            main_image.setImageResource(item.getResourceId_main_image());

            name.setText(item.getName());
            text.setText(item.getText());
            comment.setText(item.getComment());
            comment_name.setText(item.getComment_name());
        }
    }
}
