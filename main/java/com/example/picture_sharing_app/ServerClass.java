package com.example.picture_sharing_app;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ServerClass {


}
class Server{
    public final static String host="192.168.168.164";
    public final static int post=4040;
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
    public UpdateInfo(int account,String name,String sex,String subscription){
        this.account=account;
        this.name=name;
        this.sex=sex;
        this.subscription=subscription;
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

