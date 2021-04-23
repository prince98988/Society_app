package com.example.shukansilver;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashMap;


public class MetaData {
    static Context MContext=null;
    static FirebaseUser user;
    static Bitmap bitmap=null;
    static String pid="";
    static int isshown=0;  // it is for chat activity to show delete menu is already displayed or not
    static ArrayList<DatabaseReference> chat_ref=new ArrayList<>();  // to store more than one selected chat for deletion
    static ArrayList<DatabaseReference> comment_ref=new ArrayList<>();
    static CollectionReference ref=null;  // for storing images for referencing to current folder of images
    static CollectionReference buff_ref=null;
    static String uri="";
    static String puri="";
    static String date="";
    static String email= FirebaseAuth.getInstance().getCurrentUser().getEmail(); // get cuurent user email
    static String name=email.substring(0,email.indexOf("@")); // get current user name
    static String id=FirebaseAuth.getInstance().getCurrentUser().getUid();  //store current user id

}
