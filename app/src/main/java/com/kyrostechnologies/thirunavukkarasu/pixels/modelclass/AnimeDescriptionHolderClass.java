package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 11-11-2016.
 */

public class AnimeDescriptionHolderClass {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static AnimeDescriptionHolderClass holderClass=new AnimeDescriptionHolderClass();
    public static AnimeDescriptionHolderClass getHolderClass(){
        return holderClass;
    }
}
