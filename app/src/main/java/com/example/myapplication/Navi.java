package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.Diary.DiaryListActivity;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.sns.SNSActivity;

public class Navi {


    private Context context;

    public Navi(Context context) {
        this.context = context;
    }

    public void setImageButtonListeners(ImageButton btn_home, ImageButton btn_chat,ImageButton btn_sns,ImageButton btn_camera,ImageButton btn_paint,ImageButton btn_diary) {

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatListActivity.class);
                context.startActivity(intent);
            }
        });

        btn_sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SNSActivity.class);
                context.startActivity(intent);
            }
        });

        btn_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaintActivity.class);
                context.startActivity(intent);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CameraActivity.class);
                context.startActivity(intent);
            }
        });

        btn_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DiaryListActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
