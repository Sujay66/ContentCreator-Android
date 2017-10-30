package com.raveltrips.contentcreator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.raveltrips.contentcreator.Fragments.MapActivitiesFragment;
import com.raveltrips.contentcreator.Fragments.RefineLocationFragment;
import com.raveltrips.contentcreator.models.Restaurants;

public class RestaurantsActivity extends AppCompatActivity implements MapActivitiesFragment.OnFragmentInteractionListener{
    int PLACE_PICKER_REQUEST = 1;
    Restaurants restaurants = null;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar_rest);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_restaurants);
        Bundle extras = getIntent().getExtras();
        int flag=0;
        if (extras!=null)
        {
            flag =  Integer.parseInt( extras.getString("editflag"));
        }
        if(flag ==4)
        {
            Restaurants restaurants = (Restaurants) extras.getSerializable("restaurant");
            if(!restaurants.getaccuracy()) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.restaurantcontainer,  RefineLocationFragment.newInstance(restaurants)).commit();
            }else{
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.restaurantcontainer, MapActivitiesFragment.newInstance(restaurants)).commit();
            }

        }
        else {

            restaurants = new Restaurants();

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.restaurantcontainer, MapActivitiesFragment.newInstance(place,restaurants)).commitAllowingStateLoss();



        }
        else if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_CANCELED)
        {
            finish();
        }

            else {
            super.onActivityResult(requestCode, resultCode, data);
            //finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

}
