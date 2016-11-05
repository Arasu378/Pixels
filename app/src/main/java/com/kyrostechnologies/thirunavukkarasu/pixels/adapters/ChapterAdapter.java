package com.kyrostechnologies.thirunavukkarasu.pixels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ChaptersClass;

import java.util.List;

/**
 * Created by Thirunavukkarasu on 04-11-2016.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {
    private Context mContext;
    private List<ChaptersClass>chapterList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView chapter_number;

        public MyViewHolder(View itemView) {
            super(itemView);
            chapter_number=(TextView)itemView.findViewById(R.id.chapter_number);
        }
    }
    public ChapterAdapter(Context mContext, List<ChaptersClass>chapterList){
        this.mContext=mContext;
        this.chapterList=chapterList;

    }
    @Override
    public ChapterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_fragment_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterAdapter.MyViewHolder holder, int position) {
        ChaptersClass m=chapterList.get(position);
         String ChapterName=m.getChapterName();
         String ChapterID=m.getChapterID();
         String ChapterDate=m.getChapterDate();
        if(ChapterName!=null){
            holder.chapter_number.setText(ChapterName);
        }

    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
