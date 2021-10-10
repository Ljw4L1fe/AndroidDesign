package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePwdActivity extends AppCompatActivity {
    private EditText etchangePwd;
    private EditText etconchangePwd;
    private EditText etorignPwd;
    private Button btChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.changepwd_toolbar);
        setSupportActionBar(myToolbar);
        etchangePwd = findViewById(R.id.et_cnewpwd);//新密码
        etconchangePwd = findViewById(R.id.et_cconnewpwd);
        etorignPwd = findViewById(R.id.et_orginpwd);//原密码
        btChange = findViewById(R.id.bt_changepwd);//
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePwdActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = "[a-z0-9A-Z]{8,16}";//判断密码规范正则表达式
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(etchangePwd.getText());
                boolean isMatched = matcher.matches();//密码规范为true 不规范为false
                //判断输入是否为空
                if (etorignPwd.getText().length() != 0 || etchangePwd.getText().length() != 0 || etconchangePwd.getText().length() != 0) {
                    //判断新密码是否符合规范
                    if (isMatched) {
                        //判断两次输入的密码是否一致
                        if (etchangePwd.getText().toString().equals(etconchangePwd.getText().toString())) {
                            new Thread() {
                                Socket socket = null;
                                ChangePassword changePassword = new ChangePassword(etchangePwd.getText().toString(), etorignPwd.getText().toString());
                                Gson gson = new Gson();
                                String json = gson.toJson(changePassword);

                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        socket = new Socket(Server.host, Server.post);
                                        OutputStream output = socket.getOutputStream();//设置输出流
                                        output.write((json).getBytes("utf-8"));//输出
                                        output.flush();//清空
                                        BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                                        int count = 0;
                                        JsonObject jsonObject;
                                        while (true) {
                                            count = input.available();
                                            if (count > 0) {
                                                byte[] bytes = new byte[count];
                                                input.read(bytes);
                                                JsonParser jp = new JsonParser();//json解析器

                                                jsonObject = jp.parse(new String(bytes)).getAsJsonObject();//字节转字符串再转json
                                                System.out.println(jsonObject.get("tip").getAsString());

                                                break;
                                            }
                                        }
                                        Server.EndSend(output);
                                        Looper.prepare();
                                        Toast.makeText(ChangePwdActivity.this, jsonObject.get("tip").getAsString(), Toast.LENGTH_SHORT).show();
                                        Looper.loop();


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        } else {
                            clear();
                            Toast.makeText(ChangePwdActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        clear();
                        Toast.makeText(ChangePwdActivity.this, "密码格式不规范！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    clear();
                    Toast.makeText(ChangePwdActivity.this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                }
                //还得再写个判断原密码是否正确

            }
        });

    }

    private void clear() {
        etorignPwd.setText(null);
        etchangePwd.setText(null);
        etconchangePwd.setText(null);
    }
}