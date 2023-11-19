package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.Diary_Calender.DiaryActivity;
import com.example.myapplication.chat.List_ChatListActivity;
import com.example.myapplication.sns.SnsListActivity;

public class Navi {


    private Context context;

    public Navi(Context context) {
        this.context = context;
    }

    public void setImageButtonListeners(ImageButton btn_home, ImageButton btn_chat, ImageButton btn_sns, ImageButton btn_camera, ImageButton btn_paint, ImageButton btn_diary) {

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(List_ChatListActivity.class);
            }
        });

        btn_sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(SnsListActivity.class);
            }
        });

        btn_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(PaintActivity.class);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(CameraActivity.class);
            }
        });

        btn_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(DiaryActivity.class);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(MainActivity.class);
            }
        });
    }

    private void startNewActivity(Class<?> cls) {
        if (context.getClass() != cls) {
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);
        }
    }
}
