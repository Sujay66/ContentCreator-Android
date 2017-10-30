package com.raveltrips.contentcreator;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Profile;
import com.raveltrips.contentcreator.models.Restaurants;
import com.raveltrips.contentcreator.models.Trip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akash Anjanappa on 13-04-2017.
 */

public class AppContext {

    //contentCreatorApp
    public static String BASE_SERVER_URL = "http://raveltrips.com";
    public static String TEST_URL = BASE_SERVER_URL+"/api/v1/client/test";
    public static String FETCH_CREATE_PROFILE_URL = BASE_SERVER_URL+"/api/v1/client/profile/fetch";
    public static String FCM_TOKEN;
    public static String FIREBASE_TOKEN;
    public static FirebaseUser fireBaseUser;
    //cahcing the users, trips and pindrops, avoiding unneccessary server calls
    public static Profile profile;
 //   public static Creator creator;
    public static String FIREBASE_TOPIC_NAME = "ravel-all";
    public static String SERVER_SESSION;
    public static String SERVER_SESSION_KEY = "JSESSIONID";
    public static String FIREBASE_HEADER_KEY="X-Authorization-Firebase";

    //Flag ti detect offline mode
    public static Boolean OfflineMode = false;
    public static ArrayList offlineTripsList = new ArrayList();
    public static int offlineSelectedTripPosition = -1;
    public static String TRENDING_TRIPS_URL = BASE_SERVER_URL+"/api/v1/open/mobile/trendingtrips/rating";
   // sample:  /api/v1/open/mobile/profile/fetch?name=Akash Anjanappa&email=akash.dedly@gmail.com
    public static String FETCH_PROFILE_URL = BASE_SERVER_URL+"/api/v1/open/mobile/profile/fetch";

    public static String UPDATE_PROFILE_URL = BASE_SERVER_URL+"/api/v1/open/mobile/profile";

    public static String UPDATE_COMPLETE_TRIP_URL = BASE_SERVER_URL+"/api/v1/client/complete-trip";

    public static String CREATE_COMPLETE_TRIP_URL = BASE_SERVER_URL+"/api/v1/client/complete-trip";

    //example:http://localhost:8080/api/v1/open/mobile/checkout?email=akash.a2351@gmail.com
    public static String CHECKOUT_URL = BASE_SERVER_URL+"/api/v1/open/mobile/checkout";

    public static String SEARCH_URL = BASE_SERVER_URL+"/api/v1/open/mobile/trip/search";

    public static String IMAGE_UPLOAD_URL = BASE_SERVER_URL+"/api/v1/open/mobile/image";

    public static String CREATE_TRIP_URL = BASE_SERVER_URL+"/api/v1/open/mobile/trip";

    //example:http://localhost:8080/api/v1/open/mobile/cart?email=akash.a2351@gmail.com
    public static String FETCH_CART_URL = BASE_SERVER_URL+"/api/v1/open/mobile/cart";

    public static String UPDATE_FCM_URL = BASE_SERVER_URL+"/api/v1/open/mobile/notify";

    //example: http://216.172.176.65/images/ravel-images/1492405942285.jpg
    public static String BASE_IMAGE_URL = BASE_SERVER_URL+"/images/ravel-images/";


    //http://raveltrips.com/api/v1/client/complete-trip
    public static String DELETE_TRIP_URL = BASE_SERVER_URL+"/api/v1/client/complete-trip/";
    public static String GET_TRIP_URL = BASE_SERVER_URL+"/api/v1/client/complete-trip/";
    public static String FETCH_MULTIPLE_TRIPS = BASE_SERVER_URL+"/api/v1/client/completetrip/ids";
    public static String GET_ALL_TRIPS = BASE_SERVER_URL+"/api/v1/client/created-trips";
    public static String TRENDING_PINDROPS_URL = BASE_SERVER_URL+"/api/v1/open/mobile/trendingpindrops/rating";
    //Boolean flag to track async call progress
    public static Boolean fetchingData = false;
    public static ProgressDialog dialog =null;
    // http://localhost:8080/api/v1/open/mobile/image
    public static LruCache<String, Bitmap> mImgCache;


    public static boolean CALL = true;     //Distinguish between post and put calls
                                    //true for POST and false for PUT
    public static int editPosition = -1; //identify position of gem or restaurant to be edited
    static{
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        Log.d("AppContext","Setting img cache to :"+cacheSize*2);
        mImgCache = new LruCache<String, Bitmap>(cacheSize*2) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static String buildHttpParams(Map<String,String> map){
        Uri.Builder builder = new Uri.Builder();
        for(Map.Entry<String,String> entry:map.entrySet()){
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
       return builder.build().getEncodedQuery();
    }

    public static void initializeImgCache(){
        try{
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 2/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            Log.d("AppContext","Setting img cache to :"+cacheSize *2+" kbs");

            mImgCache = new LruCache<String, Bitmap>(cacheSize*2) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }catch(Exception ex){   ex.printStackTrace();
        }
    }


    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            Log.d("AppContext","Adding image to cache:"+key);
            AppContext.mImgCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        if(AppContext.mImgCache != null)
            return AppContext.mImgCache.get(key);
        else {
            AppContext.initializeImgCache();
            return null;
        }
    }

    public static void clearUserCache(){
        completedTrips.clear();
        wishlistTrips.clear();
        profile = null;
    }





    public static List<Trip> trips = new ArrayList<>();

    public static List<Pindrop> pindrops = new ArrayList<>();


    public static List<Restaurants> restaurantsList = new ArrayList<>();
    public static List<PaidActivities> activitiesList = new ArrayList<>();

    public static CompleteTrip completeTrip;
    //public static List<CompleteTrip> completeTrips = new ArrayList<>();
    public static List<CompleteTrip> completedTrips = new ArrayList<>();
    public static int selectedTripPosition;
    public static CompleteTrip selectedCompleteTrip;
    public static List<Trip> wishlistTrips = new ArrayList<>();

    public static Boolean USE_PICASSO = false;

    public static void setProfileData(Profile updatedProfile){
        profile = new Profile();
        if(updatedProfile.getCart()!=null) profile.setCart(updatedProfile.getCart());
        if(!IsEmpty(updatedProfile.getCreatedDate()))profile.setCreatedDate(updatedProfile.getCreatedDate());
        if(!IsEmpty(updatedProfile.getEmail()))profile.setEmail(updatedProfile.getEmail());
        if(!IsEmpty(updatedProfile.getName()))profile.setName(updatedProfile.getName());
        if(updatedProfile.getOrderIds()!=null)profile.setOrderIds(updatedProfile.getOrderIds());
        if(!IsEmpty(updatedProfile.getRole()))profile.setRole(updatedProfile.getRole());
        if(updatedProfile.getTripsCompletedIds()!=null)profile.setTripsCompletedIds(updatedProfile.getTripsCompletedIds());
        if(updatedProfile.getTripsWishlistIds()!=null)profile.setTripsWishlistIds(updatedProfile.getTripsWishlistIds());
        if(!IsEmpty(updatedProfile.getMobNo()))profile.setMobNo(updatedProfile.getMobNo());
        if(!IsEmpty(updatedProfile.getAddress()))profile.setAddress(updatedProfile.getAddress());
        if(!IsEmpty(updatedProfile.getImageUrl()))profile.setImageUrl(updatedProfile.getImageUrl());
        if(!IsEmpty(updatedProfile.getCreatorID()))profile.setCreatorID(updatedProfile.getCreatorID());
    }

     public static boolean IsEmpty(String str){
        return (str == null || str.isEmpty());
    }

    //List to add trip data
    public static List tripCreatorList = new ArrayList();


    public static void fetchProfile(){
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


    public static void fetchAllTrips(){
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
}
