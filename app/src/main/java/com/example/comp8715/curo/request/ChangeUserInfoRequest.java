package com.example.comp8715.curo.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ray on 6/10/16.
 */
public class ChangeUserInfoRequest extends StringRequest {


    private static final String REGISTER_REQUEST_URL = "http://curoaus.pe.hu//app_change_user_info.php";
    private Map<String, String> params;

    /**
     * Constructor for Class RegisterRequest. It will be instantiated when registering.
     * @param userID
     * @param firstname
     * @param lastname
     * @param phone
     * @param mobile
     * @param address
     * @param suburb
     * @param postcode
     * @param companyName
     * @param companyType
     * @param tradeCategory
     * @param ABN
     * @param listener
     */
    public ChangeUserInfoRequest(String userID, String firstname, String lastname, int phone, int mobile,
                                 String address, String suburb, int postcode, String companyName,
                                 String companyType, String tradeCategory, int ABN, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phone", phone + "");
        params.put("mobile", mobile + "");
        params.put("address", address);
        params.put("suburb", suburb);
        params.put("postcode", postcode + "");
        params.put("companyName", companyName);
        params.put("companyType", companyType);
        params.put("tradeCategory", tradeCategory);
        params.put("ABN", ABN + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
