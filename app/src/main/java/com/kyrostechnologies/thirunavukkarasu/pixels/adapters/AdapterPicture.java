package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.activity.Picture_Fullscreen;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.FullScreenClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.Pictures;
import com.squareup.picasso.Callback;
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
        public TextView picture_title,like_count,rating_count,comment_count;
        public LinearLayout linear_picture;
        public MyViewHolder(View itemView) {
            super(itemView);
            photo_list=(ImageView)itemView.findViewById(R.id.photo_list);
            picture_title=(TextView)itemView.findViewById(R.id.picture_title);
            like_count=(TextView)itemView.findViewById(R.id.like_count);
            rating_count=(TextView)itemView.findViewById(R.id.rating_count);
            comment_count=(TextView)itemView.findViewById(R.id.comment_count);
            linear_picture=(LinearLayout)itemView.findViewById(R.id.linear_picture);
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
            final Pictures pic=picturesList.get(position);
        final String id=pic.getId();
        final String tag=pic.getTags();
        final String  likes=pic.getLikes();
        final String  comments=pic.getComments();
        final String  favorites=pic.getFavorites();
        final String users=pic.getUser();
        if(users!=null){
           holder.picture_title.setText(users);
        }if(favorites!=null){
            holder.rating_count.setText(favorites);
        }if(comments!=null){
            holder.comment_count.setText(comments);
        }if(likes!=null){
            holder.like_count.setText(likes);
        }

        final String picture=pic.getWebformatURL();
        animate(holder);
        try{
            if(picture!=null){
                Picasso.with(mContext).load(picture).resize(600,400).centerCrop().into(holder.photo_list, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }catch (Exception e){

        }
        holder.linear_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, Picture_Fullscreen.class);
                FullScreenClass.getHolder().setId(id);
                FullScreenClass.getHolder().setPicture(picture);
                FullScreenClass.getHolder().setTag(tag);
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }
    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
