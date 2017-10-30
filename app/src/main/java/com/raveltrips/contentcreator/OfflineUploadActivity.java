package com.raveltrips.contentcreator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPostTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.GPSCoOrdinate;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Profile;
import com.raveltrips.contentcreator.models.Restaurants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OfflineUploadActivity extends AppCompatActivity {

    List pinDropTripIds = new ArrayList<String>();
    List restaurantTripIds = new ArrayList<String>();
    List activitiesTripIds = new ArrayList<String>();
    List tripObjects = new ArrayList();
    int iterator = 0;
    Button upload;
    CompleteTrip tripToUpload = new CompleteTrip();
    Boolean offlinePin = false, offlineRestaurant = false, offlineActivity =false;
    Boolean wait = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_upload);
        Bundle extras = getIntent().getExtras();

        upload = (Button)findViewById(R.id.upload_offline_locations);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Cursor pindrops = MainActivity.dataBaseHelper.getAll("HIDDENGEMS");
                Cursor restaurants = MainActivity.dataBaseHelper.getAll("RESTAURANTS");
                Cursor paidactivities = MainActivity.dataBaseHelper.getAll("PAIDACTIVITIES");
                Cursor cursor =MainActivity.dataBaseHelper.getUniqueTripsById("PAIDACTIVITIES");
                activitiesTripIds = parseRetrievedData(cursor,activitiesTripIds,2);
                cursor = MainActivity.dataBaseHelper.getUniqueTripsById("RESTAURANTS");
                restaurantTripIds = parseRetrievedData(cursor,restaurantTripIds,1);
                cursor = MainActivity.dataBaseHelper.getUniqueTripsById("HIDDENGEMS");
                pinDropTripIds = parseRetrievedData(cursor,pinDropTripIds,0);
                ArrayList list = new ArrayList();
                list.addAll(pinDropTripIds);
                list.removeAll(activitiesTripIds);
                list.addAll(activitiesTripIds);
                list.removeAll(restaurantTripIds);
                list.addAll(restaurantTripIds);

                getTrip(list);
                List<Pindrop> pindropList = tripToUpload.getPindrops();
                List<Restaurants> restaurantList = tripToUpload.getRestaurants();
                List<PaidActivities> paidActivityList = tripToUpload.getPaidActivities();

            }
        });
    }
    public List parseRetrievedData(Cursor cursor, List list, int flag)
    {

    if (cursor.moveToFirst()){
        //AppContext.offlineTripsList.clear();
        while(!cursor.isAfterLast()){

            String id = cursor.getString(cursor.getColumnIndex("TRIP_ID"));

            list.add(id);
            cursor.moveToNext();
        }
    }

                cursor.close();
        return list;
    }


    private void getTrip(List list) {
            Log.d("OfflineUploadActivity", "In getTrip");

        DownloadJsonAsyncPostTask tripDownload = new DownloadJsonAsyncPostTask();
        Gson gson = new Gson();
        HashMap map = new HashMap();
        map.put("ids",list);
        tripDownload.setBodyContent(gson.toJson(map).toString());
        tripDownload.setAsyncComplete(new AsyncComplete() {
                    @Override
                    public void OnJsonAsyncCompleted(List<String> jsons) {
                        try {
                            Gson gson = new Gson();
                            if (jsons != null && jsons.size() > 0) {
                                JSONObject responseModel = new JSONObject(jsons.get(0));
                                JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                                String status = responseModel.getString("status");
                                if(status !=null && status.equalsIgnoreCase("200")){
                                    JSONArray tripArray = (JSONArray) jsonArray.get(0);
                                    for (int i=0;i<tripArray.length();i++)
                                    {
                                        JSONObject profObj = tripArray.getJSONObject(i);
                                        CompleteTrip sProfile = gson.fromJson(profObj.toString(), CompleteTrip.class);
                                        setOfflinData(sProfile);
                                        tripObjects.add(sProfile);
                                    }
                                    UpdateTripServerCall();

                                }   else {
                                    Log.d("Offline Upload Activity","Received error from server:");
                                }
                            }
                            else
                            {
                                Log.d("OfflineUploadActivity", "Json size is 0");
                            }
                        } catch (Exception ex) {
                            Log.d("OfflineUploadActivity", "Exception converting profile from json:" + ex);
                            ex.printStackTrace();
                        }
                    }
                });
                String tripURL = null;
                tripURL = AppContext.FETCH_MULTIPLE_TRIPS;
                tripDownload.execute(tripURL);
            }

    private void UpdateTripServerCall() {


            tripToUpload = (CompleteTrip) tripObjects.get(iterator);
            DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
            Gson gson = new Gson();
            task.setBodyContent(gson.toJson(tripToUpload).toString());
            Log.d("AddNewGemFragment", "profile json" + gson.toJson(tripToUpload).toString());
            task.setAsyncComplete(new AsyncComplete() {
                @Override
                public void OnJsonAsyncCompleted(List<String> json) {
                    try {
                        Gson gson = new Gson();
                        if (json != null && json.size() > 0) {
                            JSONObject responseModel = new JSONObject(json.get(0));
                            JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                            String status = responseModel.getString("status");
                            if (status != null && status.equalsIgnoreCase("200")) {
                                JSONObject profObj = jsonArray.getJSONObject(0);
                                CompleteTrip ctrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                                if (ctrip != null) {
                                    Log.d("OfflineUpload", "updated offline trip:at "+ctrip.getName() + gson.toJson(ctrip).toString());
                                    Toast.makeText(getApplicationContext(), "Trip updated successfully ! at "+ctrip.getName(), Toast.LENGTH_SHORT).show();
                                    iterator++;
                                    if (iterator<tripObjects.size())
                                    {

                                        UpdateTripServerCall();
                                    }
                                    else
                                    {
                                        UpdateProfileServerCall();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to update trip!", Toast.LENGTH_SHORT).show();
                                Log.d("OfflineUploadActivity", "Failed to update");
                            }
                        }
                    } catch (Exception ex) {
                        Log.d("OfflineUploadActivity", "Exception converting profile from json:" + ex);
                    }
                }
            });
            task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);

    }

    private void UpdateProfileServerCall() {
        DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.profile).toString());
        Log.d("ProfileActivity", "profile json"+gson.toJson(AppContext.profile).toString());
        task.setAsyncComplete(new AsyncComplete() {
            @Override
            public void OnJsonAsyncCompleted(List<String> json) {
                try {
                    Gson gson = new Gson();
                    if (json != null && json.size() > 0) {
                        JSONObject responseModel = new JSONObject(json.get(0));
                        JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                        String status = responseModel.getString("status");
                        if (status != null && status.equalsIgnoreCase("200")) {
                            JSONObject profObj = jsonArray.getJSONObject(0);
                            Profile sProfile = gson.fromJson(profObj.toString(), Profile.class);
                            if(sProfile!=null){
                                Log.d("ProfileActivity", "updated profile:"+gson.toJson(sProfile).toString());
                                Toast.makeText(getApplicationContext(),"Profile updated successfully!",Toast.LENGTH_SHORT).show();
                                AppContext.profile.copyFromProfile(sProfile);
                                MainActivity.dataBaseHelper.deleteData("PAIDACTIVITIES");
                                MainActivity.dataBaseHelper.deleteData("RESTAURANTS");
                                MainActivity.dataBaseHelper.deleteData("HIDDENGEMS");
                                AppContext.fetchProfile();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Failed to update Profile! try again later..",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("OfflineUploadActivity", "Exception converting profile from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_PROFILE_URL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setOfflinData(CompleteTrip offlinData) {
                        if (activitiesTripIds.size()>0 && activitiesTripIds.contains(offlinData.getId())) {
                    offlineActivity = true;

                        Cursor actCursor = MainActivity.dataBaseHelper.getAllByTripId("PAIDACTIVITIES", (String) offlinData.getId());
                        if (actCursor.moveToFirst()) {
                            while (!actCursor.isAfterLast()) {
                                PaidActivities p = new PaidActivities();
                                GPSCoOrdinate gps = new GPSCoOrdinate();
                                gps.setLongitude(actCursor.getString(actCursor.getColumnIndex("LONGITUDE")));
                                gps.setLattitude(actCursor.getString(actCursor.getColumnIndex("LATITUDE")));
                                p.setGps(gps);
                                p.setName(actCursor.getString(actCursor.getColumnIndex("NAME")));
                                p.setDescription(actCursor.getString(actCursor.getColumnIndex("DESCRIPTION")));
                                p.setContactnumber(actCursor.getString(actCursor.getColumnIndex("PHONE_NUMBER")));
                                p.setWebsite(actCursor.getString(actCursor.getColumnIndex("WEBSITE")));
                                p.setaccuracy(false);
                                //get Complete trip object for this ID and set its activity list
                                List temp =offlinData.getPaidActivities();
                                //set ready for review flag GPS
                                temp.add(p);
                                offlinData.setPaidActivities(temp);
                                actCursor.moveToNext();
                            }

                        }

                }
                if (pinDropTripIds.size()> 0 && pinDropTripIds.contains(offlinData.getId()))
                {
                    offlinePin = true;

                        Cursor pindrops1 = MainActivity.dataBaseHelper.getAllByTripId("HIDDENGEMS", (String) offlinData.getId());
                        if (pindrops1.moveToFirst()) {
                            while (!pindrops1.isAfterLast()) {
                                Pindrop p = new Pindrop();
                                GPSCoOrdinate gps = new GPSCoOrdinate();
                                gps.setLongitude(pindrops1.getString(pindrops1.getColumnIndex("LONGITUDE")));
                                gps.setLattitude(pindrops1.getString(pindrops1.getColumnIndex("LATITUDE")));
                                p.setName(pindrops1.getString(pindrops1.getColumnIndex("NAME")));
                                p.setGps(gps);
                                p.setaccuracy(false);
                                p.setDescription(pindrops1.getString(pindrops1.getColumnIndex("DESCRIPTION")));
                                //get Complete trip object for this ID and set its pindrop list
                                //set ready for review flag
                                List temp = offlinData.getPindrops();
                                temp.add(p);
                                offlinData.setPindrops(temp);
                                pindrops1.moveToNext();
                            }

                        }


                }

                if (restaurantTripIds.size()>0 && restaurantTripIds.contains(offlinData.getId()))
                {
                    offlineRestaurant= true;

                        Cursor restaurants1 = MainActivity.dataBaseHelper.getAllByTripId("RESTAURANTS", (String) offlinData.getId());
                        if (restaurants1.moveToFirst()) {
                            while (!restaurants1.isAfterLast()) {
                                Restaurants p = new Restaurants();
                                GPSCoOrdinate gps = new GPSCoOrdinate();
                                gps.setLongitude(restaurants1.getString(restaurants1.getColumnIndex("LONGITUDE")));
                                gps.setLattitude(restaurants1.getString(restaurants1.getColumnIndex("LATITUDE")));
                                p.setGps(gps);
                                p.setName(restaurants1.getString(restaurants1.getColumnIndex("NAME")));
                                p.setDescription(restaurants1.getString(restaurants1.getColumnIndex("DESCRIPTION")));
                                p.setContactNumber(restaurants1.getString(restaurants1.getColumnIndex("PHONE_NUMBER")));
                                p.setWebsite(restaurants1.getString(restaurants1.getColumnIndex("WEBSITE")));
                                p.setaccuracy(false);
                                //get Complete trip object for this ID and set its restaurant list
                                //set ready for review flag and location
                                List temp = offlinData.getRestaurants();
                                temp.add(p);
                                offlinData.setRestaurants(temp);
                                restaurants1.moveToNext();
                            }

                        }


                }
    }
}


