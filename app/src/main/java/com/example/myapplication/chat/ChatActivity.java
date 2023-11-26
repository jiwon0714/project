package com.example.myapplication.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.controller.Api;
import com.example.myapplication.set_retrofit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private EditText etMessage;
    private Button btnSubmit;
    private RecyclerView recyclerMessages;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    private ImageButton backtolist;
    private TextView chatTitle;
    private Api api;
    private int now_idx;
    private Timer update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        etMessage = findViewById(R.id.et_message);
        btnSubmit = findViewById(R.id.btn_submit);
        chatTitle = findViewById(R.id.tv_title);
        recyclerMessages = findViewById(R.id.recycler_messages);

        now_idx = 0;

        messageList = new ArrayList<>(); // 메시지 리스트 초기화

        // 어댑터 초기화 및 RecyclerView에 설정
        chatAdapter = new ChatAdapter(messageList);
        chatAdapter.set_context(getApplicationContext());
        recyclerMessages.setAdapter(chatAdapter);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));

        api = set_retrofit.getClient().create(Api.class);

        chatTitle.setText(getIntent().getStringExtra("chat_name"));

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

        Call<List<ChatMessage>> init_chat = api.getChatInit(getIntent().getStringExtra("room_id"));

        init_chat.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if(response.isSuccessful()) {
                    List<ChatMessage> chatList = response.body();
                    for(ChatMessage chat : chatList) {
                        chatAdapter.addMessage(chat);
                        now_idx = chat.getIdx();
                    }
                    chatAdapter.update();
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Intent back_intent = new Intent(ChatActivity.this, List_ChatListActivity.class);
                Toast.makeText(ChatActivity.this, "채팅을 불러올 수 없습니다", Toast.LENGTH_LONG).show();
                startActivity(back_intent);
            }
        });

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
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    Integer my_identifier = prefs.getInt("uid", 99);
                    ChatMessage newMessage = new ChatMessage(messageContent, timestamp, my_identifier, getIntent().getIntExtra("opp_name", 99));

                    Call<Integer> chat = api.postChat(getIntent().getStringExtra("room_id"), newMessage);
                    chat.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.isSuccessful()) {
                                chatUpdate();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Intent back_intent = new Intent(ChatActivity.this, List_ChatListActivity.class);
                            Toast.makeText(ChatActivity.this, "채팅을 불러올 수 없습니다", Toast.LENGTH_LONG).show();
                            startActivity(back_intent);
                        }
                    });

                    // 입력 필드 비우기
                    etMessage.setText("");
                }
            }
        });

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                chatUpdate();
            }
        };

        //update = new Timer();
        //update.schedule(timerTask, 0, 4000);
    }


    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void chatUpdate() {
        Call<List<ChatMessage>> chat = api.getChat(getIntent().getStringExtra("room_id"), now_idx);

        chat.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if(response.isSuccessful()) {
                    List<ChatMessage> chatList = response.body();
                    for(ChatMessage chat : chatList) {
                        chatAdapter.addMessage(chat);
                        now_idx = chat.getIdx();
                    }
                }
                chatAdapter.update();
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Intent back_intent = new Intent(ChatActivity.this, List_ChatListActivity.class);
                Toast.makeText(ChatActivity.this, "채팅을 불러올 수 없습니다", Toast.LENGTH_LONG).show();
                startActivity(back_intent);
            }
        });

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