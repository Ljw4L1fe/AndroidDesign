package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Sampler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class LoginActivity extends AppCompatActivity {
    private Boolean bPwdSwitch = false;//密码是否可见
    private EditText etPwd;//密码行
    private EditText etAccount;//账号行
    private String host;//服务器地址
    private int post;//端口号
   // List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        list.add(6);
//        list.add(7);
//        list.add(8);
//        Gson gson = new Gson();
//        System.out.println(list);
//        System.out.println(gson.toJson(list));
//
//        List<Integer> tList = gson.fromJson(gson.toJson(list), new TypeToken<List<Integer>>(){}.getType());
//        System.out.println(tList);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);//登录顶端条
        setSupportActionBar(myToolbar);
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);//密码是否可见图片
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        Button btLogin = findViewById(R.id.bt_login);
        host = Server.host;
        post = getResources().getInteger(R.integer.post);
        System.out.println(accountt.accountt);
        if(accountt.accountt!=0){
            etAccount.setText(String.valueOf(accountt.accountt));
            accountt.accountt=0;
        }
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bPwdSwitch = !bPwdSwitch;
                if (bPwdSwitch) {
                    ivPwdSwitch.setImageResource(R.drawable.ic_outline_visibility_24);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ivPwdSwitch.setImageResource(R.drawable.ic_outline_visibility_off_24);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    etPwd.setTypeface(Typeface.DEFAULT);
                }
            }
        });//点击是否可见事件
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_gotoregister:
                        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });//注册事件
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check1 = "[0-9]{7,11}";//判断用户名规范正则表达式
                Pattern regex1 = Pattern.compile(check1);
                Matcher matcher1 = regex1.matcher(etAccount.getText());
                boolean isMatched1 = matcher1.matches();//用户名规范为true 不规范为false
                if(isMatched1){
                    if(!etPwd.getText().toString().isEmpty()){
                        ConnectToServer();
                    }else{
                        clear();
                        Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    clear();
                    Toast.makeText(LoginActivity.this, "账号格式错误！", Toast.LENGTH_SHORT).show();
                }

            }
        });//登录事件
    }
    public void ConnectToServer() {
        new Thread(){
            @Override
            public void run(){
                super.run();
                if(etAccount.getText().toString().isEmpty() || etPwd.getText().toString().isEmpty()){
                    return;
                }
                Socket socket=null;
                CheckUser check1User=new CheckUser(Integer.parseInt(etAccount.getText().toString()),etPwd.getText().toString());
                Gson gson=new Gson();
                String json= gson.toJson(check1User);
                //String checkUser = "a:" + etAccount.getText().toString() + ":" + etPwd.getText().toString();//输出格式[a:账号:密码]
                System.out.println("发送了 ["+json+"]");
                try {
                    socket = new Socket(host,post);//socket连接中..
                    OutputStream output=socket.getOutputStream();//设置输出流
                    output.write((json).getBytes("utf-8"));//输出
                    output.flush();//清空

                    BufferedInputStream pre = new BufferedInputStream(socket.getInputStream());
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());//接收流

                    int[] length={0,0,0};


                    while (true){
                        length[0]=pre.available();
                        if(length[0]>0){
                            System.out.println("preSize:"+length[0]);
                            byte[] preBytes=new byte[length[0]];
                            pre.read(preBytes);
                            JsonParser jp = new JsonParser();//json解析器
                            System.out.println(new String(preBytes));
                            JsonObject jsonObject = jp.parse(new String(preBytes)).getAsJsonObject();//字节转字符串再转json
                            length[1] = jsonObject.get("info").getAsInt();
                            length[2] = jsonObject.get("imageSize").getAsInt();
                            System.out.println("要接收 ；" + length[1]+"  ："+length[2]);
                            break;
                        }
                    }
                    //Charset charset=Charset.forName("utf-8");
                    output.write(("{\"option\":2}").getBytes("utf-8"));//输出
                    output.flush();//清空
                    byte[] infoBytes =new byte[length[1]];
                    byte[] headBytes =new byte[length[2]];
                    int count=0;//输入流剩余量.
                    int infoGet=length[1];
                    int infohas=0;
                    int imageGet=length[2];
                    int imagehas=0;
                    while (true){//持续等待接收
                        count=input.available();//接收长度(Byte)
                        if(count>0){ //检测到信息
                                System.out.println("insert:"+count);
                                if(infoGet>0) {
                                    infoGet=(input.available()/infoGet)>=1?infoGet:input.available();
                                    if(infohas<length[1]) {
                                        System.out.println(input.available()/infoGet);
                                        System.out.println("infohas="+infohas  +"infoGet="+infoGet);
                                        input.read(infoBytes, infohas, infoGet);//接收中.
                                        infohas += infoGet;
                                    }else{//info读取结束
                                        infoGet=0;
                                        JsonParser jp = new JsonParser();//json解析器
                                        System.out.println(new String(infoBytes));
                                        JsonObject jsonObject = jp.parse(new String(infoBytes)).getAsJsonObject();//字节转字符串再转json
                                        if (!jsonObject.has("tip")) { //密码正确
                                            User.account = Integer.parseInt(etAccount.getText().toString());//单例模式为User设置初值.
                                            User.password = etPwd.getText().toString();
                                            User.userInfo = jsonObject;
                                        } else {//密码错误
                                            Looper.prepare();
                                            Toast.makeText(LoginActivity.this, jsonObject.get("tip").getAsString(), Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                            return;
                                        }
                                    }
                                }
                                if(infoGet==0){
                                    if(imageGet>0) {
                                        imageGet = (input.available() / imageGet) >=1 ? imageGet : input.available();
                                        if(imagehas<length[2]) {
                                            input.read(headBytes, imagehas, imageGet);//接收中.
                                            imagehas += imageGet;
                                            System.out.println("imageHas" + imagehas + "headbyte.length=" + length[2] + "imageget=" + imageGet);
                                            if(imagehas>=headBytes.length){
                                                System.out.println("else");
                                                imageGet=0;
                                                User.headBitmap= BitmapFactory.decodeByteArray(headBytes,0, headBytes.length);
                                            }
                                        }
                                    }
                                }
                                if(imageGet==0){
                                    //Server.EndSend(output);
                                    System.out.println("finish");
                                    input.close();//三连关
                                    output.close();
                                    socket.close();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    LoginActivity.this.finish();
                                }
                               // input.read(headBytes,length[1],length[2]);
                            count=0;
                               // break;
                     /*       {
                                JsonParser jp = new JsonParser();//json解析器
                                JsonObject jsonObject = jp.parse(new String(bytes)).getAsJsonObject();//字节转字符串再转json
                                System.out.println("youl");
//                                 PersonInfo personInfo=(PersonInfo) JSONObject.toBean(JSONObject.fromObject(jsonObject),PersonInfo.class);
//                            System.out.println(personInfo.personInfoAsString.name);
                                // String message = jsonObject.get("name").getAsString();
                                if (!jsonObject.has("error")) { //密码正确
                                    User.account = Integer.parseInt(etAccount.getText().toString());//单例模式为User设置初值.
                                    User.password = etPwd.getText().toString();
                                    User.userInfo = jsonObject;
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);//跳转后该函数依然会执行到结束
                                } else {//密码错误
                                    count = 0;
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, jsonObject.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
*/
//                            input.close();//三连关
//                            output.close();
//                            socket.close();

                        }

                    }


//                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    char[] buffer = new char[255];//接收字符
//                    int length=0;//接收长度
//                    while((length=reader.read(buffer))>0){//等待接收
//
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }.start();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_toolbar_menu, menu);
        return true;
    }

    private void clear() {
        etAccount.setText(null);
        etPwd.setText(null);
    }

}