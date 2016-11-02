package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.MeMeAdapter;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MeMeClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeMe extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView meme_recyclerview;
    private SwipeRefreshLayout swipe_meme;
    private MeMeAdapter adapter;
    private List<MeMeClass>memelist=new ArrayList<MeMeClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        setContentView(R.layout.activity_me_me);
        meme_recyclerview=(RecyclerView)findViewById(R.id.meme_recyclerview);
        swipe_meme=(SwipeRefreshLayout)findViewById(R.id.swipe_meme);
        GetMeMe();
         adapter=new MeMeAdapter(MeMe.this,memelist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        meme_recyclerview.setLayoutManager(layoutManager);
        meme_recyclerview.setItemAnimator(new DefaultItemAnimator());
        meme_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipe_meme.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                GetMeMe();

            }
        });
    }

    private void GetMeMe() {
            String url="https://api.imgflip.com/get_memes";
        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("meme_imgflip",response.toString());
                try{
                    boolean success=response.getBoolean("success");
                    if(success){
                        JSONObject data=response.getJSONObject("data");
                        JSONArray memes=data.getJSONArray("memes");
                        for(int i=0; i<=memes.length();i++){
                            JSONObject first=memes.getJSONObject(i);
                            String id=first.getString("id");
                            String name=first.getString("name");
                            String url=first.getString("url");
                            String width=first.getString("width");
                            String height=first.getString("height");
                            MeMeClass me=new MeMeClass();
                            me.setId(id);
                            me.setName(name);
                            me.setUrl(url);
                            me.setWidth(width);
                            me.setHeight(height);
                            memelist.add(me);
                        }
                        adapter.notifyDataSetChanged();

                    }else{
                        final AlertDialog alertDialog= new AlertDialog.Builder(MeMe.this).create();
                        alertDialog.setTitle("No MeMe");
                        alertDialog.setMessage("Sorry No more MeMe....");
                        alertDialog.setButton("Ok",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarHandler.hide();
                serverErrorDialog.showServerErrorDialog();
                if(error!=null){
                    Log.e("error_imgflip",error.toString());
                }

            }
        }) {


        };
        ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                MeMe.this.finish();
                Intent i= new Intent(MeMe.this, MainActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
