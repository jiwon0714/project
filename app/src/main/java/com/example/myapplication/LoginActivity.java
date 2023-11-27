package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.AuthResponse;
import com.example.myapplication.dto.LoginRequest;
import com.example.myapplication.dto.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    private Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        //회원가입 버튼 누르면 실행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력된 값을 가져온다
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                // cmd-ipconfig ipv4 주소로 바꾸기
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://172.20.10.6:8080/demo/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                api = retrofit.create(Api.class);
                Call<AuthResponse> call = api.login(new LoginRequest(userID, userPass));
                call.clone().enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        // 응답 처리 (토큰 분리 및 저장)
                        if(response.isSuccessful()) {
                            AuthResponse authResponse = response.body();
                            if(authResponse.get_status().equals("201")) {
                                String jwtToken = response.body().get_token();
                                UserDTO me = response.body().get_user();
                                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                                editor.remove("token");
                                editor.remove("uid");
                                editor.apply();
                                editor.putString("token", jwtToken);
                                editor.putInt("uid", me.getIdentifier());
                                editor.apply();
                                set_retrofit.set_context(getApplicationContext());
                                Log.i("Login", "Success");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Log.i("Login2", authResponse.get_message());
                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }
                        }
                        else {
                            // error
                            Log.i("Login3", "response failed");
                            Toast.makeText( getApplicationContext(), "로그인 요청을 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        // error
                        Log.i("Login", t.getMessage());
                        Toast.makeText( getApplicationContext(), "서버에 연결을 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                        return;
                    }
                });
            }
        });

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
    }
}