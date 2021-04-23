package com.example.shukansilver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.shukansilver.MetaData.MContext;

public class commentAdapter extends ArrayAdapter<Comment> {
    public commentAdapter(@NonNull Context context, ArrayList<Comment> androidFlavors , int resource) {
        super(context, 0, (List<Comment>) androidFlavors);

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.notice_item, parent, false);
        }
        final Comment currentWord = getItem(position);
        final TextView name = listItemView.findViewById(R.id.cname);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetaData.pid=currentWord.getId();
                Toast.makeText(MContext,"OPENED PROFILE",Toast.LENGTH_SHORT).show();
                try {
                    getContext().startActivity(new Intent(getContext(),profile.class));
                }
                catch (Exception e){

                }

            }
        });
        name.setText(currentWord.getName());
        final TextView comment = (TextView) listItemView.findViewById(R.id.comment);
        String s=currentWord.getComment();

        comment.setText(s);
       listItemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               if(currentWord.getName().equals(MetaData.name))
               FirebaseDatabase.getInstance().getReference().child("family").child("Comment").child(currentWord.getDate()).removeValue();
               return false;
           }
       });
        return listItemView;
    }

}
