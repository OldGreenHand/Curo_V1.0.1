package com.example.comp8715.curo.request;


import android.test.AndroidTestCase;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by MacBook on 4/10/2016.
 */
public class QueryJobRequestTest extends AndroidTestCase {

    @Test
    public void queryJobRequestTest() throws Exception {
        // request bid to database
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("JSON response: ", jsonResponse.toString());
                    boolean success = jsonResponse.getBoolean("success");
                    assertEquals(success, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        QueryJobRequest bidJobsRequest = new QueryJobRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(bidJobsRequest);
    }
}
