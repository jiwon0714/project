package com.example.myapplication.sns;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Navi;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.set_retrofit;

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
        back.setOnClickListener(v -> onBackPressed());

        btn_home = findViewById(R.id.btn_home);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sns = findViewById(R.id.btn_sns);
        btn_camera = findViewById(R.id.btn_camera);
        btn_paint = findViewById(R.id.btn_paint);
        btn_diary = findViewById(R.id.btn_diary);

        btn_upload = findViewById(R.id.btn_upload);

        Navi navi = new Navi(this); // 'this'는 현재 액티비티의 Context를 나타냅니다.
        navi.setImageButtonListeners(btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary);

        mainimage = findViewById(R.id.mainimage);
        editText = findViewById(R.id.et_content);
        userName = findViewById(R.id.tv_profile);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                            String encodedImage = bitmap_to_base64(bitmap);
                            // new_post.setImg(encodedImage);
                            imageDTO.setImg(encodedImage);
                            mainimage.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        mainimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 선택창 생성
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDTO.setTxt(editText.getText().toString());
                imageDTO.setName(userName.getText().toString());
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

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            // 갤러리에서 선택한 이미지의 URI를 얻습니다.
            Uri selectedImageUri = data.getData();

            // 이미지 버튼을 참조합니다.
            ImageButton imageButton = findViewById(R.id.mainimage);

            // 이미지 버튼의 크기를 이미지의 크기에 맞게 동적으로 조절합니다.
            int targetWidth = imageButton.getWidth(); // 이미지 버튼의 가로 크기
            int targetHeight = imageButton.getHeight(); // 이미지 버튼의 세로 크기

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                // 이미지 버튼에 크기를 조절한 이미지를 설정합니다.
                imageButton.setImageBitmap(scaledBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */
    }
    private String bitmap_to_base64 (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}