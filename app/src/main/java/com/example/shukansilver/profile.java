package com.example.shukansilver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class profile extends AppCompatActivity {
    private Uri imageUri;
    private static final int IMAGE_REQUEST=2;
    String id= MetaData.pid,name="";
    ImageView pimage;
    Button psave,pcimage;
    EditText pname;
    @Override
    protected void onStart() {
        super.onStart();

            try {
                photo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pimage=findViewById(R.id.pimage);
        psave=findViewById(R.id.psave);
        pcimage=findViewById(R.id.pcimage);
        pname=findViewById(R.id.pname);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("family").child("profile").child(id); //reference to user

        ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = "";
                for(DataSnapshot s: dataSnapshot.getChildren()){
                    name=s.getValue().toString();
                }
                pname.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
       //psave is button to change name
        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("family").child("profile").child(id).child("name").setValue(pname.getText().toString()); //change name
                MainActivity.name= String.valueOf(pname.getText());
                Toast.makeText(profile.this,"name is updated",Toast.LENGTH_SHORT).show();
            }
        });

        pcimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

    }

    void photo() throws IOException {

        StorageReference ref= FirebaseStorage.getInstance().getReference().child("profile/"+id); //getting reference of user photo from his/her id
        try {
            final File localfile = File.createTempFile("back", "jpg");   // filelocaion for image
            ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(profile.this,"Loading completed",Toast.LENGTH_SHORT).show();
                        MetaData.bitmap=BitmapFactory.decodeFile(localfile.getAbsolutePath()); //change image into bitmap
                        pimage.setImageBitmap(MetaData.bitmap);  // set image

                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(profile.this,"PROBLEM IN DATABASE",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(profile.this,"SOMETHING WENT WRONG IN LOADING IMAGE",Toast.LENGTH_SHORT).show();
        }


    }
    //openImage() is for open images from system
    private void openImage() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,
                "select Image"),
                IMAGE_REQUEST);
    }
    // uploadImage() is for changing user image
    private void uploadImage() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();
        if (imageUri != null){
            final StorageReference ref= FirebaseStorage.getInstance().getReference().child("profile").child(id);// reference to user image
            ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {  // put file into this reference
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            pd.dismiss();
                            Toast.makeText(profile.this,"Image Upload Successful",Toast.LENGTH_SHORT).show();
                            try {
                                photo();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null
                && data.getData() != null){
            imageUri=data.getData();  //get selected data
            uploadImage();
        }
    }
   
}