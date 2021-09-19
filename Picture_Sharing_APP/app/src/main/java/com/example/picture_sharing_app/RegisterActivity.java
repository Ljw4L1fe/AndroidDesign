package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText etnewPwd;
    private EditText etnewAccount;
    private EditText etconnewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(myToolbar);
        etconnewPwd = findViewById(R.id.et_connewpwd);
        etnewPwd = findViewById(R.id.et_newpwd);
        etnewAccount = findViewById(R.id.et_newaccount);
        Button btRegister = findViewById(R.id.bt_register);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String check1 = "[0-9]{8,11}";//判断用户名规范正则表达式
//                String check2 = "[a-z0-9A-Z]{8,16}";//判断密码规范正则表达式
//                Pattern regex1 = Pattern.compile(check1);
//                Pattern regex2 = Pattern.compile(check2);
//                Matcher matcher1 = regex1.matcher(etnewAccount.getText());
//                Matcher matcher2 = regex2.matcher(etnewPwd.getText());
//                boolean isMatched1 = matcher1.matches();//用户名规范为true 不规范为false
//                boolean isMatched2 = matcher2.matches();//密码规范为true 不规范为false
//                if (isMatched1) {
//                    if (isMatched2) {
//                        if (etnewPwd.getText().toString().equals(etconnewPwd.getText().toString())) {
//                            //这里写注册方法
//                            Toast.makeText(RegisterActivity.this, "注册方法！", Toast.LENGTH_SHORT).show();
//                        } else {
//                            clear();
//                            Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        clear();
//                        Toast.makeText(RegisterActivity.this, "密码格式错误！", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    clear();
//                    Toast.makeText(RegisterActivity.this, "用户名格式错误！", Toast.LENGTH_SHORT).show();
//                }
//                //再写一个判断账号是否已存在
            }
        });
    }

    private void clear() {
        etnewAccount.setText(null);
        etconnewPwd.setText(null);
        etnewPwd.setText(null);
    }


}