package com.example.comp8715.curo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.request.BidJobsRequest;
import com.example.comp8715.curo.request.LoginRequest;
import com.example.comp8715.curo.request.MarkJobReaded;
import com.example.comp8715.curo.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JobEditActivity extends AppCompatActivity {
    private JSONObject jobDetails;
    private HashMap<String, String> bidInfo;
    private Button bid_btn;
    private TextView bidInfo_TV;
    private EditText bidAmount_ET;
    private LinearLayout bidBlock_LL;
    private Intent refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_edit);

        CardView mCardView = (CardView) findViewById(R.id.cardview);
        assert mCardView != null;
        mCardView.setRadius(55);
        mCardView.setElevation(100);

        // get current job details and status in JSON object
        jobDetails = StaticTool.jobDetails;

        // refresh activity
        refresh = new Intent(JobEditActivity.this, JobEditActivity.class);


        try {
            // mark current job be read when current job is not read
            if(jobDetails.getJSONObject("0").getString("rnf").equals("0")) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println("Job Edit Activity mark job read --- " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // mark current job be read
                MarkJobReaded markJobReaded = new MarkJobReaded(StaticTool.jobID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(JobEditActivity.this);
                queue.add(markJobReaded);
            }

            // set job details information into right place
            setAllTextViewTextContent();

            String job_status = jobDetails.getJSONObject("0").getString("status");
            // if current job status is 1 then check current job is bid avaliable or not
            if (job_status.equals("1")) {
                // get current job bid information and initial bid job related parameters
                construcBidInfo();

                // set bid information and check current job is avaliable bid or not
                bidAvaliable();
                bid_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String bidAmount = bidAmount_ET.getText().toString();
                        System.out.println("Curo - BidJobRequest -- userID = (" + StaticTool.userID + ")" +
                                " bidAmount = " + bidAmount + " jobID = " + StaticTool.jobID);

                        // request bid to database
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d(HomeActivity.TAG, "Curo - BidJobRequest -- JSON response --\n" + response);
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(JobEditActivity.this);
                                        builder.setMessage("Bid Successfully!")
                                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        bidInfo_TV.setText("Your are bit current job with: " + bidAmount);
                                                        bid_btn.setEnabled(false);
                                                    }
                                                })
                                                .create()
                                                .show();
                                    } else {
                                        bidInfo_TV.setText("Sorry, current job is not bid avaliable!");
                                        bid_btn.setEnabled(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        BidJobsRequest bidJobsRequest = new BidJobsRequest(StaticTool.userID, bidAmount, StaticTool.jobID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(JobEditActivity.this);
                        queue.add(bidJobsRequest);
                    }
                });

                // clear edit textview focus
                bidAmount_ET.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bidAmount_ET.getWindowToken(), 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // construct of bid information about current job
    private void construcBidInfo() throws JSONException {
        // initial bid job related parameters and set visiable
        bid_btn = (Button) findViewById(R.id.job_edit_bid_btn);
        bidInfo_TV = (TextView) findViewById(R.id.job_edit_bidInfo);
        bidAmount_ET = (EditText) findViewById(R.id.job_edit_bidAmount);
        bidBlock_LL = (LinearLayout) findViewById(R.id.job_edit_bid_layout);
        assert bid_btn != null;
        assert bidInfo_TV != null;
        assert bidAmount_ET != null;
        assert bidBlock_LL != null;
        bid_btn.setVisibility(View.VISIBLE);
        bidInfo_TV.setVisibility(View.VISIBLE);
        bidAmount_ET.setVisibility(View.VISIBLE);
        bidBlock_LL.setVisibility(View.VISIBLE);

        // construct of bid information
        bidInfo = new HashMap<>();
        bidInfo.put("bidderID1", jobDetails.getJSONObject("0").getString("bidderID1"));
        bidInfo.put("bidderID2", jobDetails.getJSONObject("0").getString("bidderID2"));
        bidInfo.put("bidderID3", jobDetails.getJSONObject("0").getString("bidderID3"));
        bidInfo.put("bid1", jobDetails.getJSONObject("0").getString("bid1"));
        bidInfo.put("bid2", jobDetails.getJSONObject("0").getString("bid2"));
        bidInfo.put("bid3", jobDetails.getJSONObject("0").getString("bid3"));
    }

    // check current job bid status to current user
    private void bidAvaliable() {
        String userID = StaticTool.userID;
        String bid = "";

        // set enable to button when user already bid current job
        if(bidInfo.containsValue(userID)) {
            bid_btn.setEnabled(false);

            // get current user bid amount
            if(bidInfo.get("bidderID1").equals(userID)) bid = bidInfo.get("bid1");
            else if (bidInfo.get("bidderID2").equals(userID)) bid = bidInfo.get("bid2");
            else if (bidInfo.get("bidderID3").equals(userID)) bid = bidInfo.get("bid3");

            // set bid info to textview
            bidInfo_TV.setText("Your are bit current job with: " + bid);
        } else if (bidInfo.get("bidderID1").equals("null") || bidInfo.get("bidderID2").equals("null")
                || bidInfo.get("bidderID3").equals("null")) {
            int count = 0;
            if(!bidInfo.get("bidderID1").equals("null")) count++;
            if(!bidInfo.get("bidderID2").equals("null")) count++;
            if(!bidInfo.get("bidderID3").equals("null")) count++;

            bidInfo_TV.setText("Current bid (" + count + " / 3)!");
        } else {
            bid_btn.setEnabled(false);
            bidInfo_TV.setText("Sorry, cuurent job is not bid avaliable!");
        }
    }

    private void setAllTextViewTextContent() throws JSONException {
        // set jobID
        TextView jobID_TV = (TextView) findViewById(R.id.job_edit_jobID);
        if (jobID_TV != null) jobID_TV.setText(jobDetails.getJSONObject("0").getString("jobID"));

        // set job type
        TextView jobType_TV = (TextView) findViewById(R.id.job_edit_jobType);
        if (jobType_TV != null) jobType_TV.setText(jobDetails.getJSONObject("0").getString("jobType"));

        // set job location
        TextView jobLocation_TV = (TextView) findViewById(R.id.job_edit_location);
        if (jobLocation_TV != null) jobLocation_TV.setText(jobDetails.getJSONObject("0").getString("location"));

        // set job jobPriority
        TextView jobPriority_TV = (TextView) findViewById(R.id.job_edit_priority);
        if (jobPriority_TV != null) jobPriority_TV.setText(jobDetails.getJSONObject("0").getString("jobPriority"));

        // set job quantity
        TextView jobQuantity_TV = (TextView) findViewById(R.id.job_edit_quantity);
        if (jobQuantity_TV != null) jobQuantity_TV.setText(jobDetails.getJSONObject("0").getString("quantity"));

        // set job dueTime
        TextView jobDueTime_TV = (TextView) findViewById(R.id.job_edit_dueTime);
        if (jobDueTime_TV != null) jobDueTime_TV.setText(jobDetails.getJSONObject("0").getString("dueTime"));

        // set job description
        TextView jobDescriptionTV = (TextView) findViewById(R.id.job_edit_jobDescription);
        if (jobDescriptionTV != null) jobDescriptionTV.setText(jobDetails.getJSONObject("0").getString("jobDescription"));

        // set job notes
        TextView jobNotes_TV = (TextView) findViewById(R.id.job_edit_notes);
        if (jobNotes_TV != null) jobNotes_TV.setText(jobDetails.getJSONObject("0").getString("notes"));
    }

    @Override
    // Listen to Back key
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, HomeActivity.class));
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
