package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.myapplication.controller.Api;
import com.example.myapplication.dto.post;
import android.util.Base64;

public class SNSActivity extends AppCompatActivity {

    private Api api;
    ImageButton home, heart, chat, add;
    TextView text, like;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snsactivity);

        home = findViewById(R.id.home);
        heart = findViewById(R.id.heart);
        chat = findViewById(R.id.chat);
        add = findViewById(R.id.add);

        text = findViewById(R.id.snsmain);
        img = findViewById(R.id.mainimage);
        like = findViewById(R.id.favoritecounter);

        api = set_retrofit.getClient().create(Api.class);
        Call<post> call = api.getData();
        call.enqueue(new Callback<post>() {
            @Override
            public void onResponse(Call<post> call, Response<post> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response here

                    // image decode (base64(string) -> byte(array) -> bitmap(android img)
                    post sample_post = response.body();
                    byte[] byte_image = Base64.decode(sample_post.get_img(), Base64.DEFAULT);
                    Bitmap bitmap_image = BitmapFactory.decodeByteArray(byte_image, 0, byte_image.length);

                    img.setImageBitmap(bitmap_image);
                    text.setText(sample_post.get_txt());

                    int like_count = sample_post.get_like();
                    String str_like = "" + like_count;
                    like.setText("좋아요 " + str_like + "개");


                } else {
                    // Handle error response
                    text.setText("Code: " + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<post> call, Throwable t) {
                text.setText(t.getMessage());
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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SNSActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SNSActivity.this, AddPhotoActivity.class);
                startActivity(intent);
            }
        });




    }
}