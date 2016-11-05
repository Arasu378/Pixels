package com.kyrostechnologies.thirunavukkarasu.pixels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.adapters.ChapterAdapter;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.ChaptersClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thirunavukkarasu on 04-11-2016.
 */

public class ChapterFragment extends Fragment {
    private RecyclerView chapter_recyclerview;
    private ChapterAdapter adapter;
    private List<ChaptersClass>chaptersClassList=new ArrayList<ChaptersClass>();
    public ChapterFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.chapter_fragment,container,false);
        chapter_recyclerview=(RecyclerView)view.findViewById(R.id.chapter_recyclerview);
        return view;
    }
}
