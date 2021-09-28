package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyMomentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MomentsAdapter momentsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moments);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mymoments_toolbar);
        setSupportActionBar(myToolbar);
        recyclerView = findViewById(R.id.my_comments_list);
        momentsAdapter = new MomentsAdapter(MyMomentsActivity.this, R.layout.list_item, cacheInfo.notes);
        LinearLayoutManager llm = new LinearLayoutManager(MyMomentsActivity.this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(momentsAdapter);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}