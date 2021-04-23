package com.example.shukansilver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaSync;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
    private Button log;
    private EditText email,pass;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log=findViewById(R.id.log);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        auth=FirebaseAuth.getInstance();
        log.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_pass=pass.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){  //check emailis not empty and password is not empty
                    Toast.makeText(login.this,"Details is Empty",Toast.LENGTH_SHORT).show();

                }
                else if(txt_pass.length()<6){ // password is less then 6
                    Toast.makeText(login.this,"Password Length is less than 6",Toast.LENGTH_SHORT).show();
                }
                else{
                    login_user(txt_email,txt_pass);
                }

            }
        });
    }
  // validating email and password
    private void login_user(String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(login.this,"Log In Successfull ",Toast.LENGTH_SHORT).show();
                MetaData.email=FirebaseAuth.getInstance().getCurrentUser().getEmail();  //set MetaData.email
                MetaData.id=FirebaseAuth.getInstance().getCurrentUser().getUid();//set MetaData.id
                MetaData.user=FirebaseAuth.getInstance().getCurrentUser();//set MetaData.user
                startActivity(new Intent(login.this,MainActivity.class));
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login.this,"Something is wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}