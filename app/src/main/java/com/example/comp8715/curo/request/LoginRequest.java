package com.example.comp8715.curo.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ray on 9/08/16.
 */
public class LoginRequest extends StringRequest{
//    private static final String Login_REQUEST_URL = "http://curoaustest.net23.net/app_login.php";
    private static final String Login_REQUEST_URL = "http://curoaus.pe.hu/app_login.php";
    private Map<String, String> params;

    /**
     * Constructor for Class LoginRequest. It will be instantiated when logging.
     * @param email
     * @param password
     * @param listener
     */
    public LoginRequest (String email, String password, Response.Listener<String> listener) {
        super(Method.POST, Login_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
