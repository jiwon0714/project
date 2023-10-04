package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SNSActivity extends AppCompatActivity {

    private ImageButton home, heart, chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snsactivity);


        home = findViewById(R.id.home);
        heart = findViewById(R.id.heart);
        chat = findViewById(R.id.chat);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SNSActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}