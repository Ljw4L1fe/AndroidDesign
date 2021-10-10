package com.example.picture_sharing_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.dialog.myDialog.MyAlertDialog;

import java.util.Random;

public class MomentActivity extends AppCompatActivity {
    private ImageView ivlike;
    private ImageView ivdownload;
    private ImageView ivshare;
    private ImageView ivimage;
    private ImageView ivdelete;
    private TextView title;
    private TextView description;
    private ImageView authorImg;
    private TextView authorName;
    private TextView likeCount;
    int id = -1;
    boolean mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);
        Intent i = getIntent();
        int pos = i.getIntExtra("pos", -1);
        mode = i.getBooleanExtra("mode", false);
        if (mode) {
            id = cacheInfo.notes.get(pos).id;
        } else {
            id = myCacheInfo.notes.get(pos).id;
        }
        getLikeInfo(id);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.moment_toolbar);
        setSupportActionBar(myToolbar);
        ivimage = findViewById(R.id.moment_image);
        ivlike = findViewById(R.id.moment_like);
        ivdownload = findViewById(R.id.moment_download);
        ivshare = findViewById(R.id.moment_share);
        ivdelete = findViewById(R.id.moment_delete);//删除
        title = findViewById(R.id.moment_title);
        description = findViewById(R.id.moment_context);
        authorImg = findViewById(R.id.moment_head);
        authorName = findViewById(R.id.moment_username);
        likeCount = findViewById(R.id.moment_likecount);
        //赋值
        if (mode) {
            ivimage.setImageBitmap(BitmapFactory.decodeByteArray(cacheInfo.notes.get(pos).imageByte, 0, cacheInfo.notes.get(pos).imageByte.length));
            title.setText(cacheInfo.notes.get(pos).noteName);
            description.setText(cacheInfo.notes.get(pos).subscription);
            authorImg.setImageBitmap(BitmapFactory.decodeByteArray(cacheInfo.notes.get(pos).headByte, 0, cacheInfo.notes.get(pos).headByte.length));
            authorName.setText(cacheInfo.notes.get(pos).author);
            likeCount.setText(currentNote.likes + "");
        } else {
            ivimage.setImageBitmap(BitmapFactory.decodeByteArray(myCacheInfo.notes.get(pos).imageByte, 0, myCacheInfo.notes.get(pos).imageByte.length));
            title.setText(myCacheInfo.notes.get(pos).noteName);
            description.setText(myCacheInfo.notes.get(pos).subscription);
            authorImg.setImageBitmap(BitmapFactory.decodeByteArray(myCacheInfo.notes.get(pos).headByte, 0, myCacheInfo.notes.get(pos).headByte.length));
            authorName.setText(myCacheInfo.notes.get(pos).author);
            likeCount.setText(currentNote.likes + "");
        }

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MomentActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        //删除点击事件
        //这里加个判断，如果图片动态发布人和登录的用户名一致，则把下面的点击事件方法放进去 else就toast个无权删除
        if (!mode)
            ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAlertDialog myAlertDialog = new MyAlertDialog(MomentActivity.this).builder()
                            .setTitle("提示")
                            .setMsg("确认删除？")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Server.DeleteNote(id);
                                    Intent i = new Intent(MomentActivity.this, MyMomentsActivity.class);
                                    myCacheInfo.moreFinished = false;
                                    myCacheInfo.notes = null;
                                    myCacheInfo.finished = false;
                                    myCacheInfo.moreLengths = null;
                                    myCacheInfo.moreNotes = null;
                                    myCacheInfo.lengths = null;
                                    startActivity(i);
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    myAlertDialog.show();

                }
            });
        //喜爱点击事件
        ivlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server.LikeAddOne(id);
                likeCount.setText((Integer.parseInt(likeCount.getText().toString()) + 1) + "");
            }
        });
        //下载点击事件
        ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(ivimage, pos);
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

    private void saveImage(ImageView imageView, int pos) {
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        String titlee = String.valueOf(title.getText());
        if (mode)
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, titlee, cacheInfo.notes.get(pos).subscription);
        else {
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, titlee, myCacheInfo.notes.get(pos).subscription);
        }
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }

    private void getLikeInfo(int id) {
        Server.GetLikes(id);
    }
}