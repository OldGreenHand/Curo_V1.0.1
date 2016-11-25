package com.example.comp8715.curo.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacBook on 27/08/2016.
 */
public class QueryJobRequest extends StringRequest {
    private static final String Login_REQUEST_URL = "http://curoaus.pe.hu/app_db_query_job.php";
    private Map<String, String> params;

    /**
     * @param listener
     */
    public QueryJobRequest (Response.Listener<String> listener) {
        super(Method.POST, Login_REQUEST_URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
