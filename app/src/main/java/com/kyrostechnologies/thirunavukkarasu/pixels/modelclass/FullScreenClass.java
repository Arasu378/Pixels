package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 27-10-2016.
 */

public class FullScreenClass {
    private String id;
    private String tag;
    private String picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public static final FullScreenClass holder=new FullScreenClass();
    public static FullScreenClass getHolder(){
        return holder;
    }
}
