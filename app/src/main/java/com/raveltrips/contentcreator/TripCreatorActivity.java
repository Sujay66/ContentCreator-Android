package com.raveltrips.contentcreator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.raveltrips.contentcreator.Fragments.HomePageFragment;
import com.raveltrips.contentcreator.Fragments.MainTripCreatorFragment;
import com.raveltrips.contentcreator.Fragments.MyGemsFragment;
import com.raveltrips.contentcreator.Fragments.OptionsNavigatorFragment;
import com.raveltrips.contentcreator.Fragments.TripOverviewFragment;
import com.raveltrips.contentcreator.Fragments.VideoUploadFragment;
import com.raveltrips.contentcreator.Utils.TripCreatorData;
import com.raveltrips.contentcreator.models.CompleteTrip;

import java.io.Serializable;
import java.util.List;

public class TripCreatorActivity extends AppCompatActivity  implements  MainTripCreatorFragment.OnFragmentInteractionListener,MyGemsFragment.OnFragmentInteractionList , OptionsNavigatorFragment.OnFragmentInteractionListener{

    TripCreatorData tripData = null;
    int selectedTripPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_creator);
        AppContext.completeTrip = new CompleteTrip();
        int newTripFlag = 0;
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            if (extras.get("newtrip") != null)
                newTripFlag = extras.getInt("newtrip");
        }
        if (AppContext.completedTrips.size()>0 && newTripFlag==1)
        {
            AppContext.selectedCompleteTrip = AppContext.completeTrip;
        }
       // tripData = new TripCreatorData();

        if (extras!= null && newTripFlag==0)
        {
           selectedTripPosition = extras.getInt("selectedTripPosition");
            AppContext.completeTrip =   AppContext.selectedCompleteTrip;
            AppContext.pindrops = AppContext.selectedCompleteTrip.getPindrops();
            AppContext.restaurantsList = AppContext.selectedCompleteTrip.getRestaurants();
            AppContext.activitiesList = AppContext.selectedCompleteTrip.getPaidActivities();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.trip_container, MainTripCreatorFragment.newInstance(selectedTripPosition))
                    .commit();
        }
        else {
            //New trip. clear pindrops
            AppContext.pindrops.clear();
            AppContext.restaurantsList.clear();
            AppContext.activitiesList.clear();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.trip_container, MainTripCreatorFragment.newInstance())
                    .commit();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Collapse keyboard when clicked outside edit text
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void onFragmentInteraction(View v , int position ,int flag) {
        if (flag == 1){
            if (position == 0)
                getSupportFragmentManager().beginTransaction().replace(R.id.trip_container, TripOverviewFragment.newInstance()).addToBackStack(null).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.trip_container, VideoUploadFragment.newInstance()).addToBackStack(null).commit();
        }
        else
        {
            if (position ==1 || position == 4 || position ==7)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.trip_container, MyGemsFragment.newInstance(position)).addToBackStack(null).commit();
            }
            if (position == 2)
            {
                Intent intent = new Intent(this, HiddenGemsActivity.class);
                startActivity(intent);
            }
            else if (position ==5)
            {
                Intent intent = new Intent(this, RestaurantsActivity.class);
                startActivity(intent);
            }
            else if (position ==8)
            {
                Intent intent = new Intent(this, PaidActivitiesActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public void Sujay(View v, int position, int flag, List<CompleteTrip> pindropList , int selectedType) {
        switch (selectedType)
        {
            case 1:
            if (flag == 1) {
                Intent intent = new Intent(getApplicationContext(),HiddenGemsActivity.class);
                intent.putExtra("editflag","1");
                intent.putExtra("pindrop", (Serializable) AppContext.selectedCompleteTrip.getPindrops().get(position));
                intent.putExtra("pinposition",position);
                startActivity(intent);

            } else if (flag == 2) {
                AppContext.selectedCompleteTrip.getPindrops().remove(position);
                Toast.makeText(getApplicationContext(), "Gem Deleted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), AppContext.selectedCompleteTrip.getPindrops().get(position).getName()
                        , Toast.LENGTH_LONG).show();
            }
        break;
            case 4:
                if (flag == 1) {
                    Intent intent = new Intent(getApplicationContext(),RestaurantsActivity.class);
                    intent.putExtra("editflag","4");
                    intent.putExtra("restaurant", (Serializable) AppContext.selectedCompleteTrip.getRestaurants().get(position));
                    intent.putExtra("restoposition",position);

                    startActivity(intent);

                } else if (flag == 2) {
                    AppContext.selectedCompleteTrip.getRestaurants().remove(position);
                    Toast.makeText(getApplicationContext(), "Restaurant Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), AppContext.selectedCompleteTrip.getName()
                            , Toast.LENGTH_LONG).show();
                }
                break;
            case 7:
                if (flag == 1) {
                    Intent intent = new Intent(getApplicationContext(),PaidActivitiesActivity.class);
                    intent.putExtra("editflag","7");
                    intent.putExtra("activities", (Serializable) AppContext.selectedCompleteTrip.getPaidActivities().get(position));
                    intent.putExtra("activityposition",position);
                    startActivity(intent);
                } else if (flag == 2) {
                    AppContext.selectedCompleteTrip.getPaidActivities().remove(position);
                    Toast.makeText(getApplicationContext(), "Paid Activity Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), AppContext.selectedCompleteTrip.getPaidActivities().get(position).getName()
                            , Toast.LENGTH_LONG).show();
                }
                break;
    }
    }

    @Override
    public void onFragmentInteraction(View v, int position) {
        Intent i;
        switch (position){
            case 0:     getSupportFragmentManager().beginTransaction().replace(R.id.trip_container, new HomePageFragment())
                    .addToBackStack(null).commit();
                break;
            case 1:
                AppContext.CALL = true;
                i = new Intent(this,TripCreatorActivity.class);
                startActivity(i);
                break;
            case 2:   i = new Intent(this,MyTrips.class);
                startActivity(i);
                break;
            case 3: i = new Intent(this,WelcomeActivity.class);
                startActivity(i);
                break;
            case 4:  FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                AppContext.clearUserCache();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case 5:   i = new Intent(this,ProfileNewActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
