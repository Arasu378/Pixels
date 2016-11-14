package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeEpisodeTitile;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;

public class PlayAnimeActivity extends AppCompatActivity implements EasyVideoCallback{
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    android.support.v7.app.ActionBar actionBar;
    private   String playurl=null;
    private EasyVideoPlayer player_anime;
    private int current_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_anime);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
         playurl= AnimeEpisodeTitile.getHolder().getPlayurl();
        String title=AnimeEpisodeTitile.getHolder().getTitle();
        String episodeno=AnimeEpisodeTitile.getHolder().getEpisodeNo();

        player_anime=(EasyVideoPlayer)findViewById(R.id.player_anime);
        player_anime.setCallback(this);
        if(title!=null){
            String ss=title+" "+episodeno;
            actionBar.setTitle(ss);
        }


        if(playurl!=null){
                      player_anime.setSource(Uri.parse(playurl));
            player_anime.setAutoPlay(true);

        }
        int oreintation=getScreenOrientation();
        if(oreintation==2){
            player_anime.pause();
            player_anime.setAutoFullscreen(true);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                PlayAnimeActivity.this.finish();
                Intent i= new Intent(PlayAnimeActivity.this, AnimeListActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        player_anime.pause();
        current_position=player_anime.getCurrentPosition();

    }
    @Override
    protected void onResume() {
        super.onResume();
        player_anime.seekTo(current_position);
    }
    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Toast.makeText(getApplicationContext(),"An Error Occured :"+e.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        Toast.makeText(getApplicationContext(),"player retrying",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
