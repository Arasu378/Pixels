package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.PlayVideoClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;

public class PlayVideoActivity extends AppCompatActivity implements EasyVideoCallback {
    private String  picture_id=null;
    private String url=null;
    private String tags=null;
    private String userImageURL=null;
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private    EasyVideoPlayer videoView;
    private int current_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_play_video);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        picture_id= PlayVideoClass.getholder().getPicture_id();
        url= PlayVideoClass.getholder().getUrl();
        tags= PlayVideoClass.getholder().getTags();
        userImageURL= PlayVideoClass.getholder().getUserImageURL();
         videoView =(EasyVideoPlayer)findViewById(R.id.videoView1);
        videoView.setCallback(this);

     if(url!=null){
         videoView.setSource(Uri.parse(url));
         videoView.setAutoPlay(true);

     }
        int oreintation=getScreenOrientation();
        if(oreintation==2){
            videoView.pause();
            videoView.setAutoFullscreen(true);
        }


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
        videoView.pause();
        current_position=videoView.getCurrentPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(current_position);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                PlayVideoActivity.this.finish();
                Intent i= new Intent(PlayVideoActivity.this, Video_Activity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
