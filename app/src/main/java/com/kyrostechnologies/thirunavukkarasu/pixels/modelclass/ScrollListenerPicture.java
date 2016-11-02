package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Thirunavukkarasu on 01-11-2016.
 */

public abstract class ScrollListenerPicture extends RecyclerView.OnScrollListener {
    public static String TAG=ScrollListenerPicture.class.getSimpleName();
    private int previousTotal=0;
    private boolean loading=true;
    private int visibleThreshold=5;
    int firstvisibleitem,visibleitemcount,totalitemcount;
    private int current_page=1;
    private LinearLayoutManager mLinearLayoutManager;
    public ScrollListenerPicture(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleitemcount=recyclerView.getChildCount();
        totalitemcount=mLinearLayoutManager.getItemCount();
        firstvisibleitem=mLinearLayoutManager.findFirstVisibleItemPosition();
        if(loading){
            if(totalitemcount>previousTotal){
                loading=false;
                previousTotal=totalitemcount;
            }
        }
    if(!loading&&(totalitemcount-visibleitemcount)<=(firstvisibleitem+visibleThreshold)){
        current_page++;
        onLoadMore(current_page);
        loading=true;
    }



    }
    public abstract void onLoadMore(int current_page);
    public void reset(int previousTotal, boolean loading) {
        this.previousTotal = previousTotal;
        this.loading = loading;
    }
}
