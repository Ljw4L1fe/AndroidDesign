package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MomentActivity extends AppCompatActivity {
    private ImageView ivlike;
    private ImageView ivdownload;
    private ImageView ivshare;
    private ImageView ivimage;
    private TextView title;
    private TextView description;
    private ImageView authorImg;
    private TextView authorName;
    private TextView likeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);
        Intent i=getIntent();
        int pos=i.getIntExtra("pos",-1);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.moment_toolbar);
        setSupportActionBar(myToolbar);
        ivimage = findViewById(R.id.moment_image);
        ivlike = findViewById(R.id.moment_like);
        ivdownload = findViewById(R.id.moment_download);
        ivshare = findViewById(R.id.moment_share);
        title = findViewById(R.id.moment_title);
        description = findViewById(R.id.moment_context);
        authorImg = findViewById(R.id.moment_head);
        authorName = findViewById(R.id.moment_username);
        likeCount = findViewById(R.id.moment_likecount);
        //赋值
        ivimage.setImageBitmap(BitmapFactory.decodeByteArray(cacheInfo.notes.get(pos).imageByte, 0, cacheInfo.notes.get(pos).imageByte.length));
        title.setText(cacheInfo.notes.get(pos).noteName);
        description.setText(cacheInfo.notes.get(pos).subscription);
        authorImg.setImageBitmap(BitmapFactory.decodeByteArray(cacheInfo.notes.get(pos).headByte, 0, cacheInfo.notes.get(pos).headByte.length));
        authorName.setText(cacheInfo.notes.get(pos).author);
        likeCount.setText(cacheInfo.notes.get(pos).likes +"");
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
               Server.LikeAddOne(cacheInfo.notes.get(pos).id);
               likeCount.setText((Integer.parseInt(likeCount.getText().toString())+1)+"");
            }
        });
        //下载点击事件
        ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(ivimage,pos);
                Toast.makeText(MomentActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
            }
        });
        //分享点击事件
        ivshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void saveImage(ImageView imageView,int pos) {
        imageView.setDrawingCacheEnabled(true);//设置完才能获取bitmap图片
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        String titlee=title.getText().toString();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, titlee,cacheInfo.notes.get(pos).subscription);
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }
}