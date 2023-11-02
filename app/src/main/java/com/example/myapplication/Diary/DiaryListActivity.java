package com.example.myapplication.Diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.chat.FriendItem;
import com.example.myapplication.chat.MyRecyclerAdapter;

import java.util.ArrayList;

public class DiaryListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ArrayList<FriendItem> mDiaryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /* initiate adapter */
        mRecyclerAdapter = new MyRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));


        /* adapt data */
        mDiaryItems = new ArrayList<>();
        for(int i=1;i<=10;i++){
            mDiaryItems.add(new FriendItem(R.drawable.pinokio,i+"번째 메모","2023-11-02"));
        }
        mRecyclerAdapter.setFriendList(mDiaryItems);
    }
}