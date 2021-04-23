package com.example.shukansilver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class noticeAdapter extends ArrayAdapter<Comment> {
    public noticeAdapter(@NonNull Context context, ArrayList<Comment> androidFlavors , int resource) {
        super(context, 0, (List<Comment>) androidFlavors);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }
        Comment currentWord = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);//name of user
        name.setText(currentWord.getName());

        TextView comment = (TextView) listItemView.findViewById(R.id.notice);  //notice
        String s=currentWord.getComment();
        comment.setText(s);
        return listItemView;
    }
}
