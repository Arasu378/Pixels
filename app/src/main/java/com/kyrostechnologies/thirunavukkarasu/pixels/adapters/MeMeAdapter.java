package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MeMeClass;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 02-11-2016.
 */

public class MeMeAdapter extends RecyclerView.Adapter<MeMeAdapter.MyViewHolder> {
    private Context mContext;
    private List<MeMeClass> meClassList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public  ImageView photo_list_meme;
        public TextView meme_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            meme_title=(TextView)itemView.findViewById(R.id.meme_title);
            photo_list_meme=(ImageView)itemView.findViewById(R.id.photo_list_meme);

        }
    }
    public MeMeAdapter (Context mContext,List<MeMeClass>meClassList){
        this.mContext=mContext;
        this.meClassList=meClassList;
    }
    @Override
    public MeMeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.meme_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeMeAdapter.MyViewHolder holder, int position) {
        MeMeClass m=meClassList.get(position);
         String  id=m.getId();
         String name=m.getName();
         String url=m.getUrl();
         String width=m.getWidth();
         String height=m.getHeight();
        if(name!=null){
            holder.meme_title.setText(name);
        }if(url!=null){
            try{
                Picasso.with(mContext).load(url).centerCrop().into(holder.photo_list_meme);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return meClassList.size();
    }
}
