package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 07-11-2016.
 */

public class MangaChapterID {
    private String MangaChapterId;

    public String getMangaChapterId() {
        return MangaChapterId;
    }

    public void setMangaChapterId(String mangaChapterId) {
        MangaChapterId = mangaChapterId;
    }

    public  static MangaChapterID holder=new MangaChapterID();
    public static MangaChapterID getHolder(){
        return holder;
    }
}
