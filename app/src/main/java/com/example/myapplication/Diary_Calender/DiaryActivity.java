package com.example.myapplication.Diary_Calender;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Navi;
import com.example.myapplication.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {


    CalendarView calendar;
    ImageView image;

    TextView selected_date;

    TextView diary_text;

    private static final int REQUEST_IMAGE_PICK = 1;
    AlertDialog alertDialog;


    private ImageButton btn_home, btn_chat, btn_sns, btn_camera, btn_paint, btn_diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);




        calendar = findViewById(R.id.calendar);
        selected_date = findViewById(R.id.tv_date);
        image = findViewById(R.id.diary_image);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.");
        Date date = new Date();
        selected_date.setText(formatter.format(date));

        calendar.setVisibility(View.GONE);
        image.setVisibility(View.GONE);



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





        selected_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date를 눌렀을 때 CalendarView를 토글하여 보이거나 감추도록 변경
                int visibility = calendar.getVisibility();
                if (visibility == View.VISIBLE) {
                    calendar.setVisibility(View.GONE);
                } else {
                    calendar.setVisibility(View.VISIBLE);
                }

                // CalendarView의 날짜 선택 이벤트 처리
                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        // 선택한 날짜를 TextView에 표시
                        String selectedDate = year + "." + (month + 1) + "." + dayOfMonth + ".";
                        selected_date.setText(selectedDate);

                        // 해당 날짜에 대한 일기 내용을 불러와서 diary_text에 설정
                        String diaryText = getDiaryTextFromFile(selectedDate);
                        diary_text.setText(diaryText);
                    }
                });
            }
        });

        diary_text = findViewById(R.id.diary_text);

        diary_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog.Builder를 사용하여 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryActivity.this, R.style.FullScreenDialog);

                // 다이얼로그 레이아웃을 inflate하여 설정
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_diary_text, null);


                builder.setView(dialogView);



                // 다이얼로그 내의 View를 찾아와 설정
                final TextView dialogTextView = dialogView.findViewById(R.id.dialogTextView);

                // diary_text의 내용을 dialogTextView에 설정
                String diaryText = diary_text.getText().toString();
                dialogTextView.setText(diaryText);


                // 다이얼로그 안에 날짜 설정
                final TextView dialogDate = dialogView.findViewById(R.id.dialogPutdate);

                String dialogSelectedDate = selected_date.getText().toString();
                dialogDate.setText(dialogSelectedDate);

                // 확인 버튼 추가
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼을 눌렀을 때의 동작 추가
                        String dialogText = dialogTextView.getText().toString();
                        diary_text.setText(dialogText);
                        // 해당 날짜와 텍스트를 파일에 저장
                        saveDiaryTextToFile(selected_date.getText().toString(), dialogText);
                        dialog.dismiss(); // 다이얼로그 닫기
                    }
                });

                Button ok_btn = dialogView.findViewById(R.id.ok_btn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "OK 버튼을 눌렀습니다.", Toast.LENGTH_LONG).show();

                        // 확인 버튼을 눌렀을 때의 동작 추가
                        String dialogText = dialogTextView.getText().toString();
                        diary_text.setText(dialogText);
                        // 해당 날짜와 텍스트를 파일에 저장
                        saveDiaryTextToFile(selected_date.getText().toString(), dialogText);

                        alertDialog.dismiss();
                    }
                });

                // Negative 버튼 설정
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 버튼 동작
                    }
                });


                ImageView dialogImageView = dialogView.findViewById(R.id.dialogImageView);
                // 이미지뷰를 미리 설정된 이미지로 초기화
                Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.pinokio_circle);
                dialogImageView.setImageBitmap(defaultImage);
                dialogImageView.setVisibility(View.GONE);


                // Neutral 버튼 설정
                builder.setNeutralButton("사진", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 갤러리로 이동하는 코드
                        openGallery();

                    }
                });

                // 다이얼로그 객체 생성
                alertDialog = builder.create();

                // 다이얼로그 창 속성 설정 (아래 바 숨기기)
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                // 다이얼로그 창 속성 설정 (아래 바 숨기기)
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    // 숨기기 전체 화면으로
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
                }


                // 확인 버튼에 스타일 적용
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // 확인 버튼 스타일 설정
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setTextColor(getResources().getColor(R.color.white));
                        positiveButton.setBackgroundColor(getResources().getColor(R.color.green));
                        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                        positiveButton.setTypeface(null, Typeface.BOLD);

                        // 취소 버튼 스타일 설정
                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(getResources().getColor(R.color.green)); // 원하는 색상으로 변경
                        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                        negativeButton.setTypeface(null, Typeface.BOLD);
                    }
                });

                // 다이얼로그 표시
                alertDialog.show();
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
            // 갤러리에서 이미지를 선택한 경우
            Uri selectedImageUri = data.getData();
            try {
                // 선택한 이미지를 비트맵으로 변환하여 이미지뷰에 설정
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImageBitmap = BitmapFactory.decodeStream(imageStream);

                // 이미지뷰에 선택한 이미지 설정
                if (alertDialog != null) {
                    ImageView dialogImageView = alertDialog.findViewById(R.id.dialogImageView);
                    if (dialogImageView != null) {
                        dialogImageView.setImageBitmap(selectedImageBitmap);
                        dialogImageView.setVisibility(View.VISIBLE);

                        diary_text.setText("24");

                    }
                }
            } catch (FileNotFoundException e) {
                Log.e("DiaryActivity", "선택한 이미지 로드 중 오류 발생", e);
            }
        }
    }


    private void saveDiaryTextToFile(String date, String text) {
        DiaryFileManager.saveDiaryText(this, date, text);
    }

    private String getDiaryTextFromFile(String date) {
        return DiaryFileManager.getDiaryText(this, date);
    }
}