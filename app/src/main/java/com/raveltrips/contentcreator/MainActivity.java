package com.raveltrips.contentcreator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.raveltrips.contentcreator.Fragments.HomePageFragment;
import com.raveltrips.contentcreator.Fragments.OptionsNavigatorFragment;
import com.raveltrips.contentcreator.Fragments.UploadTripsFragment;
import com.raveltrips.contentcreator.Service.ConnectionService;
import com.raveltrips.contentcreator.Utils.DataBaseHelper;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPostTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.FcmToken;
import com.raveltrips.contentcreator.models.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.raveltrips.contentcreator.AppContext.FIREBASE_TOKEN;

public class MainActivity extends AppCompatActivity implements OptionsNavigatorFragment.OnFragmentInteractionListener,ConnectionService.ConnectionServiceCallback{

    public static DataBaseHelper dataBaseHelper;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        String flag = null;
        Boolean offlineFlag = false;
        if (extras!=null)
        {
            flag = (String) extras.get("flag");
            offlineFlag = (Boolean) extras.get("offlineFlag");
        }

        //Implement connection service callback
        // Start background Service to check internet connection



        if (flag!= null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new OptionsNavigatorFragment())
                    .commit();
        }
        else if (AppContext.OfflineMode)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new OptionsNavigatorFragment())
                   .commit();
        }
        else if (offlineFlag)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new UploadTripsFragment()).commit();
        }
        else if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container,new HomePageFragment())
                    .addToBackStack(null)
                    .commit();
        }
        Intent intent = new Intent(this, ConnectionService.class);
        // Interval in seconds
        intent.putExtra(ConnectionService.TAG_INTERVAL, 1);
        // URL to ping
        intent.putExtra(ConnectionService.TAG_URL_PING, "http://www.google.com");
        // Name of the class that is calling this service
        intent.putExtra(ConnectionService.TAG_ACTIVITY_NAME, this.getClass().getName());
        // Starts the service
        startService(intent);
        fetchFCMToken();
        fetchFirebaseToken();

    }

    public void fetchServerSession(){
        if(AppContext.SERVER_SESSION == null){
            try{
                testAccessToServer();
                Log.i("MainActivity","server session fetch completed");
            }catch (Exception ex){ ex.printStackTrace();}
        }
    }

    public void fetchFCMToken(){
        if(AppContext.FCM_TOKEN == null){
            try{
                AppContext.fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String token = FirebaseInstanceId.getInstance().getToken();
                AppContext.FCM_TOKEN = token;
                FirebaseMessaging.getInstance().subscribeToTopic(AppContext.FIREBASE_TOPIC_NAME);
                Log.i("MainActivity","subscribed to topic");
                Log.i("MainActivity","Firebase FCM token:"+token);
            }catch (Exception ex){ ex.printStackTrace();}
        }
    }

    public void fetchFirebaseToken(){
        if(FIREBASE_TOKEN == null){
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                Log.i("MainActivity","Firebase token:"+idToken);
                                FIREBASE_TOKEN = idToken;
                                fetchServerSession();
                            } else {
                                Log.i("MainActivity","Firebase login failed:"+task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    public void fetchProfile(){
        //download profile data
        if (AppContext.fireBaseUser != null && AppContext.profile == null) {
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

                                    Log.d("MainActivity", "Received server profile..id:" + sProfile.getId());
                                    int offlineGemCount = MainActivity.dataBaseHelper.getCount("HIDDENGEMS");
                                    int offlineActivityCount = MainActivity.dataBaseHelper.getCount("PAIDACTIVITIES");
                                    int offlineRestaurantCount = MainActivity.dataBaseHelper.getCount("RESTAURANTS");

                                    if(offlineGemCount>0 || offlineActivityCount>0 || offlineRestaurantCount>0){
                                        Intent intent = new Intent(getApplicationContext(),OfflineUploadActivity.class);
                                        intent.putExtra("offlineFlag",true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    fetchAllTrips();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.d("MainActivity", "Exception converting profile from json:" + ex);
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
        Log.d("MainActivity", "In fetch All Trips Async task");
        if (AppContext.fireBaseUser != null && AppContext.profile != null) {
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
                                AppContext.completedTrips.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject tripObj = jsonArray.getJSONObject(i);
                                    CompleteTrip trip = gson.fromJson(tripObj.toString(),CompleteTrip.class);
                                    if(trip!=null && !AppContext.completedTrips.contains(trip)){
                                        AppContext.completedTrips.add(trip);
                                    }
                                }
                                //cache

                            }   else {
                                Log.d("MainActivity","Received error from server:");
                            }
                        }
                        else
                        {
                            Log.d("MainActivity", "Json size is 0");
                        }
                    } catch (Exception ex) {
                        Log.d("MainActivity", "Exception converting profile from json:" + ex);
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
    public void testAccessToServer() {
        Log.i("MainActivity", "pinging server");
            try{
                DownloadJsonAsyncTask pingTask = new DownloadJsonAsyncTask();
                //used only once..not required for other calls.
                pingTask.setCustomHeaderSet(true);
                pingTask.setHdrKey(AppContext.FIREBASE_HEADER_KEY);
                pingTask.setHdrVal(AppContext.FIREBASE_TOKEN);
                pingTask.setAsyncComplete(new AsyncComplete() {
                    @Override
                    public void OnJsonAsyncCompleted(List<String> jsons) {
                        try {
                            if (jsons != null && jsons.size() > 0) {
                                JSONObject responseModel = new JSONObject(jsons.get(0));
                                String status = responseModel.getString("status");
                                if (status != null && status.equalsIgnoreCase("200")) {
                                    Log.d("MainActivity", "ping successfull");
                                    //fetch server profile
                                    fetchProfile();

                                }
                                else {
                                    Log.d("MainActivity", "ping failed: status:"+status+" message:"
                                            +responseModel.getString("message"));
                                }
                            }
                        } catch (Exception ex) {
                            Log.d("MainActivity", "Exception pinging server:" + ex);
                        }
                    }
                });

                String query = "?type=creator";
                pingTask.execute(AppContext.TEST_URL+query);
            }catch (Exception ex){ ex.printStackTrace();}
    }

    public void updateFcmTokenToServer(String email, String token) {
        Log.i("MainActivity", "sending fcm token to server..email:"+email+" token:"+token);
        if (token != null && email != null) {
            try{
                DownloadJsonAsyncPostTask fcmTask = new DownloadJsonAsyncPostTask();
                FcmToken fcmToken = new FcmToken(email, token);
                Gson gson = new Gson();
                String body = gson.toJson(fcmToken).toString();
                fcmTask.setBodyContent(body);
                fcmTask.setAsyncComplete(new AsyncComplete() {
                    @Override
                    public void OnJsonAsyncCompleted(List<String> jsons) {
                        try {
                            if (jsons != null && jsons.size() > 0) {
                                JSONObject responseModel = new JSONObject(jsons.get(0));
                                String status = responseModel.getString("status");
                                if (status != null && status.equalsIgnoreCase("200")) {
                                    Log.d("MainActivity", "fcm updated to server");
                                }
                                else {
                                    Log.d("MainActivity", "fcm token send failed: status:"+status+" message:"
                                            +responseModel.getString("message"));
                                }
                            }
                        } catch (Exception ex) {
                            Log.d("MainActivity", "Exception sending fcm token to server:" + ex);
                        }
                    }
                });
                fcmTask.execute(AppContext.UPDATE_FCM_URL);
            }catch (Exception ex){ ex.printStackTrace();}
        }
    }

    @Override
    public void onFragmentInteraction(View v, int position) {
        Intent i;

            if(AppContext.OfflineMode)
            {
                switch (position)
                {
                    case 0: getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomePageFragment())
                            .addToBackStack(null).commit();
                        break;
                    case 1:
                        i = new Intent(this, OfflinePageActivity.class);
                        i.putExtra("mytrips",true);
                        startActivity(i);
                        break;
                    case 2:
//                        i = new Intent(this, WelcomeActivity.class);
//                        startActivity(i);

                        break;
                }
            }
            else {
                switch (position){
                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomePageFragment())
                            .addToBackStack(null).commit();
                    break;
                case 1:
                    AppContext.CALL = true;
                    i = new Intent(this, TripCreatorActivity.class);
                    i.putExtra("newtrip",1);
                    startActivity(i);
                    break;
                case 2:
                    i = new Intent(this, MyTrips.class);
                    startActivity(i);
                    break;
                case 3:
                    i = new Intent(this, WelcomeActivity.class);
                    startActivity(i);
                    break;
                case 4:
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    AppContext.clearUserCache();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
            }

    }

    @Override
    public void hasInternetConnection(Context appContext) {
        if (AppContext.OfflineMode) {
            Intent intent;
            AppContext.OfflineMode = false;
            int offlineGemCount = MainActivity.dataBaseHelper.getCount("HIDDENGEMS");
            int offlineActivityCount = MainActivity.dataBaseHelper.getCount("PAIDACTIVITIES");
            int offlineRestaurantCount = MainActivity.dataBaseHelper.getCount("RESTAURANTS");

            if(offlineGemCount>0 || offlineActivityCount>0 || offlineRestaurantCount>0){
                intent = new Intent(appContext,OfflineUploadActivity.class);
                intent.putExtra("offlineFlag",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
                finish();
            }else{
             intent = new Intent(appContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
        }
    }
    }

    @Override
    public void hasNoInternetConnection(final Context applicationContext) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(applicationContext);
        } else {
            builder = new AlertDialog.Builder(applicationContext);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
                if (!AppContext.OfflineMode) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            //Toast.makeText(applicationContext, "Hey Offline", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(applicationContext,OfflinePageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(intent);

                        }
                    });
                    AppContext.OfflineMode = true;

                }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


}
