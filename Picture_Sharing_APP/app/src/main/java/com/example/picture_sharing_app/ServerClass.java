package com.example.picture_sharing_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerClass {


}
class Server{
    public final static String host="10.34.119.9";
    public final static int post=4040;
    public static void EndSend(OutputStream outputStream){
        try {
            outputStream.write(("{\"option\":10}").getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  <T> void ThreadToServer(T struct,int option){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Socket socket=null;
                T t=struct;
                Gson gson=new Gson();
                String json= gson.toJson(t);
                try {
                    socket=new Socket(Server.host,Server.post);
                    OutputStream output=socket.getOutputStream();//设置输出流
                    output.write((json).getBytes("utf-8"));//输出
                    output.flush();//清空
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                    int count=0;
                    byte[] bytes;
                    JsonObject jsonObject;
                    boolean getInfo=false;
                    mainInfo mainInfo=null;
                    while (true){
                        count=input.available();
                        if(count>0){
                            bytes = new byte[count];
                            input.read(bytes);
                            System.out.println(count);
                            System.out.println(new String(bytes));
                            JsonParser jp = new JsonParser();//json解析器
                            //JsonArray ja=jp.parse(new String(bytes)).getAsJsonArray();
                            //字节转字符串再转json
                            mainInfo = new Gson().fromJson(new String(bytes), mainInfo.class);
                            cacheInfo.notes=mainInfo.notes;
                            cacheInfo.lengths=mainInfo.lengths;

                            System.out.println(mainInfo.lengths.size());
                            System.out.println(mainInfo.notes.size());
                            break;
                        }
                    }
                    output.write("{\"option\":7}".getBytes());
                    output.flush();
                    int all=0;int getall=0;
                    for(int i=0;i< mainInfo.lengths.size()*2;i++){
                        int total;
                        if(i>=mainInfo.lengths.size()){
                            total=mainInfo.headlens.get(i%mainInfo.lengths.size());
                        }else {
                            total = mainInfo.lengths.get(i);
                        }
                        int get=0;int len=0;
                        boolean finished=false;
                        byte[] imgbyte= new byte[total];
                        while (true){
                            count=input.available();
                            if(count>0){

                                if(get+count>total){
                                    if(count>total){
                                        len=total;
                                    }else {
                                        len=total-get;
                                    }
                                }else {
                                    len=count;
                                }
                                getall+=len;
                                input.read(imgbyte,get,len);
                                if(total==(get+len)){
                                    finished=true;
                                }
                                System.out.println("len="+len);
                                System.out.println("coun="+count);
                                System.out.println("get="+get);
                                System.out.println("all="+all+"  total="+total+"   getall="+getall);
                                get+=count;
                                if(finished){
                                    if(i>=mainInfo.lengths.size()){
                                        cacheInfo.notes.get(i%mainInfo.headlens.size()).headByte=imgbyte;
                                    }else {
                                        cacheInfo.notes.get(i).imageByte=imgbyte;
                                        System.out.println("---------------------photo--finished");
                                    }
                                    break;
                                }
                            }

                        }
                    }
                    Server.EndSend(output);
                    System.out.println("over");
                    cacheInfo.finished=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static  void AddToServer(byte[] bytes,addInfo addinfo){
        new Thread(){
            Socket socket=null;
            @Override
            public void run() {
                super.run();
                try {
                    String json = new Gson().toJson(addinfo);
                    socket=new Socket(Server.host,Server.post);
                    OutputStream output=socket.getOutputStream();//设置输出流
                    output.write((json).getBytes("utf-8"));//输出
                    output.flush();//清空
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                    JsonObject jsonObject;
                    int count;
                    while (true){
                        count=input.available();
                        if(count>0){
                            byte[] recive = new byte[count];
                            input.read(recive,0,count);
                            System.out.println(new String(recive));
                            JsonParser jp = new JsonParser();
                            jsonObject=jp.parse(new String(recive)).getAsJsonObject();
                            break;
                        }
                    }
                    if(jsonObject.has("tip")){
                        if (jsonObject.get("tip").getAsString().equals("可以传图片")){
                            output.write(bytes,0,bytes.length);
                            output.flush();
                            System.out.println("sendfinished bytes="+bytes.length);
                           // output.write("{\"option\":10}".getBytes());
                          //  output.flush();
                        }else { System.out.println("error:have not the string");}
                    }else {System.out.println("error:has tip"); }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    };
}

class CheckUser{
    CheckUser(int account ,String password){

    this.account=account;
    this.password=password;
    }
    int option=1;
    int account;
    String password;

}

class ChangePassword{
    int option=3;
    int account;
    String orgPassword;
    String newPassword;
    ChangePassword(String newPassword,String orgPassword){
        this.newPassword=newPassword;
        this.orgPassword=orgPassword;
        account=User.account;
    }

}
class Register{
    int option=4;
    String username;
    String password;
    Register(String username,String password){
        this.username=username;
        this.password=password;
    }
}

class User{
    public static JsonObject userInfo;
    public static int account;
    public static String password;
    public static Bitmap headBitmap;

}

 class PersonInfo
{
    public byte[] buffer;

//    public PersonInfo(byte[] buffer, PersonInfoAsString personInfoAsString)
//    {
//        this.buffer = buffer;
//        this.personInfoAsString = personInfoAsString;
//    }
    public PersonInfoAsString personInfoAsString;



}

class UpdateInfo{
    private int option=5;
    private int account;
    private String name;
    private String sex;
    private String subscription;
    private int uid;
    public int headLength;
    public UpdateInfo(int account,String name,String sex,String subscription,int uid){
        this.account=account;
        this.name=name;
        this.sex=sex;
        this.subscription=subscription;
        this.uid=uid;
    }
}



class PersonInfoAsString
{
    public int uid ;
    public String name ;
    public String sex ;
    public String space ;
    public String subscription;

    public PersonInfoAsString(int uid, String name, String sex, String subscription, String space)
    {
        this.uid = uid;
        this.name = name;
        this.sex = sex;
        this.subscription = subscription;
        this.space = space;
    }


}
class  flash
{
    private int option=6;
    private int index=0;
    flash(int index){
        this.index=index;
    }
}

class note
{
    Bitmap head;//头像
    String time;//名称
    String title;//标题
    String subscription;//描述
    Bitmap image;//图片
}
class noteList
{
    public static List<note> notes=null;
}
class mainInfo
{
 public  List<Integer> lengths=new ArrayList<Integer>();
 public  List<Integer> headlens=new ArrayList<Integer>();
 public  List<noter> notes = new ArrayList<noter>();

}
//-----------------------缓存在这里了
 class  cacheInfo{

    public static List<Integer> lengths;
    public static List<noter> notes=new ArrayList<>();
    public static boolean finished=false;
    //public static List<byte[]> images=new ArrayList<byte[]>();
}
class cacheInfo2{
    public static List<Integer> lengths;
    public static List<noter> notes;
    public static boolean finished=false;
}
 class noter{
    public String subscription;
    public String author;
    public String noteName;
    public String space;
    public String time;
    public byte[] imageByte;
    public byte[] headByte;

     public String getSubscription() {
         return subscription;
     }

     public String getAuthor() {
         return author;
     }

     public String getNoteName() {
         return noteName;
     }

     public String getSpace() {
         return space;
     }

     public String getTime() {
         return time;
     }

     public byte[] getImageByte() {
         return imageByte;
     }

     public byte[] getHeadByte() {
         return headByte;
     }

     public void setSubscription(String subscription) {
         this.subscription = subscription;
     }

     public void setAuthor(String author) {
         this.author = author;
     }

     public void setNoteName(String noteName) {
         this.noteName = noteName;
     }

     public void setSpace(String space) {
         this.space = space;
     }

     public void setTime(String time) {
         this.time = time;
     }

     public void setImageByte(byte[] imageByte) {
         this.imageByte = imageByte;
     }

     public void setHeadByte(byte[] headByte) {
         this.headByte = headByte;
     }
 }
 class addInfo{
    public String title;
    public String subscription;
    public int length;
    public int uid;
    private int option=8;
    addInfo(String title,String subscription ,int length){
        uid=User.userInfo.get("uid").getAsInt();
        this.length=length;
        this.title=title;
        this.subscription=subscription;
    }
 }
class  PhotoSystem{
    static byte[] getImageByte(Uri uri, Context context)
    {
        // uri.get
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bitmap bitmap=imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}



