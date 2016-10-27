package com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;

/**
 * Created by Thirunavukkarasu on 25-10-2016.
 */

public class ServerErrorDialog {
    private Context mContext;
    public ServerErrorDialog(Context mContext){
        this.mContext=mContext;
    }
    public void showServerErrorDialog(){
        final AlertDialog alertDialog= new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Network/Connection Error");
        alertDialog.setMessage(mContext.getString(R.string.server_error_dialog));
        alertDialog.setButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
}
