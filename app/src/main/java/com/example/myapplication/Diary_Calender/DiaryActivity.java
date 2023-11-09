package com.example.myapplication.Diary_Calender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Navi;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {


    CalendarView calendar;
    ImageView image;

    TextView selected_date;
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
                        // CalendarView를 다시 숨김
                        //calendar.setVisibility(View.GONE);
                    }
                });
            }
        });




    }
}