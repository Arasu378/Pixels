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
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ScrollListenerVIdeo;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeMe extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView meme_recyclerview;
    private SwipeRefreshLayout swipe_meme;
    private MeMeAdapter adapter;
    private List<MeMeClass>memelist=new ArrayList<MeMeClass>();
    private int nextpage=1;
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
        GetMeMe(nextpage);
         adapter=new MeMeAdapter(MeMe.this,memelist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        meme_recyclerview.setLayoutManager(layoutManager);
        meme_recyclerview.setItemAnimator(new DefaultItemAnimator());
        meme_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipe_meme.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                GetMeMe(nextpage);
            }
        });
        meme_recyclerview.addOnScrollListener(new ScrollListenerVIdeo(layoutManager){
            @Override
            public void onLoadMore(int current_page) {
                GetMeMe(current_page);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void GetMeMe(int current_page) {
         //   String url="https://api.imgflip.com/get_memes";
     // String url="https://api.imgur.com/3/gallery/hot/viral/0.json";
        String url="http://version1.api.memegenerator.net/Instances_Select_ByPopular?languageCode=en&pageIndex="+current_page+"&pageSize=15";
      //  String url="https://api.imgur.com/3/g/memes/viral";
        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("meme_imgflip",response.toString());
             try{
                    boolean success=response.getBoolean("success");
                    if(success){
                        JSONArray result=response.getJSONArray("result");
                        for(int k=0; k<result.length();k++){
                            JSONObject first=result.getJSONObject(k);
                            String generatorID=first.getString("generatorID");
                            String displayName=first.getString("displayName");
                            String totalVotesScore=first.getString("totalVotesScore");
                            String instanceID=first.getString("instanceID");
                            String instanceImageUrl=first.getString("instanceImageUrl");
                            MeMeClass me=new MeMeClass();
                            me.setDisplayName(displayName);
                            me.setGeneratorID(generatorID);
                            me.setInstanceID(instanceID);
                            me.setTotalVotesScore(totalVotesScore);
                            me.setInstanceImageUrl(instanceImageUrl);
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
                    progressBarHandler.hide();
                    swipe_meme.setRefreshing(false);


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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json");
                params.put("Authorization: Client-ID", "cbd37ce1320375c");
                return params;
            }


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
