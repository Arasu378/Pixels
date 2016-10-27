package com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Thirunavukkarasu on 25-10-2016.
 */

public class ServiceHandler extends Application {
    private static final String TAG=ServiceHandler.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static ServiceHandler mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static synchronized ServiceHandler getInstance(){return mInstance;}
    private RequestQueue getRequestQueue(){
        if(mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
