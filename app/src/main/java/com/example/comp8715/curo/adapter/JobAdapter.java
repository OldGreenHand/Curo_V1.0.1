package com.example.comp8715.curo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.activity.HomeActivity;
import com.example.comp8715.curo.activity.JobEditActivity;
import com.example.comp8715.curo.request.LoginRequest;

import org.json.JSONObject;

import java.util.List;

public class JobAdapter extends ExpandableRecyclerAdapter<JobAdapter.JobListItem> {
    public static final int TYPE_JOB_DETAILS = 1001;

    public JobAdapter(Context context, List<JobListItem> items) {
        super(context);
        setItems(items);
    }

    public static class JobListItem extends ExpandableRecyclerAdapter.ListItem {
        public String TypeName = "empty";
        public String JobID = "empty";
        public String JobContent = "empty";

        public JobListItem(String group) {
            super(TYPE_HEADER);
            TypeName = group;
        }

        public JobListItem(String first, String last) {
            super(TYPE_JOB_DETAILS);
            JobID = first;
            JobContent = last;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.job_type_arrow));
            name = (TextView) view.findViewById(R.id.job_type_name);
        }

        public void bind(int position) {
            super.bind(position);
            name.setText(visibleItems.get(position).TypeName);
        }
    }

    public class JobViewHolder extends ViewHolder {
        TextView job_id;
        TextView job_details;

        public JobViewHolder(View view) {
            super(view);
            job_id = (TextView) view.findViewById(R.id.job_deatails_ID);
            job_details = (TextView) view.findViewById(R.id.job_details_content);
        }

        public void bind(int position) {
            job_id.setText(visibleItems.get(position).JobID);
            job_details.setText(visibleItems.get(position).JobContent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.job_type, parent));
            case TYPE_JOB_DETAILS:
            default:
                return new JobViewHolder(inflate(R.layout.job_details, parent));
        }
    }

    @Override
    public void onBindViewHolder(final ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_JOB_DETAILS:
            default:
                ((JobViewHolder) holder).bind(position);
                ((JobViewHolder) holder).job_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            StaticTool staticTool = new StaticTool(mContext);
                            String jobID = ((JobViewHolder) holder).job_id.getText().toString();
                            Boolean result = staticTool.queryJobDetails(jobID);
                            if (result) {
                                Thread.sleep(2 * 1000);
                                StaticTool.jobID = jobID;
                                Intent intent = new Intent(mContext, JobEditActivity.class);
                                mContext.startActivity(intent);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }
}
