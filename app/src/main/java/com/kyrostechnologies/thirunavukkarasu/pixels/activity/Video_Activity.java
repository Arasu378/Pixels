package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.AdapterVideo;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.KeyPixabay;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.Video;
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

public class Video_Activity extends AppCompatActivity {
    private RecyclerView recyler_video;
    private AdapterVideo adapter;
    private List<Video>videoList=new ArrayList<Video>();
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        setContentView(R.layout.activity_video_);
        recyler_video=(RecyclerView)findViewById(R.id.recyler_video);
        adapter=new AdapterVideo(Video_Activity.this,videoList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyler_video.setLayoutManager(layoutManager);
        recyler_video.setItemAnimator(new DefaultItemAnimator());
        recyler_video.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                videoList.clear();
                GetVideo(query);

                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);    }

    private void GetVideo(String query) {
        final String key = KeyPixabay.getKey();

        String i = "https://pixabay.com/api/videos/?key=" + key+"&q="+query+"&pretty=true";
        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, i, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pixabay",response.toString());
                try{
                    JSONArray hits=response.getJSONArray("hits");
                    for(int k=0;k<hits.length();k++){
                        JSONObject first=hits.getJSONObject(k);
                        String picture_id=first.getString("picture_id");
                        String tags=first.getString("tags");
                        JSONObject videos=first.getJSONObject("videos");
                        JSONObject small=videos.getJSONObject("small");
                        String url=small.getString("url");
                        int width=small.getInt("width");
                        int height=small.getInt("height");
                        String size=small.getString("size");
                        String userImageURL=first.getString("userImageURL");
                        Video v=new Video();
                        v.setTags(tags);
                        v.setPicture_id(picture_id);
                        v.setUrl(url);
                        v.setUserImageURL(userImageURL);
                        videoList.add(v);



                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

                progressBarHandler.hide();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarHandler.hide();
                serverErrorDialog.showServerErrorDialog();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Video_Activity.this.finish();
                Intent i= new Intent(Video_Activity.this, MainActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
