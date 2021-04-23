package com.example.shukansilver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private Button reg;
    private EditText email,pass;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        reg=findViewById(R.id.reg);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        reg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_pass=pass.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(register.this,"Details is Empty",Toast.LENGTH_SHORT).show();

                }
                else if(txt_pass.length()<6){
                    Toast.makeText(register.this,"Password Length is less than 6",Toast.LENGTH_SHORT).show();
                }
                else{
                    register_user(txt_email,txt_pass);
                }

            }
        });
    }

    private void register_user(String email, String pass) {

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(register.this,"Registering User Successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(register.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(register.this,"Registration Failed",Toast.LENGTH_SHORT).show();

                }
            }
        });



    }

}