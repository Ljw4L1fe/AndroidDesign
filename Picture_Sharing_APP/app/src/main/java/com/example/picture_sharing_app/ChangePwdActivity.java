package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePwdActivity extends AppCompatActivity {
    private EditText etchangePwd;
    private EditText etconchangePwd;
    private EditText etorignPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.changepwd_toolbar);
        setSupportActionBar(myToolbar);
        etchangePwd = findViewById(R.id.et_cnewpwd);
        etconchangePwd = findViewById(R.id.et_cconnewpwd);
        etorignPwd = findViewById(R.id.et_orginpwd);
        Button btChange = findViewById(R.id.bt_changepwd);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePwdActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String check = "[a-z0-9A-Z]{8,16}";//判断密码规范正则表达式
//                Pattern regex = Pattern.compile(check);
//                Matcher matcher = regex.matcher(etchangePwd.getText());
//                boolean isMatched = matcher.matches();//密码规范为true 不规范为false
//                if (etorignPwd.getText().length() == 0 || etchangePwd.getText().length() == 0 || etconchangePwd.getText().length() == 0) {
//                    if (isMatched) {
//                        if (etchangePwd.getText().toString().equals(etconchangePwd.getText().toString())) {
//                            //这里写修改密码的方法
//                        } else {
//                            clear();
//                            Toast.makeText(ChangePwdActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        clear();
//                        Toast.makeText(ChangePwdActivity.this, "密码格式不规范！", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    clear();
//                    Toast.makeText(ChangePwdActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
//                }
//                //还得再写个判断原密码是否正确
            }
        });
    }

    private void clear() {
        etorignPwd.setText(null);
        etchangePwd.setText(null);
        etconchangePwd.setText(null);
    }
}