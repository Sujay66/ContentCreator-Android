package com.raveltrips.contentcreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.raveltrips.contentcreator.Fragments.MyTripsFragment;
import com.raveltrips.contentcreator.Fragments.OfflineFragments.Offline_AddNewGemFragment;
import com.raveltrips.contentcreator.Fragments.OfflineFragments.Offline_HomePageFragment;
import com.raveltrips.contentcreator.Fragments.OfflineFragments.Offline_MainTripCreatorFragment;
import com.raveltrips.contentcreator.Fragments.OfflineFragments.Offline_MapActivitiesFragment;
import com.raveltrips.contentcreator.models.CompleteTrip;

public class OfflinePageActivity extends AppCompatActivity implements
        Offline_MainTripCreatorFragment.OnFragmentInteractionListener , MyTripsFragment.OfflineFragmentInteraction , MyTripsFragment.OnFragmentInteractionListener  {

    Boolean mytripsFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_page);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_rest);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setVisibility(View.GONE);
        Bundle extras =getIntent().getExtras();
        if (extras!=null)
        {
           mytripsFlag = (Boolean) extras.get("mytrips");
        }
        if (!mytripsFlag) {
            getSupportFragmentManager().beginTransaction().replace(R.id.offlinecontainer, new Offline_HomePageFragment()).addToBackStack(null).commit();
        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.offlinecontainer, new MyTripsFragment()).addToBackStack(null).commit();
        }
    }



    @Override
    public void onFragmentInteraction(View v, int position) {

        switch (position){
            case 0 :
                getSupportFragmentManager().beginTransaction().replace(R.id.offlinecontainer,new Offline_AddNewGemFragment()).
                        addToBackStack(null).commit();
                break;
            case 1 :
                getSupportFragmentManager().beginTransaction().replace(R.id.offlinecontainer,Offline_MapActivitiesFragment.newInstance("PAIDACTIVITIES")).
                        addToBackStack(null).commit();
                break;
            case 2 :
                getSupportFragmentManager().beginTransaction().replace(R.id.offlinecontainer,Offline_MapActivitiesFragment.newInstance("RESTAURANTS")).
                        addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onLongClick(View v, int position) {

    }

    @Override
    public void offlineFragmentInteraction(View v, int position) {
        CompleteTrip offlineSelectedTrip = (CompleteTrip) AppContext.offlineTripsList.get(position);
        AppContext.offlineSelectedTripPosition = position;
        getSupportFragmentManager().beginTransaction().replace(R.id.offlinecontainer,Offline_MainTripCreatorFragment.newInstance(position)).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count>1)
        {
            super.onBackPressed();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
