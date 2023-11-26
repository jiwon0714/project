package com.example.myapplication.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

// ChatAdapter.java
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> messages;

    // 뷰 타입 상수 정의
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        // 해당 아이템의 뷰 타입을 반환
        return messages.get(position).isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 뷰 타입에 따라 다른 뷰홀더를 생성
        if (viewType == VIEW_TYPE_SENT) {
            View sentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_talk_item_mine, parent, false);
            return new SentMessageViewHolder(sentView);
        } else {
            View receivedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_talk_item_mine, parent, false);
            return new ReceivedMessageViewHolder(receivedView);
        }
        //return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

//        holder.txtMessage.setText(message.getMessageContent());
//        holder.txtDate.setText(message.getTimestamp());

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }

        // 읽음 상태에 따라 UI 업데이트
        if (message.isRead()) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
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

    // 발신 메시지 뷰홀더
    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtDate;
        TextView txtIsShown;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtIsShown = itemView.findViewById(R.id.txt_isShown); // 초기화 추가

        }

        void bind(ChatMessage message) {
            txtMessage.setText(message.getMessageContent());
            txtDate.setText(message.getTimestamp());

        }
    }

    // 수신 메시지 뷰홀더
    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtDate;
        TextView txtIsShown;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtIsShown = itemView.findViewById(R.id.txt_isShown); // 초기화 추가

        }

        void bind(ChatMessage message) {
            txtMessage.setText(message.getMessageContent());
            txtDate.setText(message.getTimestamp());

        }
    }

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView txtMessage;
//        TextView txtDate;
//        TextView txtIsShown;
//
//
//        public ViewHolder(View view) {
//            super(view);
//            txtMessage = view.findViewById(R.id.txt_message);
//            txtDate = view.findViewById(R.id.txt_date);
//            txtIsShown = view.findViewById(R.id.txt_isShown); // 초기화 추가
//
//        }
//    }
}
