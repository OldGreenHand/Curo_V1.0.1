package com.example.comp8715.curo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Delayed;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;
    private final static String USERNAME_KEY = "username";
    private final static String PASSWORD_KEY = "password";
    private final static String SAVED_KEY = "saved";
    private boolean isSaved = false;

    private EditText editEmail;
    private EditText editPassword;
    private CheckBox cbRemember;
    private Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editLoginPassword);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        bLogin = (Button)findViewById(R.id.bLogin);

        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginEditor = loginPrefs.edit();

        final TextView textRegisterView = (TextView)findViewById(R.id.textRegisterLink);
        assert textRegisterView != null;
        textRegisterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        //Check if there is saved pair of username and password and get it if there is.
        isSaved = loginPrefs.getBoolean(SAVED_KEY, false);
        if (isSaved) {
            fillData();
        }

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    /**
                     * Listen to button "Login", the response is to send a LoginRequest to database.
                     */
                    assert bLogin != null;
                    bLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String username = editEmail.getText().toString();
                            final String password = editPassword.getText().toString();

                            System.out.println("Curo - RegisterRequest -- Login -- username = (" + username + ") password = " + password);
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d(HomeActivity.TAG, "Curo - LoginRequest -- JSON response --\n" + response);
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            //Store username and password if checkbox is checked.
                                            if (cbRemember.isChecked()) {
                                                isSaved = true;
                                                saveData(username, password);
                                            } else {
                                                loginEditor.clear();
                                                loginEditor.apply();
                                                isSaved = false;
                                            }

                                            String userID = jsonResponse.getJSONObject("0").getString("userID");
                                            String firstname = jsonResponse.getJSONObject("0").getString("firstname");
                                            String lastname = jsonResponse.getJSONObject("0").getString("lastname");
                                            String agentNo = jsonResponse.getJSONObject("0").getString("agentNo");

                                            boolean result = queryJob();

                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            StaticTool.userInfo = jsonResponse;
                                            StaticTool.userID = userID;
                                            StaticTool.firstName = firstname;
                                            StaticTool.lastName = lastname;
                                            StaticTool.agentNo = agentNo;

                                            Log.d(HomeActivity.TAG, "Curo - LoginRequest -- JSON success? -- " + success
                                                + "\n   agentNo: " + agentNo);
                                            if (result) {
                                                sleep(2000);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setMessage("Login Failed")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                            queue.add(loginRequest);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    private void fillData() {
        String usr = loginPrefs.getString(USERNAME_KEY, "");
        String pwd = loginPrefs.getString(PASSWORD_KEY, "");
        editEmail.setText(usr);
        editPassword.setText(pwd);
        System.out.println("Account (Username: " + usr + "; Password: " + pwd + ".) is used!");
    }

    private void saveData(String username, String password) {
        loginEditor.putString(USERNAME_KEY, username);
        loginEditor.putString(PASSWORD_KEY, password);
        loginEditor.putBoolean(SAVED_KEY, isSaved);
        loginEditor.commit();
        System.out.println("Account (Username: " + username + "; Password: " + password + ".) is saved!");
    }

    private boolean queryJob() {
        StaticTool staticTool = new StaticTool(this);
        return staticTool.queryAllJob();
    }

}
