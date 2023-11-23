package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.UserDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    TextView back;
    EditText id,pw,repw,email,birthday;
    EditText name;
    Button pwcheck, submit, btn_profile_set;
    Boolean checkPW = false;

    CircleImageView profile;

    private Api api;

    private static final int REQUEST_IMAGE_PICK = 1;


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
        birthday=findViewById(R.id.et_birth);

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
                UserDTO userDTO = new UserDTO(id.getText().toString(), name.getText().toString(), email.getText().toString(), pw.getText().toString(),birthday.getText().toString());
                // cmd-ipconfig ipv4 주소로 바꾸기
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.219.105:8080/demo/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                api = retrofit.create(Api.class);
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

        btn_profile_set = findViewById(R.id.btn_profile_set);
        profile = findViewById(R.id.profile);

        btn_profile_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "사진을 선택해 주세요.", Toast.LENGTH_LONG).show();

                openGallery();
            }
        });




    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImageBitmap = BitmapFactory.decodeStream(imageStream);


                profile.setImageBitmap(selectedImageBitmap);

            } catch (FileNotFoundException e) {
                Log.e("AccountModifyActivity", "선택한 이미지 로드 중 오류 발생", e);
            }
        }
    }
}