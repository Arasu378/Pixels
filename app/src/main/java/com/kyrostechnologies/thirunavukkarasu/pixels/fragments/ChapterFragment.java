package com.kyrostechnologies.thirunavukkarasu.pixels.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.activity.MangaReadingActivity;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ChaptersArrayClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ChaptersClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaChapterID;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.Storage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thirunavukkarasu on 04-11-2016.
 */

public class ChapterFragment extends Fragment implements DescriptionFragment.DataPassListener{
    private RecyclerView chapter_recyclerview;
    private List<ChaptersClass>chaptersClassList=new ArrayList<ChaptersClass>();
    private Storage storage;
    private String ChaptersListString=null;
    private List<ChaptersArrayClass >ChaptersList=new ArrayList<ChaptersArrayClass>();
    private ChapterListAdapter adapter;
    private ProgressDialog pDialog;

    public  ChapterFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.chapter_fragment,container,false);
        chapter_recyclerview=(RecyclerView)view.findViewById(R.id.chapter_recyclerview);
        storage=Storage.getInstance(getContext());
       // ChaptersListString=storage.getChapterList();
showProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               holder();
            }
        }, 5000);



        return view;
    }

public void holder(){
dismissProgressDialog();
   // ChapterHolder ch=new ChapterHolder();
//    Log.d("identify","value nulll");
    ChaptersListString=storage.getChapterList();

//        Log.d("ChapterListString",ChaptersListString);
    if(ChaptersListString!=null){
        Toast.makeText(getContext(),"list loaded ",Toast.LENGTH_LONG).show();
        Log.d("identify",ChaptersListString);

        AsyncChapterListReader k=new AsyncChapterListReader();
        k.execute();
    }else{
        Toast.makeText(getContext(),"not loaded ",Toast.LENGTH_LONG).show();
    }
}
    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissProgressDialog();

    }


    @Override
    public void passData(String data) {
        Toast.makeText(getContext(),"Data:  "+data,Toast.LENGTH_LONG).show();

        this.ChaptersListString=data;

    }


    public class AsyncChapterListReader extends AsyncTask<String ,String,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
}

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{

                adapter=new ChapterListAdapter(getContext(),ChaptersList);
                final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                chapter_recyclerview.setLayoutManager(layoutManager);
                chapter_recyclerview.setItemAnimator(new DefaultItemAnimator());
                chapter_recyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }catch (Exception e){
                e.printStackTrace();
            }
            dismissProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            try{
                JSONArray first=new JSONArray(ChaptersListString);
                ChaptersList.clear();
                for(int i=0;i<first.length();i++){
                    JSONObject second=first.getJSONObject(i);
                    String ChapterDate=second.getString("ChapterDate");
                    String  ChapterId=second.getString("ChapterId");
                    String ChapterNumber=second.getString("ChapterNumber");
                    String ChapterTitle=second.getString("ChapterTitle");
                    ChaptersArrayClass s=new ChaptersArrayClass();
                    s.setChapterNumber(ChapterNumber);
                    s.setChapterDate(ChapterDate);
                    s.setChapterTitle(ChapterTitle);
                    s.setChapterId(ChapterId);
                    ChaptersList.add(s);

                }

            }catch (Exception e){

            }
            return null;
        }
    }
    public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.MyViewHolder>{
        private Context mContext;
        private List<ChaptersArrayClass>chaptersArrayClassList;
        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView chapter_list_item_number,chapter_list_item_title;
            public CardView continue_to_next_page;

            public MyViewHolder(View itemView) {
                super(itemView);
                chapter_list_item_number=(TextView)itemView.findViewById(R.id.chapter_list_item_number);
                chapter_list_item_title=(TextView)itemView.findViewById(R.id.chapter_list_item_title);
                continue_to_next_page=(CardView)itemView.findViewById(R.id.continue_to_next_page);
            }
        }
        public ChapterListAdapter(Context mContext, List<ChaptersArrayClass>chaptersArrayClassList){
            this.mContext=mContext;
            this.chaptersArrayClassList=chaptersArrayClassList;
        }

        @Override
        public ChapterListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_list_items,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChapterListAdapter.MyViewHolder holder, int position) {
            final ChaptersArrayClass m=chaptersArrayClassList.get(position);
             String ChapterNumber=m.getChapterNumber();
             String ChapterDate=m.getChapterDate();
             String ChapterTitle=m.getChapterTitle();
             final String ChapterId=m.getChapterId();
            if(ChapterNumber!=null){
                String no="NO: "+ChapterNumber;
                holder.chapter_list_item_number.setText(no);
            }if(ChapterTitle!=null&&!ChapterTitle.equals("null")){
                String title="Title :"+ChapterTitle;
                holder.chapter_list_item_title.setText(title);
            }
            holder.continue_to_next_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(mContext, MangaReadingActivity.class);
                    MangaChapterID.getHolder().setMangaChapterId(ChapterId);
                    mContext.startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return chaptersArrayClassList.size();
        }
    }
}
