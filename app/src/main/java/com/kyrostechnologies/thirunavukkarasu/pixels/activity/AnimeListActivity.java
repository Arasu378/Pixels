package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.AnimeEpisodeAdapters;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeEpisodeTitile;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.Storage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnimeListActivity extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    android.support.v7.app.ActionBar actionBar;
private Storage storage;
    private List<String>AnimeWatchList=new ArrayList<String>();
    private RecyclerView anime_episode_list;

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
        setContentView(R.layout.activity_anime_list);
        storage=Storage.getInstance(getApplicationContext());
        String animelist=storage.getAnimeListWAtch();
        anime_episode_list=(RecyclerView)findViewById(R.id.anime_episode_list);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        anime_episode_list.setLayoutManager(layoutManager);
        anime_episode_list.setItemAnimator(new DefaultItemAnimator());
        try{
            String title= AnimeEpisodeTitile.getHolder().getTitle();
            if(title!=null){
                actionBar.setTitle(title);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Gson gson=new Gson();
            Type type=new TypeToken<List<String>>(){}.getType();
            List<String> separate_class=gson.fromJson(animelist,type);
            AnimeWatchList=separate_class;
            AnimeEpisodeAdapters adapter=new AnimeEpisodeAdapters(AnimeListActivity.this,AnimeWatchList);
            anime_episode_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"List is not received",Toast.LENGTH_SHORT).show();
        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AnimeListActivity.this.finish();
                Intent i= new Intent(AnimeListActivity.this, AnimeDescriptionActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
