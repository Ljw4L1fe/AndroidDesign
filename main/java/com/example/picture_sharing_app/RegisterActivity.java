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
        etnewAccount=findViewById(R.id.et_username);//实质上是用户名
        Button btChange=findViewById(R.id.bt_changepwd);
        Button btRegister=findViewById(R.id.bt_register);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etnewPwd.getText().length()==0 || etconnewPwd.getText().length()==0){
                    clear();
                    Toast.makeText(RegisterActivity.this,"密码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(!etnewPwd.getText().toString().equals(etconnewPwd.getText().toString())){
                    clear();
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                }else {
                    Register();
                }
                //再写一个判断账号是否已存在


            }
        });

        }

        private void Register(){
        new Thread(){
            Socket socket=null;
            Register register=new Register(etnewAccount.getText().toString(),etnewPwd.getText().toString());
            Gson gson=new Gson();
            String json= gson.toJson(register);
            @Override
            public void run() {
                super.run();
                try {
                    socket=new Socket(Server.host,Server.post);
                    OutputStream output=socket.getOutputStream();//设置输出流
                    output.write((json).getBytes("utf-8"));//输出
                    output.flush();//清空
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                    int count=0;
                    JsonObject jsonObject;
                    while (true){
                        count=input.available();
                        if(count>0){
                            byte[] bytes=new byte[count];
                            input.read(bytes);
                            JsonParser jp = new JsonParser();//json解析器
                            jsonObject= jp.parse(new String(bytes)).getAsJsonObject();//字节转字符串再转json
                            System.out.println(jsonObject.get("tip").getAsString());
                            input.close();
                            output.close();
                            socket.close();
                            break;
                        }
                    }
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, jsonObject.get("tip").getAsString(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        }
    private void clear(){
        etnewAccount.setText(null);
        etconnewPwd.setText(null);
        etnewPwd.setText(null);
    }
}