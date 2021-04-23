package com.example.shukansilver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class images extends AppCompatActivity {
    private Uri imageUri;
    private Query ref;
    FirebaseFirestore fireref;
    CollectionReference reference;
    private static final int IMAGE_REQUEST = 2;
    private String num = "";
    RecyclerView r_View;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        r_View = findViewById(R.id.r_View);
        fireref = FirebaseFirestore.getInstance();
        reference = MetaData.ref;   // reference to folder of images
        getData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_images, menu); // set menu as R.menu.menu_images
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.photo) {  // new image upload
            openImage();
            return true;
        } else if (item.getItemId() == R.id.refresh) { //refresh the page
            finish();
            startActivity(getIntent()); // restart activity
            return true;
        } else if (item.getItemId() == R.id.folder) { // add new subfolder
            AlertDialog alertDialog = new AlertDialog.Builder(images.this).create(); // create alert dialog for getting new floder details
            alertDialog.setTitle("Folder Name");
            alertDialog.setMessage("Add Folder name");
            final EditText input = new EditText(images.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String folder = input.getText().toString();
                            if (!folder.equals("")) {
                                HashMap<String, Object> m = new HashMap<>();
                                int k = (int) (Math.random() * 1001);
                                m.put("uri", "");
                                m.put("date", "00:0" + k);
                                m.put("id", MetaData.name);
                                m.put("name", folder);
                                reference.document("00:0" + k).set(m);// set detail to reference of this folder
                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,
                "select Image"),
                IMAGE_REQUEST);
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();
        if (imageUri != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final String date = sdf.format(new Date());
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images").child(MetaData.id).child(date);// add image with key as uder id
            ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {  // put image in storage reference
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            Toast.makeText(images.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                            HashMap<String, Object> m = new HashMap<>();
                            m.put("uri", uri.toString());
                            m.put("date", date);
                            m.put("id", MetaData.id);
                            m.put("name", MetaData.name);
                            m.put("ref",ref.getPath()); // storeimage reference path of storage ref.
                            reference.document(date).set(m);
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            imageUri = data.getData();
            uploadImage();
        }
    }
    // getData is function for getting uploaded images
    public void getData() {

        Query query = reference.orderBy("date");// sort by key
        FirestoreRecyclerOptions<images_detail> options = new FirestoreRecyclerOptions.Builder<images_detail>()
                .setQuery(query, images_detail.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<images_detail, ImageViewHolder>(options) {
            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
                return new ImageViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position, @NonNull final images_detail model) {
                if (!model.uri.equals("")) {
                    Picasso.get().load(model.getUri()).into(holder.images); //set image
                    holder.date.setText("On: " + model.getDate());   // set data
                    holder.name.setText("By: " + model.getName());   // set user name
                } else {
                    holder.images.setImageResource(R.drawable.folder); // set folder image
                    holder.name.setText("Folder Name : " + model.getName()); ;//set folder name
                }

                holder.images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.uri.equals("")) { // is clicked view if folder
                            Intent i = new Intent(images.this, images.class);
                            MetaData.ref = FirebaseFirestore.getInstance().collection(model.name);
                            startActivity(i);
                            MetaData.buff_ref=reference;
                        } else { // if image
                            MetaData.date=model.getDate();
                            MetaData.uri = model.getUri();
                            MetaData.bitmap=((BitmapDrawable)holder.images.getDrawable()).getBitmap();  // set bitmap of this image metaData.bitmap, so that in show_image again do not need to download
                            Intent i = new Intent(images.this, show_image.class);  // show_image.class shows image
                            startActivity(i);

                        }
                    }
                });
                //click on delete of image
                holder.idelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getName().equals(MetaData.name)&&(!model.getUri().equals(""))) { // if image upload by current user
                            String ref=model.getRef().substring(1);
                            final String child=ref.substring(0,ref.indexOf("/")); //first child
                            ref=ref.substring(ref.indexOf("/")+1);
                            final String child1=ref.substring(0,ref.indexOf("/")); // second child
                            final String child3=model.getDate();  // third child
                            FirebaseStorage.getInstance().getReference().child(child).child(child1).child(child3).delete().addOnSuccessListener(new OnSuccessListener<Void>() {// delete image
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(images.this, "ITEM DELETED FROM PRIMARY STORAGE", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(images.this, "SOMETHING PROBLEM ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            reference.document(model.getDate()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) { //delete details from firebaseDatabase
                                    Toast.makeText(images.this, "ITEM DELETED FROM SECONDARY STORAGE", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if ((model.getUri().equals("")) && (model.getId().equals(MetaData.name))) {
                            reference.document(model.getDate()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(images.this, "ITEM DELETED", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(images.this, "YOU CAN NOT DELETE THIS ITEM", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        r_View.setAdapter(adapter);

    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView images;
        private TextView name, date;
        private LinearLayout ilinear;
        private Button idelete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ilinear = itemView.findViewById(R.id.ilinear);
            images = itemView.findViewById(R.id.iimage);
            name = itemView.findViewById(R.id.iname);
            date = itemView.findViewById(R.id.idate);
            idelete = itemView.findViewById(R.id.idelete);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        MetaData.ref = MetaData.buff_ref;
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }




}