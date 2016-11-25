package com.example.comp8715.curo.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacBook on 21/09/2016.
 */
public class GetJobDetailsRequest extends StringRequest {
    //    private static final String Login_REQUEST_URL = "http://curoaustest.net23.net/app_db_bid_job.php";
    private static final String Login_REQUEST_URL = "http://curoaus.pe.hu/app_db_get_job_details.php";
    private Map<String, String> params;

    /**
     * Constructor for Class LoginRequest. It will be instantiated when logging.
     * @param jobID
     * @param listener
     */

    public GetJobDetailsRequest (String jobID, Response.Listener<String> listener) {
        super(Method.POST, Login_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("jobID", jobID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
