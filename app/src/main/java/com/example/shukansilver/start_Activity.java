package com.example.shukansilver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class start_Activity extends AppCompatActivity {
     private Button reg;
     private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        reg=findViewById(R.id.reg);
        login=findViewById(R.id.log);
        reg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(start_Activity.this, "NOT FOR YOU", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(start_Activity.this,register.class));

            }
        });
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(start_Activity.this,login.class));

            }
        });

    }
    protected void onStart() {
        super.onStart();

            if ((FirebaseAuth.getInstance().getCurrentUser() != null) ) {  // if user is already login then directly open main activity
                startActivity(new Intent(start_Activity.this, MainActivity.class));
                finish();
            }

    }
}