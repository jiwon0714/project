package com.example.myapplication.sns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Navi;
import com.example.myapplication.R;
import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.set_retrofit;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnsListActivity extends AppCompatActivity {

    ImageButton home, heart, chat, add;
    private ImageButton btn_camera, btn_sns, btn_home, btn_paint, btn_chat, btn_diary;

    private RecyclerView mRecyclerView;
    private SnsRecyclerAdapter mRecyclerAdapter;
    private ArrayList<SnsItem> mSnsItems;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_activity_sns_list);

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
                Intent intent = new Intent(SnsListActivity.this, AddPhotoActivity.class);
                startActivity(intent);
            }
        });


        btn_home = findViewById(R.id.btn_home);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sns = findViewById(R.id.btn_sns);
        btn_paint = findViewById(R.id.btn_paint);
        btn_diary = findViewById(R.id.btn_diary);

        Navi navi = new Navi(this); // 'this'는 현재 액티비티의 Context를 나타냅니다.
        navi.setImageButtonListeners(btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /* initiate adapter */
        mRecyclerAdapter = new SnsRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        /* data request */
        api = set_retrofit.getClient().create(Api.class);
        mSnsItems = new ArrayList<>();
        Call<List<ImageDTO>> call = api.getImage();
        call.enqueue(new Callback<List<ImageDTO>>() {
            @Override
            public void onResponse(Call<List<ImageDTO>> call, Response<List<ImageDTO>> response) {
                if (response.isSuccessful()) {
                    List<ImageDTO> imageList = response.body();
                    for (ImageDTO post : imageList) {
                        Bitmap mainImg = base64ToBitmap(post.getImg());
                        Bitmap profileImg = base64ToBitmap(post.getProfileImg());
                        mSnsItems.add(new SnsItem(profileImg, post.getOwner(), mainImg, post.getTxt(),"댓글", "댓글"));
                    }
                    Collections.reverse(mSnsItems);
                } else {
                    System.out.println("Error");
                }
                /* adapt data */
                mRecyclerAdapter.setSnsList(mSnsItems);
            }

            @Override
            public void onFailure(Call<List<ImageDTO>> call, Throwable t) {
                // 네트워크 오류 또는 서버 응답 실패 시 처리
            }
        });




    }
    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}