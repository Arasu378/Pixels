package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.FullScreenClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.StringTokenizer;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class Picture_Fullscreen extends AppCompatActivity {
    private ImageViewTouch imgDisplay;
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private TextView tag_full_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_picture__fullscreen);
        tag_full_screen=(TextView)findViewById(R.id.tag_full_screen);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        imgDisplay=(ImageViewTouch)findViewById(R.id.imgDisplay);
        String id=  FullScreenClass.getHolder().getId();
        String tag=FullScreenClass.getHolder().getTag();
        String picture=FullScreenClass.getHolder().getPicture();
        if(tag!=null){
            tag_full_screen.setText(tag);
        }if(picture!=null){
            StringTokenizer k=new StringTokenizer(picture,"_");
            String first=k.nextToken();
            String url=first+"_960.jpg";
            progressBarHandler.show();
            try{
                Picasso.with(getApplicationContext()).load(url).resize(960,720).centerCrop().into(imgDisplay, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBarHandler.hide();

                    }

                    @Override
                    public void onError() {
                        progressBarHandler.hide();
                        Toast.makeText(getApplicationContext(),"Cannot Load Picture",Toast.LENGTH_SHORT).show();

                    }
                });

            }catch (Exception e){

            }
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Picture_Fullscreen.this.finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
