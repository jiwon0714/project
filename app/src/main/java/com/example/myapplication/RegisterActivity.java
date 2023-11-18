package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.dto.UserDTO;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView back;
    EditText name,id,pw,repw,email,et_birthday;
    Button pwcheck, submit;

    Boolean checkPW = false;
    String getName, getId, getPw, getEmail, getBrithday;

    private Api api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );

        name = findViewById(R.id.et_name);
        id=findViewById(R.id.et_id);
        pw=findViewById(R.id.et_pass);
        repw=findViewById(R.id.et_repass);
        email=findViewById(R.id.et_mail);



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

        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.btn_passcheck);
        pwcheck.setOnClickListener(v -> {
            if(pw.getText().toString().equals(repw.getText().toString())){
                checkPW = true;
                pwcheck.setText("일치");
            }else{
                pwcheck.setText("재확인");
                Toast.makeText(RegisterActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        //회원가입 완료 버튼
        submit = findViewById(R.id.btn_register);
        submit.setOnClickListener(v -> {
            if(name.length() == 0){
                Toast.makeText(RegisterActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_LONG).show();
            }else if(id.length() == 0){
                Toast.makeText(RegisterActivity.this, "아이디를 입력해 주세요.", Toast.LENGTH_LONG).show();
            }else if(pw.length() == 0){
                Toast.makeText(RegisterActivity.this, "패스워드를 입력해 주세요.", Toast.LENGTH_LONG).show();
            }else if(email.length() == 0){
                Toast.makeText(RegisterActivity.this, "이메일을 입력해 주세요.", Toast.LENGTH_LONG).show();
            }else if(checkPW == false){
                Toast.makeText(RegisterActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
            }else{
                UserDTO userDTO = new UserDTO(id.getText().toString(), name.getText().toString(), pw.getText().toString(), email.getText().toString());
                api = set_retrofit.getClient().create(Api.class);
                Call<ResponseBody> call = api.addNewUser(userDTO);

                call.clone().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // 서버 응답을 받았을 때 데이터를 로그로 확인
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            try {
                                Log.e("Response Data", response.body().string());
                            } catch (IOException e) {
                                Log.e("POST", "응답 데이터 읽기 실패");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("POST", "실패");
                    }
                });
            }
        });

        ImageButton birth = findViewById(R.id.btn_calendar);
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }
}