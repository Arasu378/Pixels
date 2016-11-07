package com.kyrostechnologies.thirunavukkarasu.pixels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ChaptersArrayClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaIdClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaPictureURL;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ProgressBarHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServerErrorDialog;
import com.kyrostechnologies.thirunavukkarasu.pixels.servicehandler.ServiceHandler;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.Storage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thirunavukkarasu on 04-11-2016.
 */

public class DescriptionFragment extends Fragment {
    private TextView manga_title_description,released_date_description,catogories_list_description,brief_description;
    private ImageView manga_picture_description;
    private String MangaId=null;
    private ProgressBarHandler progressBarHandler;
    private ServerErrorDialog serverErrorDialog;
    private String Title=null;
    private String Description=null;
    private String PictureURL=null;
   private List<String>genereslist=new ArrayList<String>();
    private List<String>ChaptersList=new ArrayList<String>();
    private List<ChaptersArrayClass>ChaptersArrayList=new ArrayList<ChaptersArrayClass>();
    private static final String TAG=DescriptionFragment.class.getSimpleName();
    private static final String  FILENAME="Manga.json";
    private Storage storage;
    public DescriptionFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View descriptionlayout=inflater.inflate(R.layout.description_fragment,container,false);
        progressBarHandler=new ProgressBarHandler(getContext());
        serverErrorDialog=new ServerErrorDialog(getContext());
        manga_title_description=(TextView)descriptionlayout.findViewById(R.id.manga_title_description);
        released_date_description=(TextView)descriptionlayout.findViewById(R.id.released_date_description);
        catogories_list_description=(TextView)descriptionlayout.findViewById(R.id.catogories_list_description);
        brief_description=(TextView)descriptionlayout.findViewById(R.id.brief_description);
        manga_picture_description=(ImageView)descriptionlayout.findViewById(R.id.manga_picture_description);
        MangaId= MangaIdClass.getHolder().getId();
        storage=Storage.getInstance(getContext());
        if(MangaId!=null){
            GetMangaDescriptios();
        }
        return descriptionlayout;
    }

    private void GetMangaDescriptios() {
            String url="http://www.mangaeden.com/api/manga/"+MangaId+"/ ";
            progressBarHandler.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Manga_description_res",response.toString());
                    try {
                        String alias=response.getString("alias");
                        String author=response.getString("author");
                        String chapters_len=response.getString("chapters_len");
                        String created=response.getString("created");
                        String description=response.getString("description");
                        Description=description;
                        String image=response.getString("image");
                        PictureURL=image;
                        String last_chapter_date=response.getString("last_chapter_date");
                        String released=response.getString("released");
                        if(released!=null){
                            if(!released.equals("")&&!released.equals("null")){
                                released_date_description.setText(released);
                            }else{
                                released_date_description.setText("Released Date");
                            }
                        }
                        String title=response.getString("title");
                        Title=title;
                        if(Title!=null){
                            manga_title_description.setText(Title);
                        }if(Description!=null){
                            String des= Html.fromHtml(Description).toString();

                            brief_description.setText(des);
                        }if(PictureURL!=null){
                            String picurl= MangaPictureURL.PICTUREURL+"/"+PictureURL;
                            progressBarHandler.show();
                            try{
                                Picasso.with(getContext()).load(picurl).resize( 150,200).centerCrop().into(manga_picture_description, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBarHandler.hide();

                                    }

                                    @Override
                                    public void onError() {
                                        progressBarHandler.hide();
                                        Toast.makeText(getContext(),"Cannot Load Picture",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        try{
                            JSONArray categories=response.getJSONArray("categories");
                            for(int k=0;k<categories.length();k++){
                                String genere=categories.getString(k);
                                Log.d("genere_text",genere);
                                genereslist.add(genere);

                            }
                            StringBuilder builder=new StringBuilder();
                            for (String generesitem:genereslist){
                                builder.append(generesitem+" ");
                            }
                            catogories_list_description.setText(builder.toString());

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            JSONArray chapters=response.getJSONArray("chapters");
                            for(int l=0;l<chapters.length();l++){
                                ChaptersArrayClass s=new ChaptersArrayClass();

                                JSONArray first=chapters.getJSONArray(l);
                                for(int m=0;m<first.length();m++){
                                    String chaptercontent=first.getString(m);
                                  //  String ChapterNumber=first.getString(0);
//
                                   // String ChapterDate=first.getString(1);
                                   // String ChapterTitle=first.getString(2);
                                   // String ChapterId=first.getString(3);

                                    if(m==0){
                                        s.setChapterNumber(chaptercontent);
                                    } else if (m == 1) {
                                        s.setChapterDate(chaptercontent);
                                    }else if(m==2){
                                        s.setChapterTitle(chaptercontent);
                                    }else if(m==3){
                                        s.setChapterId(chaptercontent);
                                    }
                                   // ChaptersList.add(chaptercontent);
                                   // Log.d("Chapter_content",chaptercontent);

                                }
                                ChaptersArrayList.add(s);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            Gson gson=new Gson();
                            String ChapterString=gson.toJson(ChaptersArrayList);
                            storage.putChapterList(ChapterString);
                        }catch (Exception e){
                            Log.d("exception_conve_gson",e.getMessage());
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
                        Log.e("error_imgflip",error.toString());
                    }

                }
            }) {

            };
            ServiceHandler.getInstance().addToRequestQueue(jsonObjectRequest);

    }




}
