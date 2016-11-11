package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ChapterHolder;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaChapterID;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaPictureURL;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class MangaReadingActivity extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private String ChapterId=null;
    private List<String >ReadList=new ArrayList<String >();
    private ViewPager viewPager;
    private MyViewPageAdapter myViewPageAdapter;
    private TextView lbl_count;
    private int selectedPosition=0;
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
        setContentView(R.layout.slider_layout_manga);
        ChapterId= MangaChapterID.getHolder().getMangaChapterId();

        String title= ChapterHolder.getHolder().getMangaTitle();
        if(title!=null){
            actionBar.setTitle(title);
        }else {
            Toast.makeText(getApplicationContext(),"title is null",Toast.LENGTH_SHORT).show();

        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        lbl_count=(TextView)findViewById(R.id.lbl_count);


        if(ChapterId!=null){
            GetMangaPictures();
        }


    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener=new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
          //  displayMetaInfo(position);
        }

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
               super.onPageScrollStateChanged(state);
        }
    };
    public void setCurrentItem(int currentItem) {
        viewPager.setCurrentItem(currentItem,false);
        displayMetaInfo(selectedPosition);
    }
    private void displayMetaInfo(int selectedPosition) {
        lbl_count.setText((selectedPosition+1)+" of "+ReadList.size());

    }


    private void GetMangaPictures() {

        String url="http://www.mangaeden.com/api/chapter/"+ChapterId+"/";
        progressBarHandler.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Chapter_Book",response.toString());
                ReadList.clear();
                try{
                    JSONArray images=response.getJSONArray("images");
                    for (int i=0;i<images.length();i++){
                        String value=images.getString(i);
                        Log.d("return_value",value);
                            ReadList.add(value);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
                progressBarHandler.hide();
                myViewPageAdapter = new MyViewPageAdapter();
                viewPager.setAdapter(myViewPageAdapter);
                viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                setCurrentItem(selectedPosition);
                myViewPageAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarHandler.hide();
                serverErrorDialog.showServerErrorDialog();
                if(error!=null){
                    Log.e("error_Chapter_Book",error.toString());
                }

            }
        }) {



        };
        ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                MangaReadingActivity.this.finish();
                Intent i= new Intent(MangaReadingActivity.this, MangaDescriptionActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class MyViewPageAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public MyViewPageAdapter(){

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater =(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view= layoutInflater.inflate(R.layout.activity_manga_reading,container,false);
            ImageViewTouch imgDisplay=(ImageViewTouch)view.findViewById(R.id.imgDisplay);
            String picture=ReadList.get(position);
            progressBarHandler.show();
            String value = null;
            try{
                JSONArray first=new JSONArray(picture);

                     value=first.getString(1);


            }catch (Exception e){
                e.printStackTrace();
            }
            String pictureurl= MangaPictureURL.PICTUREURL+value;
            Log.d("picture_url",pictureurl);
            try{
                Picasso.with(getApplicationContext()).load(pictureurl).into(imgDisplay, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBarHandler.hide();

                    }

                    @Override
                    public void onError() {
                        progressBarHandler.hide();

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return ReadList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==((View)object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

}
