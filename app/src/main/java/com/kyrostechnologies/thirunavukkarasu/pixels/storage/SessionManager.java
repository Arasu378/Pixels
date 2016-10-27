package com.kyrostechnologies.thirunavukkarasu.pixels.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kyrostechnologies.thirunavukkarasu.pixels.activity.Login_Activity;

/**
 * Created by Thirunavukkarasu on 25-10-2016.
 */

public class SessionManager {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private static final String FILE_NAME="Session_Details";
    private static final String IS_LOGIN="IsLoggedIn";
    private static final String EMAIL="email";
    private static final String USERID="userid";
    public SessionManager(Context mContext){
        this.mContext=mContext;
        sp=mContext.getSharedPreferences(FILE_NAME,3);
        editor=sp.edit();
    }
    public void createLoginSession(String email,String userid){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(EMAIL,email);
        editor.putString(USERID,userid);
        editor.commit();
    }
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i= new Intent(mContext, Login_Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(i);
        }
    }
    private boolean isLoggedIn(){
        return sp.getBoolean(IS_LOGIN,false);
    }
    public void logoutUser(){
        editor.remove(EMAIL);
        editor.remove(USERID);
        editor.putBoolean(IS_LOGIN,false);
        editor.commit();
        Intent i= new Intent(mContext, Login_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}
