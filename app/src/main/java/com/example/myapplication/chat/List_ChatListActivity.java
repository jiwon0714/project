package com.example.myapplication.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Navi;
import com.example.myapplication.R;
import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.ChatCount;
import com.example.myapplication.set_retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_ChatListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List_ChatRecyclerAdapter mRecyclerAdapter;
    private ArrayList<List_FriendItem> mfriendItems;

    private ImageButton btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary,add;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_list);

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


        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_ChatListActivity.this, ChatAddActivity.class);
                startActivity(intent);
            }
        });


        btn_home = findViewById(R.id.btn_home);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sns = findViewById(R.id.btn_sns);
        // btn_camera = findViewById(R.id.btn_camera);
        btn_paint = findViewById(R.id.btn_paint);
        btn_diary = findViewById(R.id.btn_diary);

        Navi navi = new Navi(this); // 'this'는 현재 액티비티의 Context를 나타냅니다.
        navi.setImageButtonListeners(btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /* initiate adapter */
        mRecyclerAdapter = new List_ChatRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mRecyclerAdapter.setOnItemClickListener(new List_ChatRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String roomId = mfriendItems.get(pos).getRoomId();
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("room_id", roomId);
                intent.putExtra("opp_name", mfriendItems.get(pos).getName());
                v.getContext().startActivity(intent);
            }
        });

        api = set_retrofit.getClient().create(Api.class);
        mfriendItems = new ArrayList<>();
        Call<ChatCount> call = api.getChatCount();
        call.enqueue(new Callback<ChatCount>() {
            @Override
            public void onResponse(Call<ChatCount> call, Response<ChatCount> response) {
                if (response.isSuccessful()) {
                    ChatCount chatCount = response.body();
                    if (chatCount.get_room_cnt() != 0) {
                        String[] opp_user_list = chatCount.get_user_list();
                        Integer[] opp_id_list = chatCount.get_room_id();
                        for (int i = 0; i < chatCount.get_room_cnt(); i++) {
                            mfriendItems.add(new List_FriendItem(R.drawable.pinokio_circle, opp_user_list[i], i+1 + "번째 상태메시지", chatCount.getIdentifier(), opp_id_list[i]));
                        }
                    }
                } else {}
                /* adapt data */
                mRecyclerAdapter.setFriendList(mfriendItems);
            }

            @Override
            public void onFailure(Call<ChatCount> call, Throwable t) {
                // 네트워크 오류 또는 서버 응답 실패 시 처리
            }
        });
    }
}