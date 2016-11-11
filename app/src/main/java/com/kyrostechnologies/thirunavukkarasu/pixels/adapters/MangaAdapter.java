package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.activity.MangaDescriptionActivity;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaIdClass;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.MangaPictureURL;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thirunavukkarasu on 03-11-2016.
 */

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder> implements Filterable{
    private Context mContext;
    private List<MangaClass> mangaClassList;

    @Override
    public Filter getFilter() {
        return null;
    }
        public MangaAdapter(Context mContext,List<MangaClass>mangaClassList){
            this.mangaClassList=mangaClassList;
            this.mContext=mContext;
        }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardview_manga;
        public TextView manga_title;
        public ImageView manga_picture_list_item;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview_manga=(CardView)itemView.findViewById(R.id.cardview_manga);
            manga_title=(TextView)itemView.findViewById(R.id.manga_title);
            manga_picture_list_item=(ImageView)itemView.findViewById(R.id.manga_picture_list_item);

        }

    }
    @Override
    public MangaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.manga_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MangaAdapter.ViewHolder holder, int position) {
        MangaClass mangaClass=mangaClassList.get(position);
         String MangaTitle=mangaClass.getMangaTitle();
         final String Id=mangaClass.getId();
        String im=mangaClass.getMangaPicture();
        String urlpicture= MangaPictureURL.PICTUREURL+im;
        if(MangaTitle!=null){
            holder.manga_title.setText(MangaTitle);
        }
        holder.cardview_manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, MangaDescriptionActivity.class);
                MangaIdClass.getHolder().setId(Id);
                mContext.startActivity(i);
            }
        });
        try{
            Picasso.with(mContext).load(urlpicture).resize(50,50).centerCrop().into(holder.manga_picture_list_item);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mangaClassList.size();
    }
    public void setFilter(List<MangaClass>mangaClasses){
        mangaClassList=new ArrayList<>();
        mangaClassList.addAll(mangaClasses);
        notifyDataSetChanged();
    }
}
