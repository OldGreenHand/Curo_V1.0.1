package com.example.comp8715.curo.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ray on 9/08/16.
 */
public class RegisterRequest extends StringRequest {
//    private static final String REGISTER_REQUEST_URL = "http://curoaustest.net23.net/app_register.php";
    private static final String REGISTER_REQUEST_URL = "http://curoaus.pe.hu//app_register.php";
    private Map<String, String> params;

    //    public RegisterRequest (String firstname, String lastname, String email, String password,
//                            String phone, String companyname, Response.Listener<String> listener) {
//        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
//        params = new HashMap<>();
//        params.put("firstname", firstname);
//        params.put("lastname", lastname);
//        params.put("email", email);
//        params.put("password", password);
//        params.put("phone", phone);
//        params.put("agentNo", agentNo);
//        params.put("companyname", companyname);
//    }

    /**
     * Constructor for Class RegisterRequest. It will be instantiated when registering.
     * @param userID
     * @param firstname
     * @param lastname
     * @param email
     * @param password
     * @param phone
     * @param agentNo
     * @param listener
     */
    public RegisterRequest(String userID, String firstname, String lastname, String email, String password,
                           int phone, String agentNo, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("email", email);
        params.put("password", password);
        params.put("phone", phone + "");
        params.put("agentNo", agentNo);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
