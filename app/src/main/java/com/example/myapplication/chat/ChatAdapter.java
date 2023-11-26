package com.example.myapplication.chat;

import android.content.Context;
import android.content.SharedPreferences;
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
    // 뷰 타입 상수 정의
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private Context context;
    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 뷰 타입에 따라 다른 뷰홀더를 생성
        if (viewType == VIEW_TYPE_SENT) {
            View sentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_talk_item_mine, parent, false);
            return new ViewHolder(sentView);
        } else {
            View receivedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_talk_item_not_mine, parent, false);
            return new ViewHolder(receivedView);
        }
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
    }

    public void update() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 해당 아이템의 뷰 타입을 반환
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Integer my_identifier = prefs.getInt("uid", 99);
        if(messages.get(position).getWriter() == my_identifier) {
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
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

    public void set_context(Context context) {
        this.context = context;
    }
}
