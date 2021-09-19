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
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST_CODE = 0;
    //得到图片的路径
    private String path;
    private CircleImageView iv_photo;
    private ImageView ivselectimg;
    private RadioGroup radiogroup_gender;
    private TextView tvchangepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar myToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(myToolbar);
        iv_photo = findViewById(R.id.profile_image);
        ivselectimg = findViewById(R.id.iv_selectimg);
        radiogroup_gender = findViewById(R.id.radioGroup_gender);
        tvchangepwd = findViewById(R.id.tv_changepwd);
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
                System.out.println(check.getText().toString().trim());//check.getText().toString().trim()值为男或女
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
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                i.putExtra("flag",2);
                startActivity(i);
            }
        });
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
        if (requestCode == IMAGE_REQUEST_CODE) {//确定返回到那个Activity的标志
            if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                try {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path = cursor.getString(columnIndex);  //获取照片路径
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    iv_photo.setImageBitmap(bitmap);
                } catch (Exception e) {
                    // TODO Auto-generatedcatch block
                    e.printStackTrace();
                }
            }
        }
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
}