package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.AnimeAdapters;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaPictureURL;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ScrollListenerPicture;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAnimeListActivity extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView anime_recyclerview;
    private SwipeRefreshLayout swipe_anime_list;
    private int Searchedcurrentpage=1;
    private String searchedquery=null;
    private AnimeAdapters adapter;
    private List<AnimeClass>animeClassList=new ArrayList<AnimeClass>();
    private ImageView back_arrow;
    private ViewFlipper ss;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        setContentView(R.layout.activity_my_anime_list);
        TestingApi(null,Searchedcurrentpage);
        anime_recyclerview=(RecyclerView)findViewById(R.id.anime_recyclerview);
        adapter=new AnimeAdapters(MyAnimeListActivity.this,animeClassList);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        anime_recyclerview.setLayoutManager(layoutManager);
        anime_recyclerview.setItemAnimator(new DefaultItemAnimator());
        anime_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipe_anime_list=(SwipeRefreshLayout)findViewById(R.id.swipe_anime_list);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAnimeListActivity.this.finish();
            }
        });
        swipe_anime_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TestingApi(null,Searchedcurrentpage);

            }
        });
        anime_recyclerview.addOnScrollListener(new ScrollListenerPicture(layoutManager){
            @Override
            public void onLoadMore(int current_page) {
                if(searchedquery==null){
                    TestingApi(null,current_page);
                }else{
                    TestingApi(searchedquery,current_page);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void TestingApi(String query,int page) {

        String url= MangaPictureURL.VIDEOURL+"anime?page="+page;
        String i=MangaPictureURL.VIDEOURL+"anime/?search="+"Searchquery";
        progressBarHandler.show();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONArray response) {
                Log.d("MyAnimeList",response.toString());
                try{

                    for(int i=0; i<response.length();i++){
                        JSONObject first=response.getJSONObject(i);
                        String title=first.getString("title");
                        String uploaded=first.getString("uploaded");
                        String  thumbnail=first.getString("thumbnail");
                        String url=first.getString("url");
                        AnimeClass animeClass=new AnimeClass();
                        animeClass.setThumbnail(thumbnail);
                        animeClass.setTitle(title);
                        animeClass.setUploaded(uploaded);
                        animeClass.setUrl(url);
                        animeClassList.add(animeClass);
                    }

                    adapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"not working",Toast.LENGTH_LONG).show();
                }
                try{
                    swipe_anime_list.setRefreshing(false);
                }catch (Exception e){

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

}
