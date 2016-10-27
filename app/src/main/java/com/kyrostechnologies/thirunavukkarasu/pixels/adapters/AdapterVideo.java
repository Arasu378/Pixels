package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.activity.PlayVideoActivity;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.PlayVideoClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 26-10-2016.
 */

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.MyViewHolder>{
    private Context mContext;
    private List<Video>videoList;
    public AdapterVideo (Context mContext, List<Video>videoList){
        this.mContext=mContext;
        this.videoList=videoList;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView video_cllick;
        public LinearLayout click_next_page;

        public MyViewHolder(View itemView) {
            super(itemView);
            video_cllick=(ImageView)itemView.findViewById(R.id.video_cllick);
            click_next_page=(LinearLayout)itemView.findViewById(R.id.click_next_page);
        }
    }
    @Override
    public AdapterVideo.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext)
                .inflate(R.layout.video_thumb,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterVideo.MyViewHolder holder, int position) {
        Video vid=videoList.get(position);
         final String  picture_id=vid.getPicture_id();
         final String url=vid.getUrl();
         final String tags=vid.getTags();
        String userImageURL= vid.getUserImageURL();
        try{
            if(picture_id!=null){
                String thumb_url="https://i.vimeocdn.com/video/"+picture_id+"_"+"640x360.jpg";
                Picasso.with(mContext).load(thumb_url).resize(640,360).centerCrop().into(holder.video_cllick);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
            holder.click_next_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(mContext.getApplicationContext(), PlayVideoActivity.class);
                    PlayVideoClass.getholder().setTags(tags);
                    PlayVideoClass.getholder().setUrl(url);
                    PlayVideoClass.getholder().setPicture_id(picture_id);
                    mContext.startActivity(i);
                }
            });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
