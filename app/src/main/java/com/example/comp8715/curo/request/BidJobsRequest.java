package com.example.comp8715.curo.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yongqiang Wang on 21/09/2016.
 */
public class BidJobsRequest extends StringRequest{
    //    private static final String Login_REQUEST_URL = "http://curoaustest.net23.net/app_db_bid_job.php";
    private static final String Login_REQUEST_URL = "http://curoaus.pe.hu/app_db_bid_job.php";
    private Map<String, String> params;

    /**
     * Constructor for Class LoginRequest. It will be instantiated when logging.
     * @param userID
     * @param bidAmount
     * @param poNum
     * @param listener
     */

    public BidJobsRequest (String userID, String bidAmount, String poNum, Response.Listener<String> listener) {
        super(Method.POST, Login_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("bidAmount", bidAmount);
        params.put("poNum", poNum);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
