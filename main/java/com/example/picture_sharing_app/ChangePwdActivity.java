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

public class ChangePwdActivity extends AppCompatActivity {
    private EditText etchangePwd;
    private EditText etconchangePwd;
    private EditText etorignPwd;
    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.changepwd_toolbar);
        setSupportActionBar(myToolbar);
        etchangePwd = findViewById(R.id.et_cnewpwd);//新密码
        etconchangePwd = findViewById(R.id.et_cconnewpwd);
        etorignPwd = findViewById(R.id.et_orginpwd);//原密码
        btRegister = findViewById(R.id.bt_changepwd);//
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePwdActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etchangePwd.getText().toString().isEmpty()
                        || etconchangePwd.getText().toString().isEmpty()
                ||etorignPwd.getText().toString().isEmpty()){
                    return;
                }
                new Thread(){
                    Socket socket=null;
                    ChangePassword changePassword = new ChangePassword(etchangePwd.getText().toString(),etorignPwd.getText().toString());
                    Gson gson=new Gson();
                    String json= gson.toJson(changePassword);

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

                                    break;
                                }
                            }
                            Looper.prepare();
                            Toast.makeText(ChangePwdActivity.this, jsonObject.get("tip").getAsString(), Toast.LENGTH_SHORT).show();
                            Looper.loop();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }
}