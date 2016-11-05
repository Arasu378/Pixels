package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 04-11-2016.
 */

public class MangaIdClass {
    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public static MangaIdClass holder=new MangaIdClass();
    public static MangaIdClass getHolder(){
        return holder;
    }
}
