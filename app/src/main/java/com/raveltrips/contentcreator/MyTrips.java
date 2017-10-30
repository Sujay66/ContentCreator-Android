package com.raveltrips.contentcreator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.raveltrips.contentcreator.Fragments.MyTripsFragment;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DeleteTripAsyncTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyTrips extends AppCompatActivity implements MyTripsFragment.OnFragmentInteractionListener, DialogInterface.OnCancelListener, DialogInterface.OnKeyListener {
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        if (savedInstanceState == null) {
            //fetch profile
            Log.d("MyTripsActivity","Before call to fetch profile");
            this.fetchProfile();
            AppContext.fetchingData = true;

            dialog = ProgressDialog.show(this, "Loading Trips",
                    "Please Wait", true, true,this);
            //dialog.setOnKeyListener(this);
            AppContext.dialog = dialog;
            if(!AppContext.fetchingData) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mytrip_container, new MyTripsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    @Override
    public void onFragmentInteraction(View v, int position) {
        Intent intent = new Intent(this,TripCreatorActivity.class);
        intent.putExtra("selectedTripPosition",position);
        AppContext.selectedTripPosition = position;
        AppContext.selectedCompleteTrip = AppContext.completedTrips.get(position);
        if(AppContext.CALL)
            AppContext.CALL = false;
        startActivity(intent);

    }

    @Override
    public void onLongClick(View v, int position) {
        AppContext.selectedTripPosition = position;
        AppContext.selectedCompleteTrip = AppContext.completedTrips.get(position);
        MyTrips.this.startActionMode(new ActionBarCallBack());

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }



    public void fetchProfile(){
        //download profile data
        if (AppContext.fireBaseUser != null) {
            DownloadJsonAsyncTask profileDownload = new DownloadJsonAsyncTask();
            profileDownload.setAsyncComplete(new AsyncComplete() {
                @Override
                public void OnJsonAsyncCompleted(List<String> jsons) {
                    try {
                        Gson gson = new Gson();
                        if (jsons != null && jsons.size() > 0) {
                            JSONObject responseModel = new JSONObject(jsons.get(0));
                            JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                            String status = responseModel.getString("status");
                            if (status != null && status.equalsIgnoreCase("200")) {
                                JSONObject profObj = jsonArray.getJSONObject(0);
                                Profile sProfile = gson.fromJson(profObj.toString(), Profile.class);
                                if (sProfile != null) {
                                    AppContext.setProfileData(sProfile);
                                    Log.d("AnyActivity", "Received server profile..id:" + sProfile.getId());
                                    fetchAllTrips();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.d("AnyActivity", "Exception converting profile from json:" + ex);
                        ex.printStackTrace();
                    }
                }
            });
            //http://raveltrips.com/api/v1/client/profile/fetch?name=Akash
            // Anjanappa&email=akash@raveltrips.com&type=user
            String profileUrl = null;
            HashMap<String,String> paramsMap = new HashMap<>();
            //name,type not required, as profile will be already created, we can fetch with just email
            paramsMap.put("name",AppContext.fireBaseUser.getEmail());
            paramsMap.put("type","creator");
            paramsMap.put("email",AppContext.fireBaseUser.getEmail());
            String query =  AppContext.buildHttpParams(paramsMap);
            profileUrl = AppContext.FETCH_CREATE_PROFILE_URL + "?"+query;

            profileDownload.execute(profileUrl);
        }
    }


    public void fetchAllTrips(){
        //download profile data
        Log.d("AnyActivity", "Fetching profile");
        if (AppContext.fireBaseUser != null) {
            DownloadJsonAsyncTask profileDownload = new DownloadJsonAsyncTask();
            profileDownload.setAsyncComplete(new AsyncComplete() {
                @Override
                public void OnJsonAsyncCompleted(List<String> jsons) {
                    try {
                        Gson gson = new Gson();
                        if (jsons != null && jsons.size() > 0) {
                            JSONObject responseModel = new JSONObject(jsons.get(0));
                            JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                            String status = responseModel.getString("status");
                            if(status !=null && status.equalsIgnoreCase("200")){
                                AppContext.fetchingData = false;

                                AppContext.completedTrips.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject tripObj = jsonArray.getJSONObject(i);
                                    CompleteTrip trip = gson.fromJson(tripObj.toString(),CompleteTrip.class);
                                    if(trip!=null && !AppContext.completedTrips.contains(trip)){
                                        AppContext.completedTrips.add(trip);
                                    }
                                }
                                if (AppContext.dialog!=null) {
                                    AppContext.dialog.dismiss();
                                    if(!AppContext.fetchingData) {
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.mytrip_container, new MyTripsFragment())
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                    //cache
                                }
                            }   else {
                                Log.d("AnyActivity","Received error from server:");
                            }
                        }
                        else
                        {
                            Log.d("AnyActivity", "Json size is 0");
                        }
                    } catch (Exception ex) {
                        Log.d("AnyActivity", "Exception converting profile from json:" + ex);
                        ex.printStackTrace();
                    }
                }
            });
            //http://raveltrips.com/api/v1/client/profile/fetch?name=Akash
            // Anjanappa&email=akash@raveltrips.com&type=user
            String profileUrl = null;

            profileUrl = AppContext.GET_ALL_TRIPS;
            profileDownload.execute(profileUrl);
        }
    }


    private class ActionBarCallBack implements android.view.ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            mode.setTitle("Delete a Trip");
            return true;
        }

        @Override
        public boolean onActionItemClicked(final android.view.ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.deletetrip:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MyTrips.this);
                    builder1.setMessage("Are you sure to delete this Trip?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int id) {
                       dialog = ProgressDialog.show(MyTrips.this, "Deleting Trip",
                                            "Please Wait", true, true,MyTrips.this);
                                    dialog.setOnKeyListener(MyTrips.this);
                                    AppContext.dialog = dialog;
                                    AppContext.fetchingData = true;
                                    deleteTripServerCall();
                                    mode.finish();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return true;
            }
            return false;

        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {

        }
    }
    public void deleteTripServerCall(){
        if (AppContext.fireBaseUser != null && AppContext.profile != null) {
            DeleteTripAsyncTask deleteTripAsyncTask = new DeleteTripAsyncTask();
            deleteTripAsyncTask.setAsyncComplete(new AsyncComplete() {
                @Override
                public void OnJsonAsyncCompleted(List<String> json) {
                    try {
                        Gson gson = new Gson();
                        if (json != null && json.size() > 0) {
                            JSONObject responseModel = new JSONObject(json.get(0));

                            String status = responseModel.getString("status");
                            if (status != null && status.equalsIgnoreCase("200")) {


                                MainActivity.dataBaseHelper.deleteDataByTripId("MYTRIPS",AppContext.selectedCompleteTrip.getId());
                                MainActivity.dataBaseHelper.deleteDataByTripId("RESTAURANTS",AppContext.selectedCompleteTrip.getId());
                                MainActivity.dataBaseHelper.deleteDataByTripId("PAIDACTIVITIES",AppContext.selectedCompleteTrip.getId());
                                MainActivity.dataBaseHelper.deleteDataByTripId("HIDDENGEMS",AppContext.selectedCompleteTrip.getId());
                                Log.d("MyTrips", "Deleted trip ");


                                AppContext.selectedCompleteTrip = null;
                                fetchAllTrips();
                            }
                        }
                    }catch (Exception ex){
                        Log.d("MyTrips", "Exception converting profile from json:" + ex);
                        ex.printStackTrace();
                    }
                }
            });
            String del = AppContext.DELETE_TRIP_URL+AppContext.selectedCompleteTrip.getId();
            deleteTripAsyncTask.execute(del);
        }
    }
}
