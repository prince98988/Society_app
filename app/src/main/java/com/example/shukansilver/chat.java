package com.example.shukansilver;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
public class chat extends AppCompatActivity {
    DatabaseReference ref;
    Query ref1;
    EditText chatT;
    Button chatOk;
    RecyclerView r_View;
    private Menu MENU;

    FirebaseRecyclerAdapter adapter1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatT=findViewById(R.id.chatT);
        chatOk=findViewById(R.id.chatOk);
        r_View=findViewById(R.id.rView);
        ref=FirebaseDatabase.getInstance().getReference().child("family").child("chat");
        getData();
        // chatOk is button to set chat
        chatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat=chatT.getText().toString();
                if(chat.isEmpty()){
                    Toast.makeText(chat.this,"Chat is empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    // set key as date && value as name,id,chat,date
                    chatT.setText("");
                    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM HH:mm:ss");
                    String date=sdf.format(new Date());   //get current date
                    ref.child(date).child("name").setValue(MetaData.name);
                    ref.child(date).child("id").setValue(MetaData.id);
                    ref.child(date).child("chat").setValue(chat);
                    ref.child(date).child("date").setValue(date);

                }
            }
        });
    }
    //getData function is for getting chat messages from database
    public void getData(){
        ref1= FirebaseDatabase.getInstance().getReference().child("family").child("chat").orderByKey();// query for database
        FirebaseRecyclerOptions<chat_detail> options=new FirebaseRecyclerOptions.Builder<chat_detail>()
                .setQuery(ref1,chat_detail.class)
                .build();   // set query and add chat_detail as class for storing values.
        adapter1= new FirebaseRecyclerAdapter<chat_detail, ChatViewHolder>(options) {
            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false); //chat_item is layout for chat messages
                return new ChatViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull final chat_detail model) {
                // if chat by user then set to right layout ,else left layout
                if(model.getName().equals(MetaData.name)){
                    holder.uchat.setText(model.getChat());
                    holder.udate.setText(model.getDate());
                    holder.uchat.setPadding(15,10,15,10);
                    holder.uname.setText(model.getName());
                    holder.uname.setPadding(15,10,15,10);
                }
                else {
                    holder.chat.setText(model.getChat());
                    holder.date.setText(model.getDate());
                    holder.chat.setPadding(15,10,15,10);
                    holder.name.setText(model.getName());
                    holder.name.setPadding(15,10,15,10);
                }
                // on click onLongClick on chat messages
                holder.l2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                          if(MetaData.isshown==0) {  //MetaData.isshown  is flag that show that delete menu is already displayed or not
                              MetaData.isshown=1;
                              MenuInflater i = getMenuInflater();
                              i.inflate(R.menu.delete_item, MENU);  // set delete Menu R.menu.delete_item
                          }
                        MetaData.chat_ref.add(FirebaseDatabase.getInstance().getReference().child("family").child("chat").child(model.getDate()));// add delete ref to MetaData.chat_ref
                        if(model.getName().equals(MetaData.name)) {
                           holder.uchat.setTextColor(R.color.prince1);  // change chat color to chat messages
                        }
                        return true;
                    }
                });
                // on click on other user name open profile of user
               holder.name.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       MetaData.pid=model.getId();
                       Toast.makeText(chat.this,"OPENED PROFILE",Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(chat.this,profile.class));
                   }
               });
               //on click on user itself name open his/her profile
                holder.uname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MetaData.pid=model.getId();
                        Toast.makeText(chat.this,"OPENED PROFILE",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(chat.this,profile.class));
                    }
                });

            }
        };

        r_View.setAdapter(adapter1);//add adapter to recycleView r_View
        final int lastItemPosition = adapter1.getItemCount() ;
        r_View.scrollToPosition(lastItemPosition);
        r_View.setHasFixedSize(true);

    }
    //ChatViewHolder is for binding layout variable
    private class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView name,date,chat,uname,udate,uchat;
        private LinearLayout l1,l2;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.cname);
            date=itemView.findViewById(R.id.cdate);
            chat=itemView.findViewById(R.id.chat);
            uname=itemView.findViewById(R.id.cuname);
            udate=itemView.findViewById(R.id.cudate);
            uchat=itemView.findViewById(R.id.uchat);
            l1=itemView.findViewById(R.id.others);
            l2=itemView.findViewById(R.id.user);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter1.stopListening();
        // onStop of activity stoplistening
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter1.startListening();
        // onStart of activity stoplistening
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MENU=menu;  // get reference to menu
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_i) {
                for(DatabaseReference i:MetaData.chat_ref){
                    i.removeValue();
                }
            MetaData.isshown=0;
                MENU.clear();
            MetaData.chat_ref.clear();
            return true;
        }
        // onclick cancel
        if(item.getItemId()==R.id.cancel){
            MetaData.isshown=0;  // set isshown flag as 0
            finish();            // finished this activity
            startActivity(getIntent());  // restart activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}