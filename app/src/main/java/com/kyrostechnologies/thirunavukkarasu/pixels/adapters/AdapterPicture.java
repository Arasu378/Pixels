package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.Pictures;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 26-10-2016.
 */

public class AdapterPicture extends RecyclerView.Adapter<AdapterPicture.MyViewHolder> {
    private Context mContext;
    private List<Pictures>picturesList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView photo_list;
        public TextView tag_text;
        public MyViewHolder(View itemView) {
            super(itemView);
            photo_list=(ImageView)itemView.findViewById(R.id.photo_list);
            tag_text=(TextView)itemView.findViewById(R.id.tag_text);
        }
    }
    public AdapterPicture (Context mContext,List<Pictures>pictures){
        this.mContext=mContext;
        this.picturesList=pictures;
    }
    @Override
    public AdapterPicture.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPicture.MyViewHolder holder, int position) {
            Pictures pic=picturesList.get(position);
        String id=pic.getId();
        String tag=pic.getTags();
        if(tag!=null){
            holder.tag_text.setText(tag);
        }
        String picture=pic.getWebformatURL();
        try{
            if(picture!=null){
                Picasso.with(mContext).load(picture).resize(600,400).centerCrop().into(holder.photo_list);
            }
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }
}
