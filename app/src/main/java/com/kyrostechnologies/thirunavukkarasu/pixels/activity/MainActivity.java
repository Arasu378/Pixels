package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.TestingActivity;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.AdapterPicture;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.KeyPixabay;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.Pictures;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ScrollListenerPicture;
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
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_SDCARD = 21;
    private SessionManager sessionManager;
    private Storage storage;
    private RecyclerView recyler_picture;
    private AdapterPicture adapter;
    private List<Pictures>picturesList=new ArrayList<Pictures>();
    private ServerErrorDialog serverErrorDialog;
    private CheckOnline checkOnline;
    private ProgressBarHandler progressBarHandler;
    private String searchedquery=null;
    private int Searchedcurrentpage=1;
    private boolean loading =true;
    private SwipeRefreshLayout swipe_refresh;
    private FirebaseAnalytics mFirebaseAnalytics;

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
mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)&&(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))&&ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(),"Read write Permission needed",Toast.LENGTH_SHORT).show();

            } else {


                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE_SDCARD);

            }
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyler_picture=(RecyclerView)findViewById(R.id.recyler_picture);
        GetPictures(null,Searchedcurrentpage);
        swipe_refresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh) ;
        adapter=new AdapterPicture(MainActivity.this,picturesList);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
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


       recyler_picture.addOnScrollListener(new ScrollListenerPicture(layoutManager){
            @Override
            public void onLoadMore(int current_page) {
                if(searchedquery==null){
                    GetPictures(null,current_page);
                }else{
                    GetPictures(searchedquery,current_page);
                }
                adapter.notifyDataSetChanged();
            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                GetPictures(null,Searchedcurrentpage);

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "pixels id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "pixel name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  ScrollListenerPicture.reset(0, true);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_SDCARD: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                        Toast.makeText(getApplicationContext(),"you have denied the permission",Toast.LENGTH_LONG).show();

                    return;
                }

            }}
    }

    private void GetPictures(String query, int currentpage) {
        final String key = KeyPixabay.getKey();

        String i = "https://pixabay.com/api/?key=" + key+"&q="+query+"&image_type=photo&pretty=true&page="+currentpage;
        String url="https://pixabay.com/api/?key=" + key+"&image_type=photo&pretty=true&page="+currentpage;
        if(query==null){
            i=url;
        }
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
                        String likes=first.getString("likes");
                        String comments=first.getString("comments");
                        String user=first.getString("user");
                        String favorites=first.getString("favorites");
                        Pictures pic=new Pictures();
                        pic.setId(id);
                        pic.setTags(tag);
                        pic.setWebformatURL(webformatURL);
                        pic.setLikes(likes);
                        pic.setComments(comments);
                        pic.setUser(user);
                        pic.setFavorites(favorites);
                        picturesList.add(pic);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

                progressBarHandler.hide();
                try{
                    swipe_refresh.setRefreshing(false);
                }catch (Exception e){

                }

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
                searchedquery=i;
                    GetPictures(i,Searchedcurrentpage);

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
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else if(id==R.id.about_us){
            Intent l=new Intent(MainActivity.this,AboutUs.class);
            startActivity(l);
        }else if(id==R.id.nav_meme){
            Intent l=new Intent(MainActivity.this,MeMe.class);
            startActivity(l);
        }else if(id==R.id.nav_manga){
            Intent l=new Intent(MainActivity.this,MangaActivity.class);
            startActivity(l);
        }else if(id==R.id.nav_anime){
            Intent kki=new Intent(MainActivity.this,MyAnimeListActivity.class);
            startActivity(kki);
        }else if(id==R.id.nav_testing){
            Intent kki=new Intent(MainActivity.this,TestingActivity.class);
            startActivity(kki);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
