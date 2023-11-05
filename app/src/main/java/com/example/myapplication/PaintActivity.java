package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaintActivity extends AppCompatActivity {
    private MyPaintView myView;

    int count = 0;

    private static final int REQUEST_IMAGE_PICK = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        myView = new MyPaintView(this);

        // "갤러리로 이동" 버튼 처리
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
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

        ((LinearLayout) findViewById(R.id.paintLayout)).addView(myView);
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        if (checkedId == R.id.btnRed) {
                            myView.mPaint.setColor(Color.RED);
                        } else if (checkedId == R.id.btnGreen) {
                            myView.mPaint.setColor(Color.GREEN);
                        } else if (checkedId == R.id.btnBlue) {
                            myView.mPaint.setColor(Color.BLUE);
                        }
                    }

                });

//        Button btnTh = findViewById(R.id.btnTh);
//        btnTh.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(count%2==1){
//                    btnTh.setText("얇게");
//                    myView.mPaint.setStrokeWidth(10);
//                    count++;
//                } else {
//                    btnTh.setText("두껍게");
//                    myView.mPaint.setStrokeWidth(20);
//                    count++;
//                }
//            }
//        }));

        ((ImageButton)findViewById(R.id.btnClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myView.mBitmap.eraseColor(Color.TRANSPARENT);
                myView.invalidate();
            }
        });

        ((ImageButton)findViewById(R.id.painthome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaintActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnCapture = findViewById(R.id.btn_picsave);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureAndSaveImage();
            }
        });

        ImageButton btnThickness = findViewById(R.id.imageView_paintBrush_drawing);
        btnThickness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showThicknessDialog();
            }
        });

    }
    private void showThicknessDialog() {
        // 다이얼로그 레이아웃을 inflate
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

        // SeekBar를 찾아옴
        SeekBar seekBar = dialogView.findViewById(R.id.seekBar);

        // 초기 굵기 설정
        int initialThickness = (int) myView.mPaint.getStrokeWidth();
        seekBar.setProgress(initialThickness);

        // 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("굵기 조절");

        // 굵기 조절 SeekBar의 값이 변경되었을 때 리스너 설정
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // SeekBar 값에 따라 그림판의 선 굵기 변경
                myView.mPaint.setStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 조절을 시작할 때 실행
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 조절을 멈출 때 실행
            }
        });

        // 다이얼로그 표시
        builder.show();
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();

                try {
                    Bitmap backgroundImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    myView.setBackgroundImage(backgroundImage);
                    Toast.makeText(this, "배경 이미지로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private void captureAndSaveImage() {
        myView.setDrawingCacheEnabled(true);
        myView.buildDrawingCache();
        Bitmap captureView = myView.getDrawingCache();

        if (captureView != null) {
            try {
                // 외부 저장소에 디렉토리 생성 (저장 경로를 변경하려면 수정하세요)
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyPaintImages");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // 이미지 파일 이름을 타임스탬프로 지정
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "IMG_" + timeStamp + ".png";

                // 이미지 파일을 저장
                File imageFile = new File(directory, imageFileName);
                FileOutputStream fos = new FileOutputStream(imageFile);
                captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                // 미디어 스캔을 통해 갤러리에 이미지 추가
                MediaScannerConnection.scanFile(this, new String[]{imageFile.getPath()}, null, null);

                Toast.makeText(getApplicationContext(), "그림이 갤러리에 저장되었습니다!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to save image", Toast.LENGTH_LONG).show();
            } finally {
                myView.destroyDrawingCache();
            }
        } else {
            // 캡처된 비트맵이 null일 경우에 대한 처리
            Toast.makeText(getApplicationContext(), "Failed to capture image", Toast.LENGTH_LONG).show();
        }
    }


    private static class MyPaintView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;
        public MyPaintView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);

            // 비트맵 초기화 및 배경 설정
            mBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // 투명 배경
        }

        public void setBackgroundImage(Bitmap background) {
            mCanvas.drawBitmap(background, 0, 0, null);
            invalidate();
        }
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawBitmap(mBitmap, 0, 0, null); //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int)event.getX();
            int y = (int)event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint); //mBitmap 에 기록
                    mPath.reset();
                    break;
            }
            this.invalidate();
            return true;
        }



    }
}