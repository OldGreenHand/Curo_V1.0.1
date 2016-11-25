package com.example.comp8715.curo.fragment;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.activity.ChangeUserInfoActivity;
import com.example.comp8715.curo.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ray on 21/09/16.
 */
public class UserFragment extends Fragment {

    ScrollView myscrollLinearlayout;

    RelativeLayout mainheadview;  //Top view about user info

    RelativeLayout mainactionbar; //Top menubar

    ImageView userinfo_topbar;

    ImageButton bRevise;

    JSONObject userInfo = StaticTool.userInfo;

    int Y;
    int position = 0;       //distance from Y to the position where dragging Linearlayout
    int scrollviewdistancetotop = 0;   //height of headView
    int menubarHeight = 0;
    int launchHeight = 0; //height of the motion which is required to be launched
    float scale;    //density
    int headViewPosition = 0;
    static boolean flag = true;
    static boolean topmenuflag = true;
    ArrayList<String> infoArray = new ArrayList<>();

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.userinfo_layout, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        initView();
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bRevise = (ImageButton)getView().findViewById(R.id.userinfo_revise);
        bRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), ChangeUserInfoActivity.class);
                it.putExtra("info", infoArray);
                startActivity(it);
            }
        });
    }

    private void initView() {
        userinfo_topbar = (ImageView)getView().findViewById(R.id.userinfo_topbar);

        //get density of metrics
        scale = this.getResources().getDisplayMetrics().density;
        mainheadview = (RelativeLayout) getView().findViewById(R.id.mainheadview);
        mainactionbar = (RelativeLayout) getView().findViewById(R.id.mainactionbar);

        menubarHeight = (int) (55 * scale);
        launchHeight = (int) (110 * scale);

        scrollviewdistancetotop = (int) ((150 )*scale);
        position = scrollviewdistancetotop;
        myscrollLinearlayout = (ScrollView) getView().findViewById(R.id.myscrollLinearlayout);
        myscrollLinearlayout.setY(scrollviewdistancetotop);    //should substract the height from top to Absolote Layout
        myscrollLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myscrollLinearlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //the position of pressing Y
                        Y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowY = (int) myscrollLinearlayout.getY();   //the position where dragging Linearlayout
                        int tempY = (int) (event.getRawY() - Y);    //deviation of moving
                        Y = (int) event.getRawY();
                        if ((nowY + tempY >= 0) && (nowY + tempY <= scrollviewdistancetotop)) {
                            if ((nowY + tempY <= menubarHeight)&& (topmenuflag == true) ){
                                userinfo_topbar.setVisibility(View.VISIBLE);
                                topmenuflag = false;
                            } else if ((nowY + tempY > menubarHeight) && (topmenuflag == flag)) {
                                userinfo_topbar.setVisibility(View.INVISIBLE);
                                topmenuflag = true;
                            }
                            int temp = position += tempY;
                            myscrollLinearlayout.setY(temp);
                            int headviewtemp = headViewPosition += (tempY/5);
                            mainheadview.setY(headviewtemp);
                        }
                        //top animation effect
                        if ((myscrollLinearlayout.getY() <= launchHeight) && (flag == true)) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mainheadview, "alpha", 1, 0.0f);
                            anim.setDuration(500);
                            anim.start();
                            flag = false;
                        } else if ((myscrollLinearlayout.getY() > launchHeight + 40) && (flag == false)) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mainheadview, "alpha", 0.0f, 1f);
                            anim.setDuration(500);
                            anim.start();
                            flag = true;
                        }
                        break;
                }
                return false;
            }
        });
    }


    private void initData() throws JSONException {
        // set userID
        TextView account_TV = (TextView) getView().findViewById(R.id.info_username);
        if (account_TV != null)
            infoArray.add(userInfo.getJSONObject("0").getString("userID"));
            account_TV.setText(userInfo.getJSONObject("0").getString("userID"));
        // set agentNo
        TextView agent_TV = (TextView) getView().findViewById(R.id.info_agentNo);
        if (agent_TV != null) {
            agent_TV.setText(StaticTool.agentNo);
        }
        // set First Name
        TextView firstname_TV = (TextView) getView().findViewById(R.id.info_firstname);
        if (firstname_TV != null)
            firstname_TV.setText(userInfo.getJSONObject("0").getString("firstname"));
        // set Last Name
        TextView lastname_TV = (TextView) getView().findViewById(R.id.info_lastname);
        if ( lastname_TV!= null)
            lastname_TV.setText(userInfo.getJSONObject("0").getString("lastname"));
        // set Telephone
        TextView telephone_TV = (TextView) getView().findViewById(R.id.info_telephone);
        if ( telephone_TV!= null)
            telephone_TV.setText(userInfo.getJSONObject("0").getString("phone"));
        // set Mobile
        TextView mobile_TV = (TextView) getView().findViewById(R.id.info_mobile);
        if ( mobile_TV!= null)
            mobile_TV.setText(userInfo.getJSONObject("0").getString("mobile"));
        // set Address
        TextView address_TV = (TextView) getView().findViewById(R.id.info_address);
        if ( address_TV!= null)
            address_TV.setText(userInfo.getJSONObject("0").getString("address"));
        // set Suburb
        TextView suburb_TV = (TextView) getView().findViewById(R.id.info_suburb);
        if ( suburb_TV != null)
            suburb_TV.setText(userInfo.getJSONObject("0").getString("suburb"));
        // set Postcode
        TextView postcode_TV = (TextView) getView().findViewById(R.id.info_postcode);
        if ( postcode_TV != null)
            postcode_TV.setText(userInfo.getJSONObject("0").getString("postcode"));
        // set Company
        TextView company_TV = (TextView) getView().findViewById(R.id.info_company);
        if ( company_TV != null)
            company_TV.setText(userInfo.getJSONObject("0").getString("companyName"));
        // set Company Type
        TextView companytype_TV = (TextView) getView().findViewById(R.id.info_companytype);
        if ( companytype_TV!= null)
            companytype_TV.setText(userInfo.getJSONObject("0").getString("companyType"));
        // set Trade Category
        TextView tradecategory_TV = (TextView) getView().findViewById(R.id.info_tradecategory);
        if ( tradecategory_TV!= null)
            tradecategory_TV.setText(userInfo.getJSONObject("0").getString("tradeCategory"));
        // set ABN
        TextView abn_TV = (TextView) getView().findViewById(R.id.info_abn);
        if ( abn_TV != null)
            abn_TV.setText(userInfo.getJSONObject("0").getString("ABN"));
    }

}
