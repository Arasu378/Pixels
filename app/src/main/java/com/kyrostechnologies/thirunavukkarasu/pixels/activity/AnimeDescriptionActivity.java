package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeDescriptionHolderClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeEpisodeTitile;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.Storage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimeDescriptionActivity extends AppCompatActivity {
    private String url=null;
    private Button episode_list;
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private List<String>AnimeWatchList=new ArrayList<String >();
    private ImageView image_anime_icon;
    private TextView anime_title_watch,anime_rating_watch,total_episodes,description_anime_watch;
    android.support.v7.app.ActionBar actionBar;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        storage=Storage.getInstance(getApplicationContext());
        setContentView(R.layout.activity_anime_description);
        url= AnimeDescriptionHolderClass.getHolderClass().getUrl();
        if(url!=null){
            GetAnimeDescription();
        }else{
            Toast.makeText(getApplicationContext(),"No Episodes List",Toast.LENGTH_SHORT).show();
        }
        image_anime_icon=(ImageView)findViewById(R.id.image_anime_icon);
        episode_list=(Button)findViewById(R.id.episode_list);
        anime_title_watch=(TextView)findViewById(R.id.anime_title_watch);
        anime_rating_watch=(TextView)findViewById(R.id.anime_rating_watch);
        total_episodes=(TextView)findViewById(R.id.total_episodes);
        description_anime_watch=(TextView)findViewById(R.id.description_anime_watch);
        episode_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i= new Intent(AnimeDescriptionActivity.this,AnimeListActivity.class);
                startActivity(i);
            }
        });
    }

    private void GetAnimeDescription() {

        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("MyAnimeList",response.toString());
                try{
                    String title=response.getString("title");
                    if(title!=null){
                        anime_title_watch.setText(title);
                        AnimeEpisodeTitile.getHolder().setTitle(title);
                        actionBar.setTitle(title);
                    }
                    String thumbnail=response.getString("thumbnail");
                    String rating=response.getString("rating");
                    if(rating!=null){
                        String ss="Rating :"+rating;
                        anime_rating_watch.setText(ss);
                    }
                    String summary=response.getString("summary");
                    if(summary!=null){
                        description_anime_watch.setText(summary);
                    }
                    JSONArray episodes=response.getJSONArray("episodes");
                    int  is=episodes.length();
                    String values="Total Episodes: "+String.valueOf(is);
                    if(values!=null){

                        total_episodes.setText(values);
                    }
                    for(int i=0;i<episodes.length();i++){
                        String animes=episodes.getString(i);
                        AnimeWatchList.add(animes);
                    }
                    try{
                        Gson gson=new Gson();
                        String animeliststring=gson.toJson(AnimeWatchList);
                        storage.putAnimeListWAtch(animeliststring);

                    }catch (Exception e){
                        Log.d("exception_conve_gson",e.getMessage());
                    }
                    try{
                        Picasso.with(getApplicationContext()).load(thumbnail).resize(150,100).centerCrop().into(image_anime_icon);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


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
                AnimeDescriptionActivity.this.finish();
                Intent i= new Intent(AnimeDescriptionActivity.this, MyAnimeListActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
