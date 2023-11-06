package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.post;
import com.example.myapplication.dto.post_upload;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPhotoActivity extends AppCompatActivity {

    ImageButton back,home;
    ImageView select;
    Button btn_up;
    EditText text;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Api api;
    private post_upload new_post = new post_upload();

    public AddPhotoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

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

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPhotoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        select = findViewById(R.id.mainimage);
        btn_up = findViewById(R.id.btn_upload);
        text = findViewById(R.id.et_content);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                            String encodedImage = bitmap_to_base64(bitmap);
                            new_post.setImg(encodedImage);
                            Log.d("Base64 Image", encodedImage);
                            select.setImageBitmap(bitmap);
                        }
                        catch (Exception e) {
                        e.printStackTrace();
                        }
                    }
                }
        );
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 선택창 생성
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            }
        });

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_post.setTxt(text.getText().toString());
                api = set_retrofit.getClient().create(Api.class);
                Call<post_upload> call = api.postData(new_post);

                call.enqueue(new Callback<post_upload>() {
                    @Override
                    public void onResponse(Call<post_upload> call, Response<post_upload> response) {
                        if (response.isSuccessful()) {
                            String code = response.code() + "";
                            Log.w("Code_1", code);
                        } else {
                            // Handle error response
                            String code = response.code() + "";
                            Log.w("Code_2", code);
                        }
                    }

                    @Override
                    public void onFailure(Call<post_upload> call, Throwable t) {
                        // Handle fail response
                        Log.w("Code_3", t.getMessage());
                    }
                });
            }
        });

    }

    private String bitmap_to_base64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}