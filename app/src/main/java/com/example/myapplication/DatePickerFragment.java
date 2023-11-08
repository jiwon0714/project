package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_datepicker, null);
        NumberPicker yearPicker = dialogView.findViewById(R.id.yearpicker_datepicker);
        NumberPicker monthPicker = dialogView.findViewById(R.id.monthpicker_datepicker);
        NumberPicker dayPicker = dialogView.findViewById(R.id.datepicker_datepicker);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);




        //  순환 안되게 막기
        yearPicker.setWrapSelectorWheel(false);
        monthPicker.setWrapSelectorWheel(false);
        dayPicker.setWrapSelectorWheel(false);


        yearPicker.setMinValue(currentYear - 100); // 최소값 설정
        yearPicker.setMaxValue(currentYear); // 최대값 설정
        yearPicker.setValue(year);

        monthPicker.setMinValue(1); // 최소값 설정
        monthPicker.setMaxValue(12); // 최대값 설정
        monthPicker.setValue(month + 1);

        dayPicker.setMinValue(1); // 최소값 설정
        dayPicker.setMaxValue(31); // 최대값 설정
        dayPicker.setValue(day);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        // "취소" 버튼 클릭 시
        Button cancel = dialogView.findViewById(R.id.cancel_button_datepicker);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // 다이얼로그를 닫습니다.
            }
        });

        // "완료" 버튼 클릭 시
        Button save = dialogView.findViewById(R.id.save_button);

        EditText etBirth = getActivity().findViewById(R.id.et_birth);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedYear = yearPicker.getValue();
                int selectedMonth = monthPicker.getValue();
                int selectedDay = dayPicker.getValue();

                String selectedDate = selectedYear + "년 " + selectedMonth + "월 " + selectedDay + "일";
                Toast.makeText(getActivity(), selectedDate, Toast.LENGTH_SHORT).show();

                etBirth.setText(" "+selectedDate);


                dismiss(); // 다이얼로그를 닫습니다.
            }
        });

        return builder.create();


    }


}

