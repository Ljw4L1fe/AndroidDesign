package com.example.picture_sharing_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddFragment extends Fragment {
    //得到图片的路径
    private Context context = null;
    private ImageView ivadd;
    private ImageView ivaddview;
    private EditText title;
    private EditText sub;
    private TextView subtitle;
    private Button btnadd;
    private static final int IMAGE_REQUEST_CODE = 0;
    private byte[] imgByte;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ivadd = view.findViewById(R.id.add_image);
        ivaddview = view.findViewById(R.id.add_image_view);
        btnadd = view.findViewById(R.id.btn_add_upload);
        title = view.findViewById(R.id.add_title);
        subtitle = view.findViewById(R.id.add_subcontext);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击发布后出发的事件
                if(imgByte!=null) {
                    if(title.getText().length()!=0){
                        if(subtitle.getText().length()!=0){
                            Server.AddToServer(imgByte, new addInfo(title.getText().toString(), subtitle.getText().toString(),imgByte.length));
                            clear();
                            Intent i = new Intent(context, MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(context, "请输入描述内容！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "请输入标题！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "请选择一张图片！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
        return view;
    }

    private void selectFromGallery() {
        //在这里跳转到手机系统相册里面
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private void showBottomDialog() {
        //使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //设置布局
        View view = View.inflate(context, R.layout.dialog, null);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri

//        File file = new File(Uri2PathUtil.getRealPathFromUri(this.context,selectedImage));
//        System.out.println(selectedImage.getPath());
//        FileInputStream in = null;
//        int total=(int) file.length();
//        System.out.println("total="+total);
//        imgByte=new byte[total];
//        int hasGet=0;int len;int count=0;
//        boolean finished=false;
//        try {
//            in = new FileInputStream(file);
//            while (true){
//                count=in.available();
//                if(count>0){
//                    if(hasGet+count>total){
//                        if(count>total){
//                            len=total;
//                        }else {
//                            len=total-hasGet;
//                        }
//                    }else {
//                        len=count;
//                    }
//
//                    in.read(imgByte,hasGet,len);
//                    if(total==(hasGet+len)){
//                        finished=true;
//                    }
//                    hasGet+=count;
//                    if(finished){
//                        System.out.println("---------------------------finished");
//                        break;
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        ivaddview.setImageURI(selectedImage);
        imgByte=getImageByte(selectedImage);
    }

    public byte[] getImageByte(Uri uri)
    {
       // uri.get
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bitmap bitmap=imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void clear(){
        ivaddview.setImageDrawable(null);
        title.setText(null);
        subtitle.setText(null);
    }
}

