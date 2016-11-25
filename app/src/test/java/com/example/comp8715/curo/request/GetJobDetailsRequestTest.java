package com.example.comp8715.curo.request;

import android.test.AndroidTestCase;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by MacBook on 5/10/2016.
 */
public class GetJobDetailsRequestTest extends AndroidTestCase {
    public void setUp() throws Exception {
        getJobDetailsRequestTest();
    }

    @Test
    public void getJobDetailsRequestTest() throws Exception {
        // request bid to database
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    assertEquals(success, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        GetJobDetailsRequest getJobDetailsRequest = new GetJobDetailsRequest("775721", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getJobDetailsRequest);
    }
}
