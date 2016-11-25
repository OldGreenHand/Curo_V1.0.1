

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
public class MarkJobReadedTest extends AndroidTestCase {

@Test
public void setUp() throws Exception {
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
    MarkJobReaded markJobReaded = new MarkJobReaded("775721", responseListener);
    RequestQueue queue = Volley.newRequestQueue(getContext());
    queue.add(markJobReaded);
}
}
