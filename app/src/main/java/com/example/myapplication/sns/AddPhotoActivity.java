package com.example.myapplication.sns;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Navi;
import com.example.myapplication.R;
import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.set_retrofit;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPhotoActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    ImageButton back, mainimage;
    private ImageDTO imageDTO = new ImageDTO();
    ImageButton btn_camera, btn_sns, btn_home, btn_paint, btn_chat, btn_diary;
    Button btn_upload;
    EditText editText;
    TextView userName;
    ImageView profileImg;

    String bitmapProfile;
    private Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sns_activity_add_photo);

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

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );

        btn_home = findViewById(R.id.btn_home);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sns = findViewById(R.id.btn_sns);
        // btn_camera = findViewById(R.id.btn_camera);
        btn_paint = findViewById(R.id.btn_paint);
        btn_diary = findViewById(R.id.btn_diary);
        userName = findViewById(R.id.tv_profile);
        btn_upload = findViewById(R.id.btn_upload);
        profileImg = findViewById(R.id.img_profile);

        Navi navi = new Navi(this); // 'this'는 현재 액티비티의 Context를 나타냅니다.
        navi.setImageButtonListeners(btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary);

        mainimage = findViewById(R.id.mainimage);
        editText = findViewById(R.id.et_content);

        if(set_retrofit.get_context()!=null){
            Context context = set_retrofit.get_context();
            SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userInfo = prefs.getString("userInfo", "");
            if (!TextUtils.isEmpty(userInfo)) {
                Gson gson = new Gson();
                UserDTO user = gson.fromJson(userInfo, UserDTO.class);
                userName.setText(user.getName());
                profileImg.setImageBitmap(base64ToBitmap(user.getProfileImg()));
                imageDTO.setProfileImg(user.getProfileImg());
                Log.i("getProfileImg", user.getProfileImg());
            }else{Log.i("userInfo", userInfo);}
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                    try {
                        // Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        if (bitmap != null) {
                            String encodedImage = bitmap_to_base64(bitmap);
                            imageDTO.setImg(encodedImage);
                            mainimage.setImageBitmap(bitmap);
                        } else {
                            Log.e("Bitmap Decode", "Bitmap is null");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Image Selection Error", "Error: " + e.getMessage());
                    }
                    }
                }
        );

        mainimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 선택창 생성
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageDTO.getImg() == null) {
                    Toast.makeText(getApplicationContext(), "이미지가 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                imageDTO.setTxt(editText.getText().toString());
                // 현재 날짜와 시간을 가져오기
                LocalDateTime currentDateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentDateTime = LocalDateTime.now();
                }

                // 날짜 및 시간 형식 지정
                DateTimeFormatter formatter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                }
                String formattedDateTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formattedDateTime = currentDateTime.format(formatter);
                }

                imageDTO.setDate(formattedDateTime);

                api = set_retrofit.getClient().create(Api.class);
                Call<ResponseBody> call = api.postImage(imageDTO);

                call.clone().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // 서버 응답을 받았을 때 데이터를 로그로 확인
                            Intent intent = new Intent(AddPhotoActivity.this, SnsListActivity.class);
                            Toast.makeText(AddPhotoActivity.this, "업로드가 완료되었습니다.", Toast.LENGTH_LONG).show();
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
            }
        });
    }

    private String bitmap_to_base64 (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}