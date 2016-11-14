package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 14-11-2016.
 */

public class AnimeEpisodeTitile {
    private String title=null;
    private String playurl=null;
    private String episodeNo=null;

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static AnimeEpisodeTitile holder=new AnimeEpisodeTitile();
    public static AnimeEpisodeTitile getHolder(){
        return  holder;
    }
}
