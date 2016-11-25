package com.example.comp8715.curo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.request.ChangeUserInfoRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ChangeUserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        final EditText firstname_ET = (EditText)findViewById(R.id.edit_firstname);
        final EditText lastname_ET = (EditText)findViewById(R.id.edit_lastname);
        final EditText telephone_ET = (EditText)findViewById(R.id.edit_telephone);
        final EditText mobile_ET = (EditText)findViewById(R.id.edit_mobile);
        final EditText address_ET = (EditText)findViewById(R.id.edit_address);
        final EditText suburb_ET = (EditText)findViewById(R.id.edit_suburb);
        final EditText postcode_ET = (EditText)findViewById(R.id.edit_postcode);
        final EditText company_ET = (EditText)findViewById(R.id.edit_company);
        final EditText companytype_ET = (EditText)findViewById(R.id.edit_companytype);
        final EditText tradecategory_ET = (EditText)findViewById(R.id.edit_tradecategory);
        final EditText abn_ET = (EditText)findViewById(R.id.edit_abn);

        Intent it = getIntent();
        ArrayList<String> infoArray = new ArrayList<>();
        Serializable se= it.getSerializableExtra("info");

        /**
         * Listen to button "Confirm", the response is to send a ChangeUserInfoRequest
         */
        final Button bConfirm = (Button) findViewById(R.id.bConfirm);
        assert bConfirm != null;
        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user ID
                String userID = StaticTool.userID;
                // get First Name
                assert firstname_ET != null;
                final String firstname = firstname_ET.getText().toString();
                // get Last Name
                assert lastname_ET != null;
                String lastname = lastname_ET.getText().toString();
                // get Telephone
                assert telephone_ET != null;
                int phone = Integer.parseInt(telephone_ET.getText().toString());
                // get Mobile
                assert mobile_ET != null;
                int mobile = Integer.parseInt(mobile_ET.getText().toString());
                // get Address
                assert address_ET != null;
                String address = address_ET.getText().toString();
                // get Suburb
                assert suburb_ET != null;
                String suburb = suburb_ET.getText().toString();
                // get Postcode
                assert postcode_ET != null;
                int postcode = Integer.parseInt(postcode_ET.getText().toString());
                // get Company
                assert company_ET != null;
                String companyName = company_ET.getText().toString();
                // get Company Type
                assert companytype_ET != null;
                String companyType = companytype_ET.getText().toString();
                // get Trade Category
                assert tradecategory_ET != null;
                String tradeCategory = tradecategory_ET.getText().toString();
                // get ABN
                assert abn_ET != null;
                int ABN = Integer.parseInt(abn_ET.getText().toString());
                Toast.makeText(ChangeUserInfoActivity.this, "Confirm Button Clicked! " + firstname, Toast.LENGTH_SHORT).show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("response: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println("success: " + success);

                            if (success) {
                                Toast.makeText(ChangeUserInfoActivity.this, "Change your info Successfully! " + firstname, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangeUserInfoActivity.this, HomeActivity.class);
                                ChangeUserInfoActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUserInfoActivity.this);
                                builder.setMessage("Change info failed! \nPlease try again!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ChangeUserInfoRequest changeRequest = new ChangeUserInfoRequest(userID, firstname, lastname, phone, mobile, address, suburb, postcode,
                                                                    companyName, companyType, tradeCategory, ABN, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChangeUserInfoActivity.this);
                queue.add(changeRequest);
            }
        });
    }

}
