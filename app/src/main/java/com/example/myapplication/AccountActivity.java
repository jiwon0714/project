package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.UserDTO;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    Button modify,btn_profile_modify, save;
    TextView name, id, birthday, email;
    CircleImageView profile;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Boolean changeProfile = false;
    private UserDTO userDTO;
    private Api api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        modify=findViewById(R.id.btn_modify);
        name = findViewById(R.id.et_name);
        id = findViewById(R.id.et_id);
        birthday = findViewById(R.id.et_birth);
        email = findViewById(R.id.et_mail);
        save = findViewById(R.id.btn_save);
        btn_profile_modify = findViewById(R.id.btn_profile_modify);
        profile = findViewById(R.id.profile);

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

        if(set_retrofit.get_context()!=null){
            Context context = set_retrofit.get_context();
            SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userInfo = prefs.getString("userInfo", "");
            if (!TextUtils.isEmpty(userInfo)) {
                Gson gson = new Gson();
                userDTO = gson.fromJson(userInfo, UserDTO.class);
                profile.setImageBitmap(base64ToBitmap(userDTO.getProfileImg()));
                name.setText(userDTO.getName());
                id.setText(userDTO.getId());
                birthday.setText(userDTO.getDate());
                email.setText(userDTO.getEmail());
            }else{Log.i("userInfo", userInfo);}
        }

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountModifyActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changeProfile){
                    api = set_retrofit.getClient().create(Api.class);
                    Call<ResponseBody> call = api.modifyUser(userDTO);
                    call.clone().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                // 서버 응답을 받았을 때 데이터를 로그로 확인
                                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                                Toast.makeText(AccountActivity.this, "업로드가 완료되었습니다.", Toast.LENGTH_LONG).show();
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
                            Log.e("POST_FAIL", t.getMessage());
                        }
                    });
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String userInfo = gson.toJson(userDTO);
                    Log.i("modifyuserInfo", userInfo);
                    editor.putString("userInfo", userInfo);
                    editor.apply();
                }
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_profile_modify.setOnClickListener(new View.OnClickListener() {
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
                userDTO.setProfileImg(bitmap_to_base64(selectedImageBitmap));
                changeProfile = true;

            } catch (FileNotFoundException e) {
                Log.e("AccountModifyActivity", "선택한 이미지 로드 중 오류 발생", e);
            }
        }
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private String bitmap_to_base64 (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}