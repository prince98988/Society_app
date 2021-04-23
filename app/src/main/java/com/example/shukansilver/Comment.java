package com.example.shukansilver;
// this class is for storing comment and notice detail
public class Comment {
    private String name;
    private String comment;
    private String date;
    private String id;
    Comment(String n,String c,String d,String i){
        name=n;
        comment=c;
        date=d;
        id=i;
    }
    Comment(String n,String c,String d){
        name=n;
        comment=c;
        date=d;
    }
    Comment(String n,String c){
        name=n;
        comment=c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String getName(){
        return name;
    }
    String getDate(){
        return date;
    }
    String getComment() {return comment;}
    void setName(String n){
        name=n;
    }
    void setDate(String d){
        date=d;
    }
    void setComment(String c){
        comment=c;
    }

}
