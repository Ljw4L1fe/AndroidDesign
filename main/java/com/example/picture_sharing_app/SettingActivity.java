package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(myToolbar);
        Button btExit = findViewById(R.id.bt_exit);
        Button btClear = findViewById(R.id.bt_clear);
        Button btAbout = findViewById(R.id.bt_exit);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //退出登录按钮点击事件
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}