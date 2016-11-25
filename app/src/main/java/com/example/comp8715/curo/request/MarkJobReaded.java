package com.example.comp8715.curo.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacBook on 4/10/2016.
 */
public class MarkJobReaded extends StringRequest {
    private static final String Login_REQUEST_URL = "http://curoaus.pe.hu/app_job_mark_read.php";
    private Map<String, String> params;

    /**
     * Constructor for Class DBChangeOrNotCheckRequest. It will be instantiated
     * when checking if the database changed once a 5 seconds.
     * @param jobID
     * @param listener
     */
    public MarkJobReaded (String jobID, Response.Listener<String> listener) {
        super(Method.POST, Login_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("jobID", jobID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
