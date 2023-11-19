package com.example.myapplication.Diary_Calender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class DiaryFileManager {

    public static final String FILE_NAME_PREFIX = "diary_";

    public static void saveDiaryText(Context context, String date, String text) {
        // 파일 이름에 연도, 월, 일 정보를 포함하여 생성
        String fileName = FILE_NAME_PREFIX + date.replace(".", "_") + ".txt";
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static String getDiaryText(Context context, String date) {
        // 파일 이름에 연도, 월, 일 정보를 포함하여 생성
        String fileName = FILE_NAME_PREFIX + date.replace(".", "_") + ".txt";
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFileStreamPath(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }



    public static void saveDiaryImageToFile(Context context, String date, Bitmap imageBitmap) {
        String fileName = FILE_NAME_PREFIX + date.replace(".", "_") + "_image.png";
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getDiaryImageFromFile(Context context, String date) {
        String fileName = FILE_NAME_PREFIX + date.replace(".", "_") + "_image.png";
        try (FileInputStream fis = context.openFileInput(fileName)) {
            return BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




}
