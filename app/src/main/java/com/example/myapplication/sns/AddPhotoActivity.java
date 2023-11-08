package com.example.myapplication.sns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Navi;
import com.example.myapplication.R;

import java.io.IOException;

public class AddPhotoActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    ImageButton back, mainimage;


    ImageButton btn_camera, btn_sns, btn_home, btn_paint, btn_chat, btn_diary;

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
        btn_camera = findViewById(R.id.btn_camera);
        btn_paint = findViewById(R.id.btn_paint);
        btn_diary = findViewById(R.id.btn_diary);

        Navi navi = new Navi(this); // 'this'는 현재 액티비티의 Context를 나타냅니다.
        navi.setImageButtonListeners(btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary);


        mainimage = findViewById(R.id.mainimage);

        mainimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리에서 이미지를 선택하는 인텐트를 생성
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_PICTURE); // SELECT_PICTURE는 상수로 정의된 정수입니다.
            }
        });





    }

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


}