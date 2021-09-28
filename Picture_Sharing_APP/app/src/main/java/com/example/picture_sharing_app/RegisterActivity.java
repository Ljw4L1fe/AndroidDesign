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
        etnewPwd = findViewById(R.id.et_pwd);
        etnewAccount = findViewById(R.id.et_newaccount);//实质上是用户名
        Button btChange = findViewById(R.id.bt_changepwd);
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
                String check1 = "[0-9]{8,11}";//判断用户名规范正则表达式
                String check2 = "[a-z0-9A-Z]{8,16}";//判断密码规范正则表达式
                Pattern regex1 = Pattern.compile(check1);
                Pattern regex2 = Pattern.compile(check2);
                Matcher matcher1 = regex1.matcher(etnewAccount.getText());
                Matcher matcher2 = regex2.matcher(etnewPwd.getText());
                boolean isMatched1 = matcher1.matches();//用户名规范为true 不规范为false
                boolean isMatched2 = matcher2.matches();//密码规范为true 不规范为false
                //判断用户名格式是否正确
                if (isMatched1) {
                    //判断密码格式是否正确
                    if (isMatched2) {
                        //判断两次输入的密码是否一致
                        if (etnewPwd.getText().toString().equals(etconnewPwd.getText().toString())) {
                            Register();
                        } else {
                            clear();
                            Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        clear();
                        Toast.makeText(RegisterActivity.this, "密码格式错误！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    clear();
                    Toast.makeText(RegisterActivity.this, "用户名格式错误！", Toast.LENGTH_SHORT).show();
                }
                //再写一个判断账号是否已存在
            }
        });

    }

    private void Register() {
        new Thread() {
            Socket socket = null;
            Register register = new Register(etnewAccount.getText().toString(), etnewPwd.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(register);

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
                            input.close();
                            output.close();
                            socket.close();
                            break;
                        }
                    }
                    Server.EndSend(output);
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, jsonObject.get("tip").getAsString(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void clear() {
        etnewAccount.setText(null);
        etconnewPwd.setText(null);
        etnewPwd.setText(null);
    }
}