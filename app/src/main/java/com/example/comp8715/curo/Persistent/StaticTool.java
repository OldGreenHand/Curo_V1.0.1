package com.example.comp8715.curo.Persistent;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.comp8715.curo.activity.HomeActivity;
import com.example.comp8715.curo.adapter.JobAdapter;
import com.example.comp8715.curo.request.GetJobDetailsRequest;
import com.example.comp8715.curo.request.QueryJobRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yongqiang Wang on 23/08/2016.
 */
public class StaticTool  extends AppCompatActivity {
    public static int JobNum = 0;

    private Context context;
    private int jobDescMaxLength = 20;
    public static String userID = "";
    public static String firstName = "";
    public static String lastName = "";
    public static String jobID = "";
    public static String agentNo = "";

    public static JSONObject jobDetails;

    public static JSONObject userInfo;


    /** state different job type list
     *  job_status1 --> New Jobs
     *  job_status2 --> Bidded Jobs
     *  job_status3 --> Jobs in Progress
     *  job_status4 --> Jobs Finished
     *  job_status5 --> Jobs invoiced
     *  job_status6 --> Invoices Overdue
     *  */
    private static List<JobAdapter.JobListItem> job_status1;
    private static List<JobAdapter.JobListItem> job_status2;
    private static List<JobAdapter.JobListItem> job_status3;
    private static List<JobAdapter.JobListItem> job_status4;
    private static List<JobAdapter.JobListItem> job_status5;
    private static List<JobAdapter.JobListItem> job_status6;
    public static List<JobAdapter.JobListItem> unreadJobs;

    public static List<JobAdapter.JobListItem> all_jobs;

    public StaticTool(Context context) {
        this.context = context;
    }

    public boolean queryAllJob() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    Log.d(HomeActivity.TAG, "query all jobs success: " + success);
                    if (success) {
                        constructJob(jsonResponse);
                    } else {
                        Log.d(HomeActivity.TAG, " query all jobs fails!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        QueryJobRequest queryJobRequest = new QueryJobRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(queryJobRequest);
        return true;
    }

    // based on jobID query job details from DB
    public boolean queryJobDetails(String jobID) {
        System.out.println("Start query job: " + jobID);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jobDetails= new JSONObject(response);
                    boolean success = jobDetails.getBoolean("success");
                    Log.d(HomeActivity.TAG, "query job details success: " + success);
                    Log.d("StaticTool ---- ", "job details: " + jobDetails.toString());
                    if (!success) {
                        Log.d(HomeActivity.TAG, " query job details fails!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetJobDetailsRequest getJobDetailsRequest = new GetJobDetailsRequest(jobID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(getJobDetailsRequest);
        return true;
    }

    /**
     * @param  response JSONObject contains all jobs query from web database
     * */
    public void constructJob(JSONObject response) throws JSONException {
        System.out.println(response.toString());
        System.out.println("Start construction!");
        job_status1 = new ArrayList<>();
        job_status1.add(new JobAdapter.JobListItem("New Jobs"));

        job_status2 = new ArrayList<>();
        job_status2.add(new JobAdapter.JobListItem("Bidded Jobs"));

        job_status3 = new ArrayList<>();
        job_status3.add(new JobAdapter.JobListItem("Jobs in Progress"));

        job_status4 = new ArrayList<>();
        job_status4.add(new JobAdapter.JobListItem("Jobs Finished"));

        job_status5 = new ArrayList<>();
        job_status5.add(new JobAdapter.JobListItem("Jobs invoiced"));

        job_status6 = new ArrayList<>();
        job_status6.add(new JobAdapter.JobListItem("Invoices Overdue"));

        unreadJobs = new ArrayList<>();
        unreadJobs.add(new JobAdapter.JobListItem("Notification"));

        // iteration all jobs
        for(int i = 0; i < response.length() - 1; i++) {

            // get job details: id, description, status, bidderID1, bidderID2, bidderID3, rnf, agentNo
            String status = response.getJSONObject(String.valueOf(i)).getString("status");
            String jobID = response.getJSONObject(String.valueOf(i)).getString("jobID");
            String jobDescription = response.getJSONObject(String.valueOf(i)).getString("jobDescription");
            String rnf = response.getJSONObject(String.valueOf(i)).getString("rnf");
            String agentNo = response.getJSONObject(String.valueOf(i)).getString("agentNo");
            String bidderID1 = response.getJSONObject(String.valueOf(i)).getString("bidderID1");
            String bidderID2 = response.getJSONObject(String.valueOf(i)).getString("bidderID2");
            String bidderID3 = response.getJSONObject(String.valueOf(i)).getString("bidderID3");
            String winnerID = response.getJSONObject(String.valueOf(i)).getString("winnerID");

            // applying jobDescMaxLength
            if(jobDescription.length() > jobDescMaxLength) {
                jobDescription = jobDescription.substring(0, jobDescMaxLength) + " ...";
            }

            // filter the job based on current agentNo
            if(agentNo.equals(StaticTool.agentNo)) {
                // based on status to add jobs
                boolean isBided = bidderID1.equals(StaticTool.userID) || bidderID2.equals(StaticTool.userID)
                        || bidderID3.equals(StaticTool.userID);
                boolean isOwner = winnerID.equals(StaticTool.userID);
                switch (status) {
                    case "1":
                        if ( !bidderID1.equals(StaticTool.userID) && !bidderID2.equals(StaticTool.userID) ){
                            job_status1.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        } else {
                            job_status2.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        }
                        // add unread and unOwner jobs
                        if(rnf.equals("0")) {
                            unreadJobs.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        }
                        break;
                    case "2":
                        if (isBided) job_status2.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        break;
                    case "3":
                        if (isOwner) {
                            job_status3.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        }
                        break;
                    case "4":
                        if (isOwner) job_status4.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        break;
                    case "5":
                        if (isOwner) job_status5.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        break;
                    case "6":
                        if (isOwner) job_status6.add(new JobAdapter.JobListItem(jobID, jobDescription));
                        break;
                }

                // add unread jobs
                if(isOwner && rnf.equals("0")) {
                    unreadJobs.add(new JobAdapter.JobListItem(jobID, jobDescription));
                }
            }
        }

        // set null to empty job
        if(job_status1.size() == 1) {job_status1.add(new JobAdapter.JobListItem("", "Currently no jobs!"));}
        if(job_status2.size() == 1) {job_status2.add(new JobAdapter.JobListItem("", "Currently no jobs!"));}
        if(job_status3.size() == 1) {job_status3.add(new JobAdapter.JobListItem("", "Currently no jobs!"));}
        if(job_status4.size() == 1) {job_status4.add(new JobAdapter.JobListItem("", "Currently no jobs!"));}
        if(job_status5.size() == 1) {job_status5.add(new JobAdapter.JobListItem("", "Currently no jobs!"));}
        if(job_status6.size() == 1) {job_status6.add(new JobAdapter.JobListItem("", "Currently no jobs!"));}

        all_jobs = StaticTool.job_status1;
        all_jobs.addAll(StaticTool.job_status2);
        all_jobs.addAll(StaticTool.job_status3);
        all_jobs.addAll(StaticTool.job_status4);
        all_jobs.addAll(StaticTool.job_status5);
        all_jobs.addAll(StaticTool.job_status6);
    }
}
