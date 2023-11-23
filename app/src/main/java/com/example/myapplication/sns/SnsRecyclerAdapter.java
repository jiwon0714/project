package com.example.myapplication.sns;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.chat.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SnsRecyclerAdapter extends RecyclerView.Adapter<SnsRecyclerAdapter.ViewHolder> {
    private ArrayList<SnsItem> mSnsList;
    private ArrayList<Comment_Item> commentList;

    private EditText et_comment;

    private CommentAdapter adapter;

    int click = 0;
    int numofheart = 0;
    @NonNull
    @Override
    public SnsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sns_item_recyclerview, parent, false);
        return new SnsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mSnsList.get(position));

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click ==0) {
                    holder.heart.setImageResource(R.drawable.fillheart);
                    numofheart++;
                    holder.heartCounter.setText("좋아요 " + numofheart + "개");
                    click ++;
                }else{
                    holder.heart.setImageResource(R.drawable.heart);
                    --numofheart;
                    holder.heartCounter.setText("좋아요 " + numofheart + "개");
                    click --;
                }
            }
        });






        //댓글창
        holder.chat_to_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog 빌더 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());


                // View를 인플레이트할 때 holder.itemView.getContext()를 사용
                View dialogView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.dialog_sns_comment, null);

                // AlertDialog에 리사이클러뷰 추가
                RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
                recyclerView.setLayoutManager(layoutManager);

                // 리사이클러뷰 어댑터 설정
                commentList = new ArrayList<>();

                adapter = new CommentAdapter(commentList);
                recyclerView.setAdapter(adapter);

                // AlertDialog 생성 및 표시
                AlertDialog alertDialog = builder.create();
                alertDialog.setView(dialogView);  // AlertDialog에 설정한 뷰를 세팅

                // 커스텀 애니메이션 적용
                Window window = alertDialog.getWindow();

                // AlertDialog의 크기 및 위치 조절
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // AlertDialog의 크기 조절
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                        // 세로 길이를 화면의 절반으로 설정
                        layoutParams.height = (int) (holder.itemView.getContext().getResources().getDisplayMetrics().heightPixels * 0.6);

                        // AlertDialog를 하단에 위치시키고 여백 없이 설정
                        layoutParams.gravity = Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;



                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        if (window != null) {
                            // 열기&닫기 시 애니메이션 설정
                            layoutParams.windowAnimations = R.style.AnimationPopupStyle;
                            window.setAttributes( layoutParams );
                            // dim 처리
                            window.setDimAmount(0.7f); // 원하는 dim 정도로 조절
                        }

                        alertDialog.getWindow().setAttributes(layoutParams);
                    }
                });

                Button btn_comment = dialogView.findViewById(R.id.btn_comment);
                et_comment = dialogView.findViewById(R.id.et_comment);


                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendMessage();
                    }
                });

                et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            sendMessage();
                            return true;
                        }
                        return false;
                    }
                });


                alertDialog.show();
            }
        });
    }

    private void sendMessage() {
        String commentContext = et_comment.getText().toString();

        if (!commentContext.isEmpty()) {
            String commentTime = getCurrentTimestamp();

            // 프로필 이미지와 이름을 가져와서 Comment_Item을 생성하는 부분(db에서 정보 받아와서)
            // CircleImageView profileImage = ...
            // String name = ...

            Comment_Item newComment = new Comment_Item(commentContext, commentTime);

            // 어댑터에 메시지 추가
            adapter.addComment(newComment);

            // 입력 필드 비우기
            et_comment.setText("");
        }
    }
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void setSnsList(ArrayList<SnsItem> list){
        this.mSnsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            return mSnsList.size();
        }catch (Exception e) {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        ImageView main_image;
        TextView name;
        TextView text;
        TextView comment;
        TextView comment_name;
        ImageView heart;
        ImageView chat_to_comment;
        TextView heartCounter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_name = (TextView) itemView.findViewById(R.id.comment_name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            main_image = (ImageView) itemView.findViewById(R.id.main_image);
            heart = (ImageView) itemView.findViewById(R.id.heart);
            profile = (ImageView) itemView.findViewById(R.id.img_profile);
            name = (TextView) itemView.findViewById(R.id.tv_profile);
            text = (TextView) itemView.findViewById(R.id.sns_main);
            chat_to_comment = (ImageView) itemView.findViewById(R.id.chat);
            heartCounter = (TextView)  itemView.findViewById(R.id.favoritecounter);
        }

        void onBind(SnsItem item){
            Bitmap mainImageBitmap = item.getResourceId_main_image();

            profile.setImageResource(R.drawable.pinokio_circle);
            main_image.setImageBitmap(mainImageBitmap);

            name.setText(item.getName());
            text.setText(item.getText());
            comment.setText(item.getComment());
            comment_name.setText(item.getComment_name());
        }
    }
}
