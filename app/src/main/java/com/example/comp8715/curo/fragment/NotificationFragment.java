package com.example.comp8715.curo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.adapter.ExpandableRecyclerAdapter;
import com.example.comp8715.curo.adapter.JobAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 25/08/16.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recycler;
    private List<JobAdapter.JobListItem> items;
    private Context context;
    private JobAdapter adapter;

    public NotificationFragment() {}

    public NotificationFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.job_recyclerview2, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
        recycler = (RecyclerView) getView().findViewById(R.id.job_recycler2);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        initializeData();
        initializeAdapter();
    }

    private void initializeData() {
        items = StaticTool.unreadJobs;
    }

    private void initializeAdapter() {
        adapter = new JobAdapter(context, items);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        adapter.expandAll();
        recycler.setAdapter(adapter);
    }
}
