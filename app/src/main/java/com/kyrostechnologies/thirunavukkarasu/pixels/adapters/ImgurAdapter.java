package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.felipecsl.gifimageview.library.GifImageView;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ImgurClas;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 21-11-2016.
 */

public class ImgurAdapter extends RecyclerView.Adapter<ImgurAdapter.MyViewHolder> {
    private Context mContext;
    private List<ImgurClas>imgurClasList;
    public ImgurAdapter(Context mContext, List<ImgurClas>imgurClasList){
        this.mContext=mContext;
        this.imgurClasList=imgurClasList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
      public GifImageView imgur_item_gif;
       public CardView imgur_card_view_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgur_card_view_item=(CardView)itemView.findViewById(R.id.imgur_card_view_item);
            imgur_item_gif=(GifImageView) itemView.findViewById(R.id.imgur_item_gif);
        }
    }
    @Override
    public ImgurAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.imgur_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgurAdapter.MyViewHolder holder, int position) {
        ImgurClas i=imgurClasList.get(position);
         String id=i.getId();
         String title=i.getTitle();
         String description=i.getDescription();
         String  datetime=i.getDatetime();
         String type=i.getType();
         boolean nsfw=i.isNsfw();
         boolean in_gallery=i.isIn_gallery();
         String  link=i.getLink();
         String ups=i.getUps();
         String downs=i.getDowns();
         String  points=i.getPoints();
         String score=i.getScore();
        animate(holder);
        if(link!=null){
            try{
                Glide.with(mContext)
                        .load(Uri.parse(link))
                        .asGif()
                        .crossFade()
                        .error(R.drawable.anime_icon)
                        .into(holder.imgur_item_gif);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(mContext,"not working gif",Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public int getItemCount() {
        return imgurClasList.size();
    }
    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
