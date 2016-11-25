package com.example.comp8715.curo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText editFistName = (EditText) findViewById(R.id.editFirstName);
        final EditText editLastName = (EditText) findViewById(R.id.editLastName);
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final Spinner spinnerAgentNo = (Spinner) findViewById(R.id.spinnerAgentNo);

        final EditText editCompany = (EditText)findViewById(R.id.editCompany);

        /**
         * Listen to button "Register", the response is to send a RegisterRequest
         * including user's personal information  to database.
         */
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        assert bRegister != null;
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = String.valueOf(System.currentTimeMillis());
                System.out.println("Time : " + userID);
                assert editFistName != null;
                final String firstname = editFistName.getText().toString();
                assert editLastName != null;
                final String lastname = editLastName.getText().toString();
                assert editEmail != null;
                final String email = editEmail.getText().toString();
                assert editPassword != null;
                final String password = editPassword.getText().toString();
                assert editPhone != null;
                final int mobile = Integer.parseInt(editPhone.getText().toString());
                Toast.makeText(RegisterActivity.this, "Register Successfully" + firstname, Toast.LENGTH_SHORT).show();

                final String agentNo = String.valueOf(spinnerAgentNo.getSelectedItem());
//                final String companyname = editCompany.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("response: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println("success: " + success);

                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("This Email has already been registered! \nPlease try another Email!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userID, firstname, lastname, email, password, mobile, agentNo, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}
