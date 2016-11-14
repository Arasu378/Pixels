package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.activity.AnimeDescriptionActivity;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeDescriptionHolderClass;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 10-11-2016.
 */

public class AnimeAdapters extends RecyclerView.Adapter<AnimeAdapters.MyViewHolder> {
  private Context mContext;
    private List<AnimeClass>animeClassList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView_next_anime;
        public ImageView anime_icon_list_item;
        public TextView title_text_anime,uploadedon;

      public MyViewHolder(View itemView) {

          super(itemView);
          cardView_next_anime=(CardView)itemView.findViewById(R.id.cardView_next_anime);
          anime_icon_list_item=(ImageView)itemView.findViewById(R.id.anime_icon_list_item);
          title_text_anime=(TextView)itemView.findViewById(R.id.title_text_anime);
          uploadedon=(TextView)itemView.findViewById(R.id.uploadedon);


      }
  }
    public AnimeAdapters(Context mContext, List<AnimeClass> animeClassList){
        this.mContext=mContext;
        this.animeClassList=animeClassList;
    }
    @Override
    public AnimeAdapters.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimeAdapters.MyViewHolder holder, int position) {
        AnimeClass animeClass=animeClassList.get(position);
         String title=animeClass.getTitle();
         String uploaded=animeClass.getUploaded();
         String thumbnail=animeClass.getThumbnail();
         final String url=animeClass.getUrl();
        if(title!=null){
            String text="Anime Title: "+title;
            holder.title_text_anime.setText(title);
        }if(uploaded!=null){
            String text="Uploded On: "+uploaded;
                holder.uploadedon.setText(text);
        }if(thumbnail!=null){
            try{
                Picasso.with(mContext).load(thumbnail).resize(100,120).centerCrop().into(holder.anime_icon_list_item);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        holder.cardView_next_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, AnimeDescriptionActivity.class);
                AnimeDescriptionHolderClass.getHolderClass().setUrl(url);
                mContext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return animeClassList.size();
    }
}
