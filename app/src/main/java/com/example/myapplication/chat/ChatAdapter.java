package com.example.myapplication.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

// ChatAdapter.java
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_talk_item_mine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        holder.txtMessage.setText(message.getMessageContent());
        holder.txtDate.setText(message.getTimestamp());

        // 읽음 상태에 따라 UI 업데이트
        if (message.isRead()) {
            holder.txtIsShown.setVisibility(View.GONE);
        } else {
            holder.txtIsShown.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtDate;
        TextView txtIsShown;


        public ViewHolder(View view) {
            super(view);
            txtMessage = view.findViewById(R.id.txt_message);
            txtDate = view.findViewById(R.id.txt_date);
            txtIsShown = view.findViewById(R.id.txt_isShown); // 초기화 추가

        }
    }
}
