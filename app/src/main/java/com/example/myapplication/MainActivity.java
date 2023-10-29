package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity  {

    private ImageButton btn_camera, btn_sns, btn_account, btn_paint, btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_account = findViewById(R.id.btn_account);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sns = findViewById(R.id.btn_sns);
        btn_camera = findViewById(R.id.btn_camera);
        btn_paint = findViewById(R.id.btn_paint);

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

        btn_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PaintActivity.class);
                startActivity(intent);
            }
        });

        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        btn_sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SNSActivity.class);
                startActivity(intent);
            }
        });

        btn_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PaintActivity.class);
                startActivity(intent);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });

//        btn_account.setOnClickListener(this);
//        btn_message.setOnClickListener(this);
//        btn_sns.setOnClickListener(this);
//        btn_camera.setOnClickListener(this);
//        btn_paint.setOnClickListener(this);
//        btn_home.setOnClickListener(this);


    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_account:
//                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
//                break;
//            case R.id.btn_paint:
//                Intent intent1 = new Intent(MainActivity.this, PaintActivity.class);
//                break;
//            case R.id.btn_account:
//                Intent intent2 = new Intent(MainActivity.this, AccountActivity.class);
//                break;


    //}
}
