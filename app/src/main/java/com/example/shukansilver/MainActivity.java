package com.example.shukansilver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

     private Button add ,delete,ok,view,cancel;
     private EditText notice,reply;
     private ListView comments,notice_lw;
     static String name="";
     static String temp="";
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("family").child("Board");// reference to notice board folder
    final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("family").child("Comment"); //reference to comment folder
    ArrayList<Comment> comment_array;  // to store comments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add=findViewById(R.id.add);
        view=findViewById(R.id.show_WB);
        delete=findViewById(R.id.delete);
        cancel=findViewById(R.id.cancel);
        ok=findViewById(R.id.ok);
        notice=findViewById(R.id.notice);
        notice_lw=findViewById(R.id.noitce_lw);
        comments=findViewById(R.id.comments);
        MetaData.MContext=MainActivity.this;
        comment_array=new ArrayList<>();
        name=MetaData.name;
        reply=findViewById(R.id.reply);
        getData(ref,notice_lw);
        getData1(ref1,comments);

        cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               notice.setText("");
               getData(ref,notice_lw);
               LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) notice.getLayoutParams();
               params.height = 0;
               notice.setLayoutParams(params);
               LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) add.getLayoutParams();
               params1.height = 0;
               add.setLayoutParams(params1);
               LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) cancel.getLayoutParams();
               params2.height = 0;
               cancel.setLayoutParams(params2);
           }
       });
        // ok is button for comment set
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                String txt_reply = reply.getText().toString();
                if (TextUtils.isEmpty(txt_reply)) {
                    Toast.makeText(MainActivity.this, "Please Fill Value", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  // getting date in dd-MM-yy HH:mm:ss format
                        String date=sdf.format(new Date());      // get current date
                        int k=MetaData.email.indexOf("@");     // get index of @
                        // set value as name***comment  and key as date
                        FirebaseDatabase.getInstance().getReference().child("family").child("Comment").child(date).setValue(MetaData.email.substring(0,k)+"***"+MetaData.id+"|||"+txt_reply);
                        Toast.makeText(MainActivity.this, "comment set", Toast.LENGTH_SHORT).show();
                    } catch (Exception r) {
                        Toast.makeText(MainActivity.this, "Problem in Database", Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        reply.setText("");
                    }
                }
                }
        });
        // add button as adding notice
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String txt_notice = notice.getText().toString();
                if (TextUtils.isEmpty(txt_notice)) {
                    Toast.makeText(MainActivity.this, "Please Fill Notice", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int k=MetaData.email.indexOf("@");
                        // set value as name***notice and key as user id
                        FirebaseDatabase.getInstance().getReference().child("family").child("Board").child(MetaData.id).setValue(MetaData.email.substring(0,k)+"***"+txt_notice);
                        Toast.makeText(MainActivity.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
                    } catch (Exception r) {
                        Toast.makeText(MainActivity.this, "ERROR IN DATABASE", Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        notice.setText("");
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) notice.getLayoutParams();
                        params.height = 0;
                        notice.setLayoutParams(params);
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) add.getLayoutParams();
                        params1.height = 0;
                        add.setLayoutParams(params1);
                        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) cancel.getLayoutParams();
                        params2.height = 0;
                        cancel.setLayoutParams(params2);
                    }

                }
            }
        });
    }
    //getData1 function is for getting comments from database
    private void getData1(final DatabaseReference ref1, final ListView comments) {
        ref1.orderByKey().addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_array.clear();
                for(DataSnapshot s: dataSnapshot.getChildren()){
                    String s1=s.getValue().toString();
                    int k=s1.indexOf("|||");
                    int k1=s1.indexOf("***");
                    String c_id=s1.substring(k1+3,k);
                    String nn=s1.substring(0,k1);
                    comment_array.add(new Comment(nn,s1.substring(k+3,s1.length()),s.getKey(),c_id));
                }
             comments.setAdapter(new commentAdapter(MainActivity.this,comment_array,R.color.colorAccent));
             comments.setSelection(comments.getAdapter().getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });

    }
    //getData function is for getting notice from data base
    public void getData(DatabaseReference ref, final ListView not){

        ref.orderByKey().addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Comment> a1=new ArrayList<>();  // to store notice

                for(DataSnapshot s: dataSnapshot.getChildren()){
                    String s1=s.getValue().toString();
                    String c=s.getKey();
                    int k1=s1.indexOf("***");  // separating name and notice
                    String k=s1.substring(0,k1);
                    a1.add(new Comment(k,s1.substring(k1+3)));
                }
                not.setAdapter(new noticeAdapter(MainActivity.this,a1,R.color.colorAccent)); // add this data to adapter
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i=getMenuInflater();
        i.inflate(R.menu.menu,menu); // set menu as R.menu.menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.signout) {
            FirebaseAuth.getInstance().signOut();    // sighing out from firebase
            Toast.makeText(MainActivity.this,"Sign Out Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,start_Activity.class));  // start_Activity.class for start page of app
            return true;
        }
        else if (item.getItemId()==R.id.show_WB) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) notice.getLayoutParams();  //notice board layout
            params.height = 150; //set height equal to 150
            notice.setLayoutParams(params);
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) add.getLayoutParams();  // add notice button
            params1.height = 75;  //set height equal to 75
            add.setLayoutParams(params1);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) cancel.getLayoutParams(); //cancel white board
            params2.height = 75;    //set height equal to 75
            cancel.setLayoutParams(params2);
        }
        else if (item.getItemId()==R.id.delete) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            try {
                ref.child("family").child("Board").child(MetaData.id).removeValue();    //delete user notice from notice board
            }
            catch (Exception e){
                Toast.makeText(MainActivity.this, "Error In DataBase", Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.chat){
            Toast.makeText(MainActivity.this,"CHAT IS OPENED",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,chat.class));  //chat.class is activity for group chat
            return true;
        }
        else if(item.getItemId()==R.id.images) {
            Toast.makeText(MainActivity.this,"Family Images Opened",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(MainActivity.this,images.class);            //images.class is activity for group image
            CollectionReference reference= FirebaseFirestore.getInstance().collection("images");
            MetaData.ref=reference;
            startActivity(i);
            return true;
        }
        //for user profile
        else if (item.getItemId()==R.id.proile) {
            MetaData.pid=MetaData.id;
            Toast.makeText(MainActivity.this,"OPENED PROFILE",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,profile.class)); //profile.class for showing interface to user profile
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // name function for getting name from user id
    private void name(String id1){

        DatabaseReference  ref=FirebaseDatabase.getInstance().getReference().child("family").child("profile").child(id1);
        ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s: dataSnapshot.getChildren()){
                    name=s.getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}