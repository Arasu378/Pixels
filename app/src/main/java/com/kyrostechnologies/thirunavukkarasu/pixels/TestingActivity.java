package com.kyrostechnologies.thirunavukkarasu.pixels;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.ImgurAdapter;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ImgurClas;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.KeyPixabay;
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

public class TestingActivity extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private Spinner sort_id;
    private Spinner select_id;
    private RecyclerView recycler_imgur;
    private ImgurAdapter adapter;
    private List<ImgurClas>imgurClasList=new ArrayList<ImgurClas>();
    private SwipeRefreshLayout swipe_imgur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_testing);
        progressBarHandler=new ProgressBarHandler(this);
        serverErrorDialog=new ServerErrorDialog(this);
        sort_id=(Spinner)findViewById(R.id.sort_id);
        select_id=(Spinner)findViewById(R.id.select_id);
        recycler_imgur=(RecyclerView)findViewById(R.id.recycler_imgur);
        swipe_imgur=(SwipeRefreshLayout)findViewById(R.id.swipe_imgur);
            swipe_imgur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    GetPictures();
                    swipe_imgur.setRefreshing(false);
                }
            });
        GetPictures();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recycler_imgur.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recycler_imgur.setItemAnimator(new DefaultItemAnimator());
        adapter=new ImgurAdapter(TestingActivity.this,imgurClasList);
        recycler_imgur.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        List<String>sort_list=new ArrayList<String>();
        sort_list.add("viral");
        sort_list.add("top");
        sort_list.add("time");
        List<String>select_list=new ArrayList<String>();
        select_list.add("hot");
        select_list.add("top");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_id.setPrompt("Sort");
        sort_id.setAdapter(dataAdapter);
        ArrayAdapter<String> dataAdapter12 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, select_list);
        dataAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_id.setPrompt("Section");
        select_id.setAdapter(dataAdapter12);
        select_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); sort_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void GetPictures() {
        final String key = KeyPixabay.getKey();

        String i = "https://api.imgur.com/3/gallery/hot/viral/0.json";
        String meme="https://api.imgur.com/3/g/memes/viral/";

        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, i, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Imgur_Response",response.toString());
                try {
                        boolean success=response.getBoolean("success");
                    if(success){
                        JSONArray data=response.getJSONArray("data");
                        Toast.makeText(getApplicationContext(),"Connected successfully",Toast.LENGTH_SHORT).show();

                        for(int i=0; i<data.length();i++){
                            JSONObject first=data.getJSONObject(i);
                            String id=first.getString("id");
                            String title=first.getString("title");
                            String description=first.getString("description");
                            String datetime=first.getString("datetime");
                            String type=null;
                            try{
                                type=first.getString("type");
                            }catch (Exception e){
                            }
                            boolean nsfw=first.getBoolean("nsfw");
                         //   String account_url=first.getString("account_url");
                           // String  account_id=first.getString("account_id");
                            boolean in_gallery=first.getBoolean("in_gallery");
                            String link=first.getString("link");
                            String ups=first.getString("ups");
                            String downs=first.getString("downs");
                            String points=first.getString("points");
                            String score=first.getString("score");

                            if(type!=null){
                                if(type.equals("image/gif")){
                                ImgurClas imgurClas=new ImgurClas();
                                imgurClas.setId(id);
                                imgurClas.setTitle(title);
                                imgurClas.setDescription(description);
                                imgurClas.setDatetime(datetime);
                                imgurClas.setType(type);
                                imgurClas.setNsfw(nsfw);
                                imgurClas.setIn_gallery(in_gallery);
                                imgurClas.setLink(link);
                                imgurClas.setUps(ups);
                                imgurClas.setDowns(downs);
                                imgurClas.setPoints(points);
                                imgurClas.setScore(score);
                                imgurClasList.add(imgurClas);
                            }}


                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"Not connected",Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                progressBarHandler.hide();
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarHandler.hide();
                serverErrorDialog.showServerErrorDialog();
                if(error!=null){
                    Log.e("error_imgur",error.toString());
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams()throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
              //  params.put("Content-Type", "application/json");
                String cred=String.format("8746663ace1a471");
                String auth="Client-ID "+ Base64.encodeToString(cred.getBytes(),Base64.DEFAULT);
                params.put("Authorization: ", "Client-ID "+cred);
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                TestingActivity.this.finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
