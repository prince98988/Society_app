package com.example.shukansilver;

public class member {


    private int mImageResourceId= no_image;


    private static final int no_image=-1;


    public member(int mimage) {
        mImageResourceId=  mimage;

    }



    public int getmImageResourceId(){return mImageResourceId;}


    public boolean hasImage(){return mImageResourceId!=no_image;}
}

