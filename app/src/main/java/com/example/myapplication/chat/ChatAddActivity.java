package com.example.myapplication.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.AuthResponse;
import com.example.myapplication.dto.LoginRequest;
import com.example.myapplication.set_retrofit;
import com.example.myapplication.sns.AddPhotoActivity;
import com.example.myapplication.sns.SnsListActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAddActivity extends AppCompatActivity {
    private EditText opp_id;
    private Button btn_add;
    private TextView back;
    private Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activiy_add_chat);

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

        opp_id = findViewById(R.id.et_id);
        btn_add = findViewById(R.id.btn_register);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력된 값을 가져온다
                String userID = opp_id.getText().toString();
                if(userID == null) {
                    Toast.makeText( getApplicationContext(), "추가할 상대를 입력해주세요.", Toast.LENGTH_SHORT ).show();
                    return;
                }

                api = set_retrofit.getClient().create(Api.class);
                Call<Integer> call = api.add_chat(userID);
                call.clone().enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        // 응답 처리 (토큰 분리 및 저장)
                        if(response.isSuccessful()) {
                            Integer result = response.body();
                            if(result == 2) {
                                Toast.makeText( getApplicationContext(), "이미 채팅이 존재하는 유저입니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }
                            else if(result ==1) {
                                Intent intent = new Intent(ChatAddActivity.this, List_ChatListActivity.class);
                                Toast.makeText(ChatAddActivity.this, "추가가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText( getApplicationContext(), "존재하지 않는 유저입니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }
                        }
                        else {
                            // error
                            Toast.makeText( getApplicationContext(), "추가 요청을 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        // error
                        Toast.makeText( getApplicationContext(), "서버에 연결을 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                        return;
                    }
                });
            }
        });
    }
}
