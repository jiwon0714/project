package com.example.myapplication.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private EditText etMessage;
    private Button btnSubmit;
    private RecyclerView recyclerMessages;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    private ImageButton backtolist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        etMessage = findViewById(R.id.et_message);
        btnSubmit = findViewById(R.id.btn_submit);
        recyclerMessages = findViewById(R.id.recycler_messages);

        messageList = new ArrayList<>(); // 메시지 리스트 초기화

        // 어댑터 초기화 및 RecyclerView에 설정
        chatAdapter = new ChatAdapter(messageList);
        recyclerMessages.setAdapter(chatAdapter);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));




        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);


        backtolist = findViewById(R.id.back);

        backtolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, List_ChatListActivity.class);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = etMessage.getText().toString();

                if (!messageContent.isEmpty()) {
                    // 현재 시간을 간단한 형식으로 가져오기
                    String timestamp = getCurrentTimestamp();
                    ChatMessage newMessage = new ChatMessage(messageContent, timestamp, true, 1); // 어댑터에 메시지 추가
                    receiveMessage();
                    chatAdapter.addMessage(newMessage);

                    // 입력 필드 비우기
                    etMessage.setText("");
                }
            }
        });



        // 액티비티가 시작될 때 모든 메시지를 읽은 것으로 표시
        markAllMessagesAsRead();
    }

    private void receiveMessage() {
        // 메시지를 받았다고 가정
        String receivedMessageContent = "안녕하세요! 받은 메시지입니다.";
        String timestamp = getCurrentTimestamp();
        ChatMessage receivedMessage = new ChatMessage(receivedMessageContent, timestamp, false, 2); // false는 발신 여부를 나타냄

        // 어댑터에 메시지 추가
        chatAdapter.addMessage(receivedMessage);
    }

    private void markAllMessagesAsRead() {
        for (int i = 0; i < messageList.size(); i++) {
            markMessageAsRead(i);
        }
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }


    // ChatActivity에서 읽음 상태 업데이트 예시
// (실제로는 상대방이 메시지를 확인했을 때 이 메서드를 호출해야 합니다.)
    private void markMessageAsRead(int position) {
        if (position >= 0 && position < messageList.size()) {
            ChatMessage message = messageList.get(position);
            message.setRead(true);

            // 어댑터에 변경된 상태를 알리고 UI 갱신
            chatAdapter.notifyItemChanged(position);
        }
    }
}