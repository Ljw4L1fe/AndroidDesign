package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST_CODE = 0;
    //得到图片的路径
    private String path;
    private CircleImageView iv_photo;
    private ImageView ivselectimg;
    private RadioGroup radiogroup_gender;
    private TextView tvchangepwd;
    private EditText et_name;
    private EditText et_subscription;
    private String sex = "未知";
    private Button btn_save;
    private RadioButton rbmale;
    private RadioButton rbfemale;
    private boolean changePhoto = false;
    private byte[] imgbytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar myToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(myToolbar);
        iv_photo = findViewById(R.id.profile_image);//头像图片
        btn_save = findViewById(R.id.bt_save);
        //匹配
        rbmale = findViewById(R.id.radioButton_male);
        rbfemale = findViewById(R.id.radioButton_female);
        ivselectimg = findViewById(R.id.iv_selectimg);
        radiogroup_gender = findViewById(R.id.radioGroup_gender);
        tvchangepwd = findViewById(R.id.tv_changepwd);
        et_name = findViewById(R.id.profile_account);
        et_subscription = findViewById(R.id.profile_introduceinput);
        //赋值
        iv_photo.setImageBitmap(User.headBitmap);//头像
        et_name.setText(User.userInfo.get("name").getAsString());//用户名
        String gender = User.userInfo.get("sex").getAsString();
        if (gender.equals("男")) {
            rbmale.setChecked(true);
            sex = "男";
        } else {
            rbfemale.setChecked(true);
            sex = "女";
        }
        et_subscription.setText(User.userInfo.get("subscription").getAsString());//密码
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSave();
            }
        });
        tvchangepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ChangePwdActivity.class);
                startActivity(i);
            }
        });
        //性别选择
        radiogroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {  //获取所选择值
                RadioButton check = (RadioButton) findViewById(checkedId);
                sex = check.getText().toString().trim();
            }
        });
        ivselectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateSave() {
        new Thread() {
            Socket socket = null;
            UpdateInfo updateInfo = new UpdateInfo(User.account, et_name.getText().toString(), sex, et_subscription.getText().toString(), User.userInfo.get("uid").getAsInt());

            @Override
            public void run() {
                super.run();
                if (changePhoto) {
                    updateInfo.headLength = imgbytes.length;
                }
                Gson gson = new Gson();
                String json = gson.toJson(updateInfo);
                try {
                    socket = new Socket(Server.host, Server.post);
                    OutputStream output = socket.getOutputStream();//设置输出流
                    output.write((json).getBytes("utf-8"));//输出
                    output.flush();//清空
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                    int count = 0;
                    JsonObject jsonObject;
                    JsonParser jp = new JsonParser();//json解析器
                    while (true) {
                        count = input.available();
                        if (count > 0) {
                            byte[] bytes = new byte[count];
                            input.read(bytes);
                            jsonObject = jp.parse(new String(bytes)).getAsJsonObject();//字节转字符串再转json
                            if (changePhoto) {
                                changePhoto = false;
                                if (jsonObject.get("tip").getAsString().equals("可以传图片")) {
                                    output.write(imgbytes);
                                    output.flush();
                                    while (true) {
                                        count = input.available();
                                        if (count > 0) {
                                            bytes = new byte[count];
                                            input.read(bytes);
                                            jsonObject = jp.parse(new String(bytes)).getAsJsonObject();//字节转字符串再转json
                                            System.out.println(jsonObject.get("tip").getAsString());
                                        }
                                    }
                                }
                            } else {
                                System.out.println(jsonObject.get("tip").getAsString());
                            }
                            Server.EndSend(output);
                            input.close();
                            output.close();
                            socket.close();
                            break;
                        }
                    }
                    Looper.prepare();
                    Toast.makeText(ProfileActivity.this, jsonObject.get("tip").getAsString(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showBottomDialog() {
        //使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //设置布局
        View view = View.inflate(this, R.layout.dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.choosepic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromGallery();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void selectFromGallery() {
        //在这里跳转到手机系统相册里面
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
        iv_photo.setImageURI(selectedImage);
        imgbytes = PhotoSystem.getImageByte(selectedImage, ProfileActivity.this);
        System.out.println("选择完成！：img=" + imgbytes.length);
        changePhoto = true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton_male:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radioButton_female:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}