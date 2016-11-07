package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.MangaAdapter;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MangaActivity extends AppCompatActivity {
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView manga_recyclerview;
    private SwipeRefreshLayout swipe_manga;
    private List<MangaClass>mangaClassList=new ArrayList<MangaClass>();
    private MangaAdapter adapter;
    private String  MangaResponse=null;
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
        setContentView(R.layout.activity_manga);

        File file = new File( "/sdcard/"+"Manga.json");
        if(!file.exists()) {
            Toast.makeText(getApplicationContext(),"Creating the file",Toast.LENGTH_LONG).show();
            GetManga();
        }else{
            AsyncReader i=new AsyncReader();
            i.execute();
            Toast.makeText(getApplicationContext(),"Reading the file",Toast.LENGTH_LONG).show();

        }
        manga_recyclerview=(RecyclerView)findViewById(R.id.manga_recyclerview);
        adapter=new MangaAdapter(MangaActivity.this,mangaClassList);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        manga_recyclerview.setLayoutManager(layoutManager);
        manga_recyclerview.setItemAnimator(new DefaultItemAnimator());
        manga_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipe_manga=(SwipeRefreshLayout)findViewById(R.id.swipe_manga);
        swipe_manga.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                GetManga();

            }
        });
    }

    private void GetManga() {


        String url="http://www.mangaeden.com/api/list/0/";
        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Manga_response",response.toString());
                MangaResponse=response.toString();
                if(MangaResponse!=null){
                    AsyncWriter writer=new AsyncWriter();
                    writer.execute();
                }else{
                    Toast.makeText(MangaActivity.this,"Could not write to External sd card..",Toast.LENGTH_LONG).show();
                }

                try{
                    JSONArray manga=response.getJSONArray("manga");
                    for(int i=0;i<manga.length();i++){
                        JSONObject first=manga.getJSONObject(i);
                        String MangaTitle=first.getString("t");
                        String Id=first.getString("i");
                        MangaClass mangaClass=new MangaClass();
                        mangaClass.setId(Id);
                        mangaClass.setMangaTitle(MangaTitle);
                        mangaClassList.add(mangaClass);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                progressBarHandler.hide();
                try{
                    swipe_manga.setRefreshing(false);
                }catch (Exception e){

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

        };
        ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                MangaActivity.this.finish();
                Intent i= new Intent(MangaActivity.this, MainActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                final List<MangaClass>filteredmanga=filter(mangaClassList,query);
                adapter.setFilter(filteredmanga);

                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
                final List<MangaClass>filteredmanga=filter(mangaClassList,newText);
                adapter.setFilter(filteredmanga);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);    }
    private List<MangaClass>filter(List<MangaClass>mangaClassList,String query){
        query=query.toLowerCase();
        final List<MangaClass>filterdlist=new ArrayList<>();
        for(MangaClass movie:mangaClassList){
            final String text=movie.getMangaTitle().toLowerCase();
            if(text.contains(query)){
                filterdlist.add(movie);
            }
        }
        return filterdlist;
    }
    private void writeToFile(String content,String fileName) {
        FileOutputStream fos;
        try {
            File myFile = new File("/sdcard/"+fileName);
            myFile.createNewFile();
            FileOutputStream fOut = new

                    FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new

                    OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.close();
            fOut.close();

//            Toast.makeText(MangaActivity.this,fileName + "saved",Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

    }
    public class AsyncReader extends AsyncTask<String,String,String >{
        private ProgressDialog progressDialog;
             @Override
        protected String doInBackground(String... params) {
                 ReadFile("Manga.json");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MangaActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Reading File Please wait..");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }
    public class AsyncWriter extends AsyncTask<String,String,String >{
        private ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            writeToFile(MangaResponse,"Manga.json");

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MangaActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Writing File Please wait..");
            pd.show();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

        }
    }
    private void ReadFile(String filename){
        StringBuffer stringBuffer = new StringBuffer();
        String aDataRow = "";
        String aBuffer = "";
        try {
            File myFile = new File("/sdcard/"+filename);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            JSONObject response=new JSONObject(aBuffer);

            try{
                JSONArray manga=response.getJSONArray("manga");
                for(int i=0;i<manga.length();i++){
                    JSONObject first=manga.getJSONObject(i);
                    String MangaTitle=first.getString("t");
                    String Id=first.getString("i");
                    MangaClass mangaClass=new MangaClass();
                    mangaClass.setId(Id);
                    mangaClass.setMangaTitle(MangaTitle);
                    mangaClassList.add(mangaClass);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            progressBarHandler.hide();
            try{
                swipe_manga.setRefreshing(false);
            }catch (Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
