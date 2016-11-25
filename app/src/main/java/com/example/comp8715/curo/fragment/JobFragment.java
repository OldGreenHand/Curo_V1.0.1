package com.example.comp8715.curo.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.adapter.ExpandableRecyclerAdapter;
import com.example.comp8715.curo.adapter.JobAdapter;

import java.util.ArrayList;
import java.util.List;

public class JobFragment extends Fragment {


    private RecyclerView recycler;
    private List<JobAdapter.JobListItem> items;
    private Context context;
    private JobAdapter adapter;


    public JobFragment() {
        items = new ArrayList<>();
    }

    // JobFragment constructor
    @SuppressLint("ValidFragment")
    public JobFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the job_recyclerview for this fragment
        return inflater.inflate(
                R.layout.job_recyclerview, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        // getter and setter the recycler view content
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
        recycler = (RecyclerView) getView().findViewById(R.id.job_recycler);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        initializeData();
        initializeAdapter();
    }

    // construct the jobs content to be shown
    private void initializeData() {
        items = StaticTool.all_jobs;
    }

    private void initializeAdapter() {
        adapter = new JobAdapter(context, items);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setAdapter(adapter);
    }

}
