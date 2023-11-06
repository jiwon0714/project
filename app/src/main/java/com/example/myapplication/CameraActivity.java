package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    // 레이아웃의 버튼과 이미지뷰를 연결할 변수 선언
    private ImageButton btn_picture , btn_save;
    private ImageView imageView;

    private ImageButton btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary;

    // 그림을 받아올 비트맵 변수를 선언
    private Bitmap bitmap =null;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView);
        btn_picture = findViewById(R.id.btn_picture);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setVisibility(View.INVISIBLE);

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

        btn_home = findViewById(R.id.btn_home);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sns = findViewById(R.id.btn_sns);
        btn_camera = findViewById(R.id.btn_camera);
        btn_paint = findViewById(R.id.btn_paint);
        btn_diary = findViewById(R.id.btn_diary);

        Navi navi = new Navi(this); // 'this'는 현재 액티비티의 Context를 나타냅니다.
        navi.setImageButtonListeners(btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary);



        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera_app.launch(new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                ));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename="photo.JPG";
                saveFile(filename);
            }
        });
    }



    ActivityResultLauncher<Intent> camera_app =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if( result.getResultCode() == RESULT_OK
                                    && result.getData() != null){
                                Bundle extras = result.getData().getExtras();
                                bitmap = (Bitmap) extras.get("data");
                                imageView.setImageBitmap(bitmap);
                                btn_save.setVisibility(View.VISIBLE);
                            }
                        }
                    }
            );

    public byte[] bitmapToByteArray( Bitmap $bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        $bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        return stream.toByteArray()  ;
    }

    private void ToastMsg(String msg)
    {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void saveFile(String filename)
    {
        if(bitmap == null)
        {
            ToastMsg("먼저 촬영을 하세요");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(
                MediaStore.Images.Media.DISPLAY_NAME,
                filename);
        values.put(
                MediaStore.Images.Media.MIME_TYPE,
                "image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(
                    MediaStore.Images.Media.IS_PENDING,
                    1);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri item = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        try {
            ParcelFileDescriptor pdf =
                    contentResolver.openFileDescriptor(
                            item,
                            "w",
                            null);

            if (pdf == null) {
                ToastMsg("파일 디스크립션 생성에 실패하였습니다.");
                return;
            }
            byte[] strToByte = bitmapToByteArray(bitmap);
            FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
            fos.write(strToByte);
            fos.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear();
                values.put(
                        MediaStore.Images.Media.IS_PENDING,
                        0);
                contentResolver.update(item, values, null, null);
            }
            ToastMsg("갤러리에 파일을 저장하였습니다.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastMsg(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            ToastMsg(e.getMessage());
        }

    }

}