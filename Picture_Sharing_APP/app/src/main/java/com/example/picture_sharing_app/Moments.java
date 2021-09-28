package com.example.picture_sharing_app;

import android.graphics.Bitmap;

public class Moments {
    private String mTime;
    private String noteName;
    private Bitmap mImage;

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmUsername() {
        return noteName;
    }

    public void setmUsername(String mUsername) {
        this.noteName = mUsername;
    }

    public Bitmap getmImageId() {
        return mImage;
    }

    public void setmImageId(Bitmap mImageId) {
        this.mImage = mImageId;
    }
}
