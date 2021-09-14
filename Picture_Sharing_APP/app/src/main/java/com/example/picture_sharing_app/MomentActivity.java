package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MomentActivity extends AppCompatActivity {
    private ImageView ivlike;
    private ImageView ivdownload;
    private ImageView ivshare;
    private ImageView ivimage;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.moment_toolbar);
        setSupportActionBar(myToolbar);
        ivimage = findViewById(R.id.moment_image);
        ivlike = findViewById(R.id.moment_like);
        ivdownload = findViewById(R.id.moment_download);
        ivshare = findViewById(R.id.moment_share);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MomentActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        //喜爱点击事件
        ivlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //下载点击事件
        ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(ivimage);
            }
        });
        //分享点击事件
        ivshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void saveImage(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        Random rand1=new Random();
        int rand=rand1.nextInt();
        String title=String.valueOf(rand);
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, title,description);
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }
}