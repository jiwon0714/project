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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.UserDTO;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountModifyActivity extends AppCompatActivity {
    EditText name,id,pw,repw,email,birthday;

    Button pwcheck, save;

    boolean checkPW = false;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_modify);

        save = findViewById(R.id.btn_save);
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

        pwcheck = findViewById(R.id.btn_passcheck);
        pwcheck.setOnClickListener(v -> {
            if(pw.getText().toString().equals(repw.getText().toString())){
                checkPW = true;
                pwcheck.setText("일치");
            }else{
                pwcheck.setText("재확인");
                Toast.makeText(AccountModifyActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        if (!TextUtils.isEmpty(userInfo)) {
            Gson gson = new Gson();
            UserDTO user = gson.fromJson(userInfo, UserDTO.class);
            name.setText(user.getName());
            id.setText(user.getId());
            birthday.setText(user.getDate());
            email.setText(user.getEmail());
            Log.i("userInfo", user.getName());
            Log.i("userInfo", user.getId());
            Log.i("userInfo", user.getDate());
            Log.i("userInfo", user.getEmail());

        }else{Log.i("userInfo", userInfo);}

        save.setOnClickListener(v -> {
//            if(name.length() == 0){
//                Toast.makeText(AccountModifyActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_LONG).show();
//            }else if(id.length() == 0){
//                Toast.makeText(AccountModifyActivity.this, "아이디를 입력해 주세요.", Toast.LENGTH_LONG).show();
//            }else if(pw.length() == 0){
//                Toast.makeText(AccountModifyActivity.this, "패스워드를 입력해 주세요.", Toast.LENGTH_LONG).show();
//            }else if(email.length() == 0){
//                Toast.makeText(AccountModifyActivity.this, "이메일을 입력해 주세요.", Toast.LENGTH_LONG).show();
//            }else if(checkPW == false){
//                Toast.makeText(AccountModifyActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
//            }else{
//                UserDTO userDTO = new UserDTO(id.getText().toString(), name.getText().toString(), email.getText().toString(), pw.getText().toString(),birthday.getText().toString());
//                // cmd-ipconfig ipv4 주소로 바꾸기
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://192.168.0.79:8080/demo/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                api = retrofit.create(Api.class);
//                Call<ResponseBody> call = api.addNewUser(userDTO);
//
//                call.clone().enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            // 서버 응답을 받았을 때 데이터를 로그로 확인
//                            Intent intent = new Intent(AccountModifyActivity.this, AccountActivity.class);
//                            Toast.makeText(AccountModifyActivity.this, "회원정보가 수정되었습니다.", Toast.LENGTH_LONG).show();
//                            startActivity(intent);
//                            try {
//                                Log.e("Response Data", response.body().string());
//                            } catch (IOException e) {
//                                Log.e("POST", "응답 데이터 읽기 실패");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e("POST", "실패");
//                    }
//                });
//            }

            Intent intent = new Intent(AccountModifyActivity.this, AccountActivity.class);
            startActivity(intent);
        });

    }


}