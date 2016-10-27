package com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Thirunavukkarasu on 25-10-2016.
 */

public class CheckOnline {
    private Context mContext;
    public CheckOnline(Context mContext){
        this.mContext=mContext;
        if(!checkOnline()){
            onlineDialog();
        }

    }

    public boolean checkOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;

        }else {
            onlineDialog();

        }

        return false;
    }

    private void onlineDialog() {
        AlertDialog alertDialog= new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("We cannot detect any internet connectivity.Please check your internet connection and try again");
        alertDialog.setButton("Try Again",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                checkOnline();
            }
        });
        alertDialog.show();
    }
}
