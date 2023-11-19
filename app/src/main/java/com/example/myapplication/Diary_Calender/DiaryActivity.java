package com.example.myapplication.Diary_Calender;


import static com.example.myapplication.Diary_Calender.DiaryFileManager.FILE_NAME_PREFIX;
import static com.example.myapplication.Diary_Calender.DiaryFileManager.getDiaryImageFromFile;
import static com.example.myapplication.Diary_Calender.DiaryFileManager.saveDiaryImageToFile;

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

    TextView selected_date,btn_delete;

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


        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDiaryContent();
            }
        });


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

                        // 해당 날짜에 대한 이미지를 불러와서 diary_image에 설정
                        Bitmap diaryImage = DiaryFileManager.getDiaryImageFromFile(getApplicationContext(), selected_date.getText().toString());
                        if (diaryImage != null) {
                            ImageView diaryImageView = findViewById(R.id.diary_image);
                            diaryImageView.setImageBitmap(diaryImage);
                            diaryImageView.setVisibility(View.VISIBLE);
                        } else {
                            // 해당 날짜에 이미지가 없으면 diary_image를 숨김
                            ImageView diaryImageView = findViewById(R.id.diary_image);
                            diaryImageView.setVisibility(View.GONE);
                        }
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



                Button ok_btn = dialogView.findViewById(R.id.ok_btn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();

                        // 확인 버튼을 눌렀을 때의 동작 추가
                        String dialogText = dialogTextView.getText().toString();
                        diary_text.setText(dialogText);
                        // 해당 날짜와 텍스트를 파일에 저장
                        saveDiaryTextToFile(selected_date.getText().toString(), dialogText);

                        alertDialog.dismiss();
                    }
                });

                Button btn_pic = dialogView.findViewById(R.id.btn_pic);

                btn_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "사진을 선택해 주세요.", Toast.LENGTH_LONG).show();

                        openGallery();
                    }
                });

                ImageButton dismiss = dialogView.findViewById(R.id.dismiss);

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });



                ImageView dialogImageView = dialogView.findViewById(R.id.dialogImageView);
                // 이미지뷰를 미리 설정된 이미지로 초기화
                Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.pinokio_circle);
                dialogImageView.setImageBitmap(defaultImage);
                dialogImageView.setVisibility(View.GONE);


                Bitmap diaryImage = DiaryFileManager.getDiaryImageFromFile(getApplicationContext(), selected_date.getText().toString());
                if (diaryImage != null) {

                    dialogImageView.setImageBitmap(diaryImage);
                    dialogImageView.setVisibility(View.VISIBLE);
                }

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
            Uri selectedImageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImageBitmap = BitmapFactory.decodeStream(imageStream);

                // 이미지뷰에 선택한 이미지 설정
                if (alertDialog != null) {
                    ImageView dialogImageView = alertDialog.findViewById(R.id.dialogImageView);
                    if (dialogImageView != null) {
                        dialogImageView.setImageBitmap(selectedImageBitmap);
                        dialogImageView.setVisibility(View.VISIBLE);

                        // diary_image에도 설정
                        ImageView diaryImage = findViewById(R.id.diary_image);
                        diaryImage.setImageBitmap(selectedImageBitmap);
                        diaryImage.setVisibility(View.VISIBLE);

                        // 해당 날짜와 이미지를 파일에 저장
                        DiaryFileManager.saveDiaryImageToFile(getApplicationContext(), selected_date.getText().toString(), selectedImageBitmap);
                    }
                }
            } catch (FileNotFoundException e) {
                Log.e("DiaryActivity", "선택한 이미지 로드 중 오류 발생", e);
            }
        }
    }


    private void deleteDiaryContent() {
        String selectedDate = selected_date.getText().toString();

        // 삭제할 텍스트 파일 이름
        String textFileName = FILE_NAME_PREFIX + selectedDate.replace(".", "_") + ".txt";
        // 삭제할 이미지 파일 이름
        String imageFileName = FILE_NAME_PREFIX + selectedDate.replace(".", "_") + "_image.png";

        // 파일 삭제
        deleteFile(textFileName);
        deleteFile(imageFileName);

        // UI 업데이트: 내용 및 이미지 숨김
        diary_text.setText("");
        image.setVisibility(View.GONE);

        Toast.makeText(getApplicationContext(), "내용이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void saveDiaryTextToFile(String date, String text) {
        DiaryFileManager.saveDiaryText(this, date, text);
    }

    private String getDiaryTextFromFile(String date) {
        return DiaryFileManager.getDiaryText(this, date);
    }
}