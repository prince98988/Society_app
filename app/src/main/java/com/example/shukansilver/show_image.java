package com.example.shukansilver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class show_image extends AppCompatActivity {
  ImageView image;
  Button download;
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        image=findViewById(R.id.simage);
        download=findViewById(R.id.sdownload);
        if(MetaData.bitmap==null)  //
        Picasso.get().load(MetaData.uri).into(image); // load image in layout
        else{
            image.setImageBitmap(MetaData.bitmap);
        }
        // Download is button for downloading image
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(show_image.this, "Downloading started Just wait for message", Toast.LENGTH_LONG).show();
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    Toast.makeText(show_image.this, "YOUR DEVICE NOT SUPPORT ", Toast.LENGTH_SHORT).show();
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){  //if storage permission is denied than is request for storage writing
                        String[] permission ={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,WRITE_EXTERNAL_STORAGE_CODE);
                    }
                    else{
                        Toast.makeText(show_image.this, "JUST WAITING", Toast.LENGTH_SHORT).show();
                        new Loadimage(image).doInBackground(MetaData.uri); // passing uri of image to asyncTask
                    }
                }
            }
        });

    }
    private class Loadimage extends AsyncTask<String,Void,Bitmap>{
        ImageView image;
         Loadimage(ImageView image){
             this.image=image;
         }
        @Override
        protected Bitmap doInBackground(String... strings) {  //background process
             /*
            Bitmap bitmap=MetaData.bitmap;
            MetaData.bitmap=null;
            File path= Environment.getExternalStorageDirectory();
            File dir=new File(path+"/Download");
            dir.mkdirs();
            String imagename=MetaData.date+".png";
            File file=new File(dir,imagename);
            OutputStream out;
            try{
                out=new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                out.flush();
                out.close();
                Toast.makeText(show_image.this, "DOWNLOAD COMPLETED", Toast.LENGTH_SHORT).show();

            }catch(Exception e){
                Toast.makeText(show_image.this, "PROBLEM IN DOWNLOAD", Toast.LENGTH_SHORT).show();
            }

            return bitmap;
            */
            DownloadManager.Request request=new DownloadManager.Request(Uri.parse(strings[0]));  //DownloadManager.Request for setting up downloading image from link
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
            request.setTitle("Download");    // Set title for notification
            request.setDescription("Downloading....");  //Set message
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //set notification visibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {   //DownloadManager is only work in android version greater then or equal to kitkat
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+MetaData.date); // set path for to save download
            }
            DownloadManager manager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE) ;
            manager.enqueue(request);  // set request
            Bitmap bitmap=MetaData.bitmap;  //
            return bitmap;
        }
    }
}