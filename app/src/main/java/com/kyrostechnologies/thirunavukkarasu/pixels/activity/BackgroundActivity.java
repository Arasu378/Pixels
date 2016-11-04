package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.KeyPixabay;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BackgroundActivity extends AppCompatActivity {
    private NotificationManager manager;
    private Notification mynotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        GetPictures();
    }
    private void GetPictures() {
        final String key = KeyPixabay.getKey();

        String url="https://pixabay.com/api/?key=" + key+"&image_type=photo&pretty=true&page=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pixabay",response.toString());
                try{
                    PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),0,new Intent(),0);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext());
                    builder.setAutoCancel(false);
                    builder.setContentTitle("Your vechile is...");
                    builder.setContentText("Rejected");
                    builder.setSmallIcon(android.R.drawable.stat_notify_more);
                    builder.setContentIntent(pendingIntent);
                    mynotification = builder.getNotification();
                    manager.notify(11, mynotification);
                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null){
                    Log.e("error_pixabay",error.toString());
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("KEY", key);
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
