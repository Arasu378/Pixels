package com.kyrostechnologies.thirunavukkarasu.pixels.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Thirunavukkarasu on 25-10-2016.
 */

public class Storage {
    private static Storage storage;
    private  SharedPreferences sp;
    private Storage(Context mContext){
        String filename="UserData";
        sp=mContext.getApplicationContext().getSharedPreferences(filename,0);
    }
    public static Storage getInstance(Context mContext){
        if(storage==null){
            storage=new Storage(mContext);
        }
        return storage;
    }
    public String getUserId(){
        return sp.getString("UserId",null);
    }
    public void putUserId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("UserId",value);
        editor.commit();
    }
    public String getEmailId(){
        return sp.getString("EmailId",null);
    }
    public void putEmailId(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("EmailId",value);
        editor.commit();
    }
    public String getDisplayName(){
        return sp.getString("DisplayName",null);
    }
    public void putDisplayName(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("DisplayName",value);
        editor.commit();
    }public String getUserPicture(){
        return sp.getString("UserPicture",null);
    }
    public void putUserPicture(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("UserPicture",value);
        editor.commit();
    }public String getChapterList(){
        return sp.getString("ChapterList",null);
    }
    public void putChapterList(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("ChapterList",value);
        editor.commit();
    }
    public String getAnimeListWAtch(){
        return sp.getString("AnimeListWAtch",null);
    }
    public void putAnimeListWAtch(String value){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.putString("AnimeListWAtch",value);
        editor.commit();
    }
    public void clear(){
        SharedPreferences.Editor editor;
        editor=sp.edit();
        editor.clear();
        editor.commit();
    }
}
