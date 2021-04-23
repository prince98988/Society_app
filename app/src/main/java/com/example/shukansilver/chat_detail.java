package com.example.shukansilver;

public class chat_detail {
    public String chat;
    public String date;
    public String name;
    public String id;

    public chat_detail(){}
    public chat_detail(String chat,String date,String id,String name){
        this.id=id;
        this.name=name;
        this.date=date;
        this.chat=chat;
    }

    String getName(){
        return name;
    }
    String getDate(){
        return date;
    }
    String getChat(){
        return chat;
    }
    String getId(){
        return id;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
