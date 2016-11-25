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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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
    private long mBackPressed;
    private  final int TIME_INTERVAL = 2000;
    private  String CategoryString=null;
    private Spinner spinner_category_testing;
    private Spinner spinner_order_testing;



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
        spinner_category_testing=(Spinner)findViewById(R.id.spinner_category_testing);
        spinner_order_testing=(Spinner)findViewById(R.id.spinner_order_testing);
        List<String>spinner_order_string=new ArrayList<String>();
        List<String >catogory_item=new ArrayList<String>();
        spinner_order_string.add("popular");
        spinner_order_string.add("latest");
        catogory_item.add("all");
        catogory_item.add("fashion");
        catogory_item.add("nature");
        catogory_item.add("backgrounds");
        catogory_item.add("science");
        catogory_item.add("education");
        catogory_item.add("people");
        catogory_item.add("feelings");
        catogory_item.add("religion");
        catogory_item.add("health");
        catogory_item.add("places");
        catogory_item.add("animals");
        catogory_item.add("industry");
        catogory_item.add("food");
        catogory_item.add("computer");
        catogory_item.add("sports");
        catogory_item.add("transportation");
        catogory_item.add("buildings");
        catogory_item.add("business");
        catogory_item.add("music");

        ArrayAdapter<String>orderadapter=new ArrayAdapter<String>(this, R.layout.spinner_item_text, spinner_order_string);
        ArrayAdapter<String>categoryorder=new ArrayAdapter<String>(this,R.layout.spinner_item_text,catogory_item);
        orderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryorder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category_testing.setPrompt("Category");
        spinner_order_testing.setPrompt("Order");
        spinner_category_testing.setAdapter(categoryorder);
        spinner_order_testing.setAdapter(orderadapter);
        spinner_order_testing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                picturesList.clear();
                if(CategoryString!=null){
                    GetPictures(null,Searchedcurrentpage,CategoryString,item);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); spinner_category_testing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                picturesList.clear();
                GetPictures(null,Searchedcurrentpage,item,"popular");
                CategoryString=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GetPictures(null,Searchedcurrentpage,null,"popular");
        swipe_refresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh) ;
        adapter=new AdapterPicture(MainActivity.this,picturesList);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyler_picture.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
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
                    GetPictures(null,current_page,null,"popular");
                }else{
                    GetPictures(searchedquery,current_page,null,"popular");
                }
                adapter.notifyDataSetChanged();
            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                GetPictures(null,Searchedcurrentpage,null,"popular");

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

    private void GetPictures(String query, int currentpage,String Categorystr,String order ) {
        final String key = KeyPixabay.getKey();

        String i = "https://pixabay.com/api/?key=" + key+"&q="+query+"&image_type=photo&pretty=true&page="+currentpage;
        String url="https://pixabay.com/api/?key=" + key+"&image_type=photo&pretty=true&page="+currentpage;
        String categoryurl="https://pixabay.com/api/?key=" + key+"&order="+order+"&category="+Categorystr+"&image_type=photo&pretty=true&page="+currentpage;
        if(query==null&&Categorystr==null){
            i=url;
        }else if(Categorystr!=null){
            i=categoryurl;
        }
        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, i, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pixabay",response.toString());
                try{
                    JSONArray hits=response.getJSONArray("hits");
                    String total=response.getString("total");
                    String totalHits=response.getString("totalHits");
                    if(!totalHits.equals("0")){
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
                    }else{
                        Toast.makeText(getApplicationContext(),"Sorry Pictures not available",Toast.LENGTH_LONG).show();
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
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
            }


            mBackPressed = System.currentTimeMillis();        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

      // dataAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String i=query.replace(" ","+");
                picturesList.clear();
                searchedquery=i;
                    GetPictures(i,Searchedcurrentpage,null,"popular");

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
