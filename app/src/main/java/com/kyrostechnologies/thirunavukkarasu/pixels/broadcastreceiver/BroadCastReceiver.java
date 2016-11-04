package com.kyrostechnologies.thirunavukkarasu.pixels.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kyrostechnologies.thirunavukkarasu.pixels.activity.BackgroundActivity;

/**
 * Created by Thirunavukkarasu on 03-11-2016.
 */

public class BroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Start your intent service
            Intent i= new Intent(context, BackgroundActivity.class);
            context.startActivity(i);
        }
    }
}
