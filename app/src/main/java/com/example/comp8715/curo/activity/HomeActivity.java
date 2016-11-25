package com.example.comp8715.curo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.comp8715.curo.Persistent.StaticTool;
import com.example.comp8715.curo.fragment.JobFragment;
import com.example.comp8715.curo.R;
import com.example.comp8715.curo.fragment.NotificationFragment;
import com.example.comp8715.curo.fragment.UserFragment;
import com.example.comp8715.curo.request.SampleFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.BottomBarFragment;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = HomeActivity.class.getSimpleName();

    private boolean mShouldRun = false; // If the Runnable should keep on running
    private final Handler mHandler = new Handler();
    private StaticTool staticTool;

    private CoordinatorLayout coordinatorLayout;
    private TextView tv;
    BottomBar bottomBar;
    BottomBarBadge unreadMessages;
    public JobFragment jobFragment;
    NotificationFragment notificationFragment;
    UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        jobFragment = new JobFragment(this);
        notificationFragment = new NotificationFragment(this);
        userFragment = new UserFragment();
        staticTool = new StaticTool(this);

        String tmp = "Welcome to CuroAus \n" + StaticTool.firstName +
                " " + StaticTool.lastName;

        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setFragmentItems(getFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(jobFragment, R.drawable.ic_work_white_24dp, "Jobs"),
                new BottomBarFragment(notificationFragment, R.drawable.ic_mail_white_24dp, "Quotes"),
                new BottomBarFragment(userFragment, R.drawable.ic_face_white_24dp, "About me"),
                new BottomBarFragment(SampleFragment.newInstance("Content for more."), R.drawable.ic_settings_white_24dp, "More")
        );
        // Setting colors for different tabs when there's more than three of them.
        bottomBar.mapColorForTab(0, "#3c7e10");
        bottomBar.mapColorForTab(1, "#86c218");
        bottomBar.mapColorForTab(2, R.color.colorPrimary);
        bottomBar.mapColorForTab(3, "#8ab16f");

        //bottomBar.selectTabAtPosition(1, true);
        // Make a Badge for the first tab, with red background color and a value of "4".
        unreadMessages = bottomBar.makeBadgeForTabAt(1, "#FF0000", StaticTool.unreadJobs.size() - 1);
        if(StaticTool.unreadJobs.size() - 1 == 0) {
            unreadMessages.hide();
        } else {
            unreadMessages.show();
        }
        // If you want the badge be shown always after unselecting the tab that contains it.
        unreadMessages.setAutoShowAfterUnSelection(false);

        startUpdater();
    }

    public void startUpdater() {

        mShouldRun = true;
        mHandler.post(mUpdateClock);
    }

    private final Runnable mUpdateClock = new Runnable() {
        public void run() {
            if(mShouldRun) {
                //query all job per 6 seconds.
                staticTool.queryAllJob();

                int unread_job_num = StaticTool.unreadJobs.size() - 1;
                System.out.println("HomeActiticy query all job --> current unread job num: " + unread_job_num);
                if(unread_job_num == 0) {
                    // Change the displayed count for this badge.
                    unreadMessages.setCount(0);
                    unreadMessages.hide();
                } else {
                    // Change the displayed count for this badge.
                    unreadMessages.setCount(unread_job_num);
                    // Control the badge's visibility
                    unreadMessages.show();
                }
                // Change the show / hide animation duration.
                //unreadMessages.setAnimationDuration(200);

            }
            // Call the method to actually update the clock
            mHandler.postDelayed(mUpdateClock, 6000); // 6 second

        }
    };

    @Override
    // Listen to Back key
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
