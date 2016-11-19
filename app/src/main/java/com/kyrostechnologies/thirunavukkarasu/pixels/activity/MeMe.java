package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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
    private String searchedquery=null;

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
        GetMeMe(null,nextpage);
         adapter=new MeMeAdapter(MeMe.this,memelist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        meme_recyclerview.setLayoutManager(layoutManager);
        meme_recyclerview.setItemAnimator(new DefaultItemAnimator());
        meme_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipe_meme.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                GetMeMe(null,nextpage);
            }
        });
        meme_recyclerview.addOnScrollListener(new ScrollListenerVIdeo(layoutManager){
            @Override
            public void onLoadMore(int current_page) {
                GetMeMe(null,current_page);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String i=query.replace(" ","+");
                memelist.clear();
                searchedquery=i;
                GetMeMe(i,nextpage);

                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);    }

    private void GetMeMe(String query,int current_page) {

        String url="http://version1.api.memegenerator.net/Instances_Select_ByPopular?languageCode=en&pageIndex="+current_page+"&pageSize=15";
        progressBarHandler.show();
        String i="http://version1.api.memegenerator.net/Instances_Select_ByNew?languageCode=en&pageIndex="+current_page+"&pageSize=15&urlName="+query;
        if(query!=null){
                url=i;
        }
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

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
