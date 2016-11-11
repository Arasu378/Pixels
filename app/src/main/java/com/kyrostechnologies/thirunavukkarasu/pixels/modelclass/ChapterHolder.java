package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 08-11-2016.
 */

public class ChapterHolder {
    public String ChapterHolder;
    private String MangaTitle;
    public static ChapterHolder holder=new ChapterHolder();
    public static ChapterHolder getHolder(){
        return holder;
    }
    public ChapterHolder(){

    }

    public String getMangaTitle() {
        return MangaTitle;
    }

    public void setMangaTitle(String mangaTitle) {
        MangaTitle = mangaTitle;
    }
}
