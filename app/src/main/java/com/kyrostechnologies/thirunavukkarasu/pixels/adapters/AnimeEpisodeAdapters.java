package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.activity.PlayAnimeActivity;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.AnimeEpisodeTitile;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 14-11-2016.
 */

public class AnimeEpisodeAdapters extends RecyclerView.Adapter<AnimeEpisodeAdapters.MyViewHolder> {
    private Context mContext;
    private List<String >animelist;
    public AnimeEpisodeAdapters(Context mContext, List<String>animelist){
     this.mContext=mContext;
        this.animelist=animelist;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView episode_item_list;
        public CardView cardview_anime_list;

        public MyViewHolder(View itemView) {
            super(itemView);
            episode_item_list=(TextView)itemView.findViewById(R.id.episode_item_list);
            cardview_anime_list=(CardView)itemView.findViewById(R.id.cardview_anime_list);
        }
    }
    @Override
    public AnimeEpisodeAdapters.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_episode_textview,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimeEpisodeAdapters.MyViewHolder holder, int position) {
        final String value=animelist.get(position);
       int size= animelist.size();
        final int total=size-position;
        if(total==0){
            String valussss="Episode: "+"1";
            holder.episode_item_list.setText(valussss);

        }else{
            String bb="Episode: "+total;
            holder.episode_item_list.setText(bb);

        }
        holder.cardview_anime_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext,PlayAnimeActivity.class);
                AnimeEpisodeTitile.getHolder().setPlayurl(value);
                if(total==0){
                    String valussss=" Episode: "+"1";
                AnimeEpisodeTitile.getHolder().setEpisodeNo(valussss);
                }else{
                    String bb=" Episode: "+total;
                    AnimeEpisodeTitile.getHolder().setEpisodeNo(bb);

                }
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return animelist.size();
    }
}
