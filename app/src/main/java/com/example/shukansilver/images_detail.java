package com.example.shukansilver;

import android.net.Uri;

public class images_detail {
    public String uri;
    public String date;
    public String id;
    public String name;
    public String ref;
    public images_detail(){};
    public images_detail(String date,String id,String name,String uri,String ref){
        this.uri=uri;
        this.date=date;
        this.id=id;
        this.name=name;
        this.ref=ref;
    }
    public String getUri(){
        return uri;
    }
    public String getDate(){
        return date;
    }

    public String getRef() {
        return ref;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}

