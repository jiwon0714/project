package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.dto.UserDTO;
import com.google.gson.Gson;

public class AccountActivity extends AppCompatActivity {

    TextView check;
    Button modify;
    TextView name, id, birthday, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        check = findViewById(R.id.btn_register);
        modify=findViewById(R.id.btn_modify);
        name = findViewById(R.id.et_name);
        id = findViewById(R.id.et_id);
        birthday = findViewById(R.id.et_birth);
        email = findViewById(R.id.et_mail);

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

        Context context = set_retrofit.get_context();
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userInfo = prefs.getString("userInfo", "");

        if (!TextUtils.isEmpty(userInfo)) {
            Gson gson = new Gson();
            UserDTO user = gson.fromJson(userInfo, UserDTO.class);
            name.setText(user.getName());
            id.setText(user.getId());
            birthday.setText(user.getDate());
            email.setText(user.getEmail());
        }else{Log.i("userInfo", userInfo);}

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountModifyActivity.class);
                startActivity(intent);
            }
        });
    }


}