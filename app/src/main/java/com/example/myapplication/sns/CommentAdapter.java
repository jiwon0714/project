package com.example.myapplication.sns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<com.example.myapplication.sns.CommentAdapter.ViewHolder> {
    private  List<Comment_Item> comments;

    public CommentAdapter(List<Comment_Item> comment) {
        this.comments = comment;
    }

    private int click = 0;
    private int numofheart =0;

    @NonNull
    @Override
    public com.example.myapplication.sns.CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new com.example.myapplication.sns.CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comment_Item comment = comments.get(position);

        holder.tv_comment.setText(comment.getcommentContext());
        holder.tv_CommentTime.setText(comment.getcommentTime());
//        holder.profile.setImageBitmap(comment.getProfileImage());
//        holder.name.setText(comment.getname());
        holder.heartCounter.setText(String.valueOf(comment.getHeartCount())); // Convert int to String

//        holder.heart.setOnClickListener(new View.OnClickListener() {
//
//
//            //여긴 boolean으로 처리할 건지 확인 해야함
//            @Override
//            public void onClick(View v) {
//                if(click ==0) {
//                    holder.heart.setImageResource(R.drawable.fillheart);
//                    numofheart++;
//                    holder.heartCounter.setText(numofheart);
//                    click ++;
//                }else{
//                    holder.heart.setImageResource(R.drawable.heart);
//                    --numofheart;
//                    holder.heartCounter.setText(numofheart);
//                    click --;
//                }
//            }
//        });

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment.isHeart()) {
                    // 좋아요를 클릭한 경우
                    comment.setHeart(true);
                    comment.setHeartCount(comment.getHeartCount() + 1);
                    holder.heart.setImageResource(R.drawable.fillheart);
                } else {
                    // 좋아요를 취소한 경우
                    comment.setHeart(false);
                    comment.setHeartCount(comment.getHeartCount() - 1);
                    holder.heart.setImageResource(R.drawable.heart);
                }

                holder.heartCounter.setText(String.valueOf(comment.getHeartCount()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addComment(Comment_Item comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1); // Notify only the inserted position
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView tv_comment;
        TextView tv_CommentTime;
        TextView name;
        ImageView heart;
        TextView heartCounter;

        public ViewHolder(View view) {
            super(view);
            profile = view.findViewById(R.id.profile);
            tv_comment = view.findViewById(R.id.tv_Comment);
            tv_CommentTime = view.findViewById(R.id.comment_time);
            name = view.findViewById(R.id.comment_name);
            heart = view.findViewById(R.id.heart);
            heartCounter = view.findViewById(R.id.heartCount);

        }
    }
}