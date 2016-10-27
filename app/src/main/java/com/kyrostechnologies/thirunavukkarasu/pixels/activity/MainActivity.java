package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.AdapterPicture;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.KeyPixabay;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.Pictures;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.SessionManager;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.Storage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
private SessionManager sessionManager;
    private Storage storage;
    private RecyclerView recyler_picture;
    private AdapterPicture adapter;
    private List<Pictures>picturesList=new ArrayList<Pictures>();
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager( getApplicationContext());
        sessionManager.checkLogin();
        serverErrorDialog=new ServerErrorDialog(this);
        checkOnline=new CheckOnline(this);
        checkOnline.checkOnline();
        progressBarHandler=new ProgressBarHandler(this);
        storage=Storage.getInstance(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyler_picture=(RecyclerView)findViewById(R.id.recyler_picture);
        GetPictures("fruits");
        adapter=new AdapterPicture(MainActivity.this,picturesList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyler_picture.setLayoutManager(layoutManager);
        recyler_picture.setItemAnimator(new DefaultItemAnimator());
        recyler_picture.setAdapter(adapter);
        adapter.notifyDataSetChanged();
            View headerview = navigationView.getHeaderView(0);
             ImageView addprofile = (ImageView) headerview.findViewById(R.id.imageView_profile_pic);
            TextView nameuserlogin = (TextView) headerview.findViewById(R.id.name_header_view);
            TextView email = (TextView) headerview.findViewById(R.id.email_header_view);
        String userpicture=storage.getUserPicture();
        String EmailS=storage.getEmailId();
        String Username=storage.getDisplayName();
        if(EmailS!=null){
            email.setText(EmailS);
        }if(Username!=null){
            nameuserlogin.setText(Username);
        }if(userpicture!=null){
            Picasso.with(getApplicationContext())
                    .load(userpicture)
                    .resize(100, 100)
                    .centerCrop()
                    .into(addprofile);
        }



    }

    private void GetPictures(String query) {
        final String key = KeyPixabay.getKey();

        String i = "https://pixabay.com/api/?key=" + key+"&q="+query+"&image_type=photo&pretty=true";
        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, i, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pixabay",response.toString());
                try{
                    JSONArray hits=response.getJSONArray("hits");
                    for(int k=0; k<hits.length();k++){
                        JSONObject first=hits.getJSONObject(k);
                        String tag=first.getString("tags");
                        String webformatURL=first.getString("webformatURL");
                        String id=first.getString("id");
                        Pictures pic=new Pictures();
                        pic.setId(id);
                        pic.setTags(tag);
                        pic.setWebformatURL(webformatURL);
                        picturesList.add(pic);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                String i=query.replace(" ","+");
                picturesList.clear();
                    GetPictures(i);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_pictures) {
        } else if (id == R.id.nav_videos) {
            Intent i=new Intent(MainActivity.this,Video_Activity.class);
            startActivity(i);
        }else if(id==R.id.nav_share){
        }else if(id==R.id.nav_logout){
            sessionManager.logoutUser();
            storage.clear();
            Intent i=new Intent(MainActivity.this,Login_Activity.class);
            startActivity(i);
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
