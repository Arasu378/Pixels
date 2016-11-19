package com.kyrostechnologies.thirunavukkarasu.pixels;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.KeyPixabay;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.CheckOnline;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        progressBarHandler=new ProgressBarHandler(this);
        serverErrorDialog=new ServerErrorDialog(this);
        sort_id=(Spinner)findViewById(R.id.sort_id);
        select_id=(Spinner)findViewById(R.id.select_id);
        List<String>sort_list=new ArrayList<String>();
        sort_list.add(" ");
        sort_list.add("viral");
        sort_list.add("top");
        sort_list.add("time");
        List<String>select_list=new ArrayList<String>();
        select_list.add(" ");
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
                Toast.makeText(getApplicationContext(),"Selected :"+item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); sort_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Selected :"+item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GetPictures();
    }
    private void GetPictures() {
        final String key = KeyPixabay.getKey();

        String i = "https://api.imgur.com/3/gallery/hot/viral/0.json";

        progressBarHandler.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, i, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Imgur_Response",response.toString());
                try {
                        boolean success=response.getBoolean("success");
                    if(!success){
                        Toast.makeText(getApplicationContext(),"Not connected",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Connected successfully",Toast.LENGTH_SHORT).show();

                    }
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
                //params.put("Content-Type", "application/json");
                params.put("Authorization: ", "103816b47d6169d");
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
