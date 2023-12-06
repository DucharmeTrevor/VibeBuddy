package com.vibebuddy.vibebuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lovense.sdklibrary.Lovense;
import com.vibebuddy.vibebuddy.Buddy.Buddy;
import com.vibebuddy.vibebuddy.Buddy.BuddyManager;
import com.vibebuddy.vibebuddy.Frags.BuddyControlFrag;
import com.vibebuddy.vibebuddy.Frags.BuddySettingsFrag;
import com.vibebuddy.vibebuddy.Frags.ExampleTrainingFrag;
import com.vibebuddy.vibebuddy.Frags.RewardTrainingFrag;
import com.vibebuddy.vibebuddy.Lovense.LovenseManager;

public class MainActivity extends AppCompatActivity {
    Lovense lovense;
    public static LovenseManager lovenseManager;
    public static BuddyManager buddyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                //Database permissions
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        lovense = Lovense.getInstance(getApplication());

        lovense.setDeveloperToken("SCfh7CamO5irTBgvB53z2hdAYjmq2SJLiUNFLrB1SvOQu9hyemQL5lUimQZTeIqT");

        lovenseManager = new LovenseManager(this,lovense);

        //get saved buddy from buddy manager
        buddyManager = new BuddyManager(this);
        buddyManager.LoadBuddies();
        if(buddyManager.BuddyCount()<=0){
            buddyManager.AddBuddy(Buddy.DefaultBuddy());
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, BuddyControlFrag.class, null)
                    .commit();
        }
        //log app started
        Log.d("App Started", "App Started");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        buddyManager.SaveBuddies();
    }
    public void OnSettingsButtonClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, BuddySettingsFrag.class, null)
                .commit();
    }

    public void OnExampleTrainingButtonClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, ExampleTrainingFrag.class, null)
                .commit();
    }

    public void OnRewardTrainingButtonClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, RewardTrainingFrag.class, null)
                .commit();
    }

    public void OnBuddyControlButtonClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, BuddyControlFrag.class, null)
                .commit();
    }
}