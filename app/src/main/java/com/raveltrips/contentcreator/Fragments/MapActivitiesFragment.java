package com.raveltrips.contentcreator.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.Utils.DataBaseHelper;
import com.raveltrips.contentcreator.Utils.PermUtility;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.AsyncUploadImage;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPostTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.async.MultipleImageUploadAsyncTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.GPSCoOrdinate;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Profile;
import com.raveltrips.contentcreator.models.Restaurants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapActivitiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapActivitiesFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int PROXIMITY_RADIUS = 10000;
    Double latitude,longitude;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    ImageView gallery,cam,icon;
    Place place = null;
    List imageURL = new ArrayList();
    String filePath = null;
    String fileName = null;
    List filenames = new ArrayList();
    // TODO: Rename and change types of parameters
    private Place mParam1;
    private Object mParam2;
    private GoogleMap mGoogleMap;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    GPSCoOrdinate gpsCoOrdinate = new GPSCoOrdinate();
    Restaurants restaurants = null;
    PaidActivities activities = null;
    private OnFragmentInteractionListener mListener;
    GoogleApiClient mGoogleApiClient;
    DataBaseHelper dataBaseHelper;
    private Button viewPhotos=null;

    public MapActivitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment MapActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapActivitiesFragment newInstance(Place place) {
        MapActivitiesFragment fragment = new MapActivitiesFragment();
        Bundle args = new Bundle();
        if (place != null)
        {
            args.putParcelable(ARG_PARAM1, (Parcelable) place);
        }


       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MapActivitiesFragment newInstance(Place place , Object object) {
        MapActivitiesFragment fragment = new MapActivitiesFragment();
        Bundle args = new Bundle();
        if (place != null)
        {
            args.putParcelable(ARG_PARAM1, (Parcelable) place);
        }
        args.putSerializable(ARG_PARAM2, (Serializable) object);

        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MapActivitiesFragment newInstance(Object object) {
        MapActivitiesFragment fragment = new MapActivitiesFragment();
        Bundle args = new Bundle();


        args.putSerializable(ARG_PARAM2, (Serializable) object);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MapActivitiesFragment newInstance() {
        MapActivitiesFragment fragment = new MapActivitiesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Place) getArguments().getParcelable(ARG_PARAM1);
            mParam2 = (Object) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_rest);
        TextView save = (TextView) toolbar.findViewById(R.id.restaurant_toolbar_save);
        TextView cancel = (TextView) toolbar.findViewById(R.id.toolbar_cancel);
        TextView titleheading = (TextView) toolbar.findViewById(R.id.titleheading);
        titleheading.setTypeface(dinlight);
        save.setTypeface(dinlight);
        cancel.setTypeface(dinlight);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        place  = (Place) getArguments().getParcelable(ARG_PARAM1);

        Typeface custom_font = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        final EditText name = (EditText) view.findViewById(R.id.namefield);
        final EditText website = (EditText) view.findViewById(R.id.websitetext);
        final EditText phoneNumber = (EditText) view.findViewById(R.id.phonenumber);
        final EditText description = (EditText) view.findViewById(R.id.descriptiontext);
        final EditText tagstext = (EditText) view.findViewById(R.id.tagstext);
        viewPhotos = (Button)view.findViewById(R.id.viewphotos);
        if (mParam2 != null && mParam2 instanceof  Restaurants)
        {
            if (((Restaurants) mParam2).getName()!=null)
            {
                viewPhotos.setVisibility(View.VISIBLE);
                viewPhotos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction().
                                replace(R.id.restaurantcontainer, ViewPhotosFragment.newInstance(mParam2))
                                .addToBackStack(null).commit();
                    }
                });
            }
            icon.setImageResource(R.drawable.restaurantname);
            name.setText(((Restaurants) mParam2).getName());
            if (((Restaurants) mParam2).getGps()!=null)
            {
                gpsCoOrdinate = ((Restaurants) mParam2).getGps();
            }
            if (((Restaurants) mParam2).getWebsite() !=null)
                website.setText(((Restaurants) mParam2).getWebsite());
            phoneNumber.setText(((Restaurants) mParam2).getContactNumber());
            description.setText(((Restaurants) mParam2).getDescription());
            if(((Restaurants) mParam2).getTags()!=null) {

                List<String> tempTags = ((Restaurants) mParam2).getTags();
                if (((Restaurants) mParam2).getTags() != null) {
                    for (String s : tempTags) {
                        if (!s.equals("")) {
                            tagstext.append(s + ",");
                        }
                    }
                }
            }
        }
        else if (mParam2 != null && mParam2 instanceof  PaidActivities)
        {

            if (((PaidActivities) mParam2).getName()!=null)
            {
                viewPhotos.setVisibility(View.VISIBLE);
                viewPhotos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction().
                                replace(R.id.paidcontainer, ViewPhotosFragment.newInstance(mParam2))
                                .addToBackStack(null).commit();
                    }
                });
            }

            icon.setImageResource(R.drawable.paidactivityicon);
            name.setText(((PaidActivities) mParam2).getName());
            if (((PaidActivities) mParam2).getGps()!=null)
                gpsCoOrdinate = ((PaidActivities) mParam2).getGps();
            if (((PaidActivities) mParam2).getWebsite() !=null)
                website.setText(((PaidActivities) mParam2).getWebsite());
            phoneNumber.setText(((PaidActivities) mParam2).getContactnumber());
            description.setText(((PaidActivities) mParam2).getDescription());

            if (((PaidActivities) mParam2).getTags()!=null) {
                List<String> tempTags = ((PaidActivities) mParam2).getTags();
                if (((PaidActivities) mParam2).getTags() != null) {
                    for (String s : tempTags) {
                        if (!s.equals("")) {
                            tagstext.append(s + ",");
                        }
                    }
                }
            }
        }

        name.setTypeface(dinlight);
        website.setTypeface(dinlight);
        description.setTypeface(dinlight);
        phoneNumber.setTypeface(dinlight);
        tagstext.setTypeface(dinlight);
        TextView text = (TextView) view.findViewById(R.id.textView);
        TextView text1 = (TextView) view.findViewById(R.id.longitudetext);
        TextView text2 = (TextView) view.findViewById(R.id.numberfield);
        TextView text3 = (TextView) view.findViewById(R.id.photostext);
        TextView text4 = (TextView) view.findViewById(R.id.tagsfield);
        TextView text5 = (TextView) view.findViewById(R.id.textView2);
        text.setTypeface(montserrat);
        text1.setTypeface(montserrat);
        text2.setTypeface(montserrat);
        text3.setTypeface(montserrat);
        text4.setTypeface(montserrat);
        text5.setTypeface(montserrat);
        if (place != null) {
            name.setText(place.getName());
            if (place.getWebsiteUri() != null)
                website.setText(place.getWebsiteUri().toString());
            if (place.getPhoneNumber() != null)
                phoneNumber.setText(place.getPhoneNumber().toString());
            if (place.getLatLng()!=null) {
                gpsCoOrdinate.setLattitude(String.valueOf(place.getLatLng().latitude));
                gpsCoOrdinate.setLongitude(String.valueOf(place.getLatLng().longitude));
            }
        }
        gallery = (ImageView)view.findViewById(R.id.galleryaddicon);
        cam = (ImageView)view.findViewById(R.id.cameraaddicon);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    if(PermUtility.checkCameraPermission(getContext())){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }else{
                        ToastMessage(" Give Camera Permissions");
                    }
                }catch(Exception ex){
                    Log.d("AddTrip","Exception starting camera activity:"+ex);
                    ToastMessage(" Failed to start Camera");
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   //getFragmentManager().popBackStackImmediate();
                   getActivity().finish();
               }
               catch (Exception e)
               {
                   Log.d("MapActivitiesFragment","Error on cancel"+e.getStackTrace());
               }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(PermUtility.checkReadPermission(getContext())){
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
                    }else{
                        ToastMessage(" Give Gallery Permissions");
                    }


                }catch(Exception ex){
                    Log.d("AddTrip","Exception starting gallery activity:"+ex);
                    ToastMessage(" Failed to Open Gallery");
                }
            }
        });

        if (save!=null) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String descriptionText = description.getText().toString();
                    final String restautantName = name.getText().toString();
                    final String contactNumber = phoneNumber.getText().toString();
                    final String weburl = website.getText().toString();
                    String[] tags = tagstext.getText().toString().split(",");
                    final List taglist = new ArrayList<>();
                    if(tags.length>0) {
                        for (int i = 0; i < tags.length; i++) {
                            tags[i] = tags[i].trim();
                            taglist.add(tags[i]);
                        }
                    }
                    if (getActivity().getClass().getSimpleName().equals("RestaurantsActivity")) {
                        restaurants = (Restaurants) getArguments().getSerializable(ARG_PARAM2);
                        int position = -1;
                        Restaurants temp =null;
                        Bundle extras = getActivity().getIntent().getExtras();
                        if (extras!=null) {
                            position = (int) extras.get("restoposition");
                        }
                            if (position > -1)
                        {
                           temp = AppContext.restaurantsList.remove(position);
                        }
                        restaurants.setDescription(descriptionText);
                       restaurants.setContactnumber(contactNumber);
                        if(taglist.size()>0){
                            restaurants.setTags(taglist);
                        }else{
                            restaurants.setTags(null);
                        }

                        restaurants.setName(restautantName);
                        restaurants.setWebsite(weburl);


                        restaurants.setGps(gpsCoOrdinate);
                        if(restaurants.getName()!=null && restaurants.getContactnumber()!=null && restaurants.getDescription()!=null
                                && restaurants.getTags()!=null && restaurants.getImageUrls()!=null  ){
                            restaurants.setreadyForReview(true);
                        }
                        AppContext.restaurantsList.add(restaurants);
                        AppContext.completeTrip.setRestaurants(AppContext.restaurantsList);

                        if(filePath!=null){
                            uploadImagesToServer(restaurants);
                        }
                        else if (restaurants.getImageUrls().size()>0)
                        {
                            if(AppContext.CALL)
                                CreateTripServerCall();
                            else
                                UpdateTripServerCall(restaurants);
                        }
                        else
                        {

                            AppContext.restaurantsList.remove(restaurants);
                            if (temp!=null)
                                AppContext.restaurantsList.add(temp);
                            ToastMessage("Please upload an image");
                        }
                    } else {
                        activities = (PaidActivities) getArguments().getSerializable(ARG_PARAM2);
                        int position = -1;
                        PaidActivities temp = null;
                        Bundle extras = getActivity().getIntent().getExtras();
                        if (extras!=null){
                            position = (int) extras.get("activityposition");
                        }

                        if (position > -1)
                        {
                            temp =AppContext.activitiesList.remove(position);
                        }
                        activities.setDescription(descriptionText);
                        activities.setContactnumber(contactNumber);
                        if(taglist.size()>0){
                            activities.setTags(taglist);
                        }else{
                            activities.setTags(null);
                        }
                        activities.setName(restautantName);
                        activities.setWebsite(weburl);
                        if(activities.getName()!=null && activities.getContactnumber()!=null && activities.getDescription()!=null
                                && activities.getTags()!=null && activities.getImageUrls()!=null  ){
                            activities.setreadyForReview(true);
                        }
                        AppContext.activitiesList.add(activities);
                        AppContext.completeTrip.setPaidActivities(AppContext.activitiesList);

                        if(filePath!=null){
                            uploadImagesToServer(activities);
                        }
                        else if(activities.getImageUrls().size()>0)
                        {
                            if (AppContext.CALL)
                                CreateTripServerCall();
                            else
                                UpdateTripServerCall(activities);
                        }
                        else
                        {
                            AppContext.activitiesList.remove(activities);
                            if (temp!=null)
                                AppContext.activitiesList.add(temp);
                            ToastMessage("Please upload an Image");
                        }
                    }
                }
            });
        }
        return view;
    }

    private void UpdateTripServerCall(final Object object) {


        DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.completeTrip).toString());
        Log.d("AddNewGemFragment", "profile json"+gson.toJson(AppContext.completeTrip).toString());
        task.setAsyncComplete(new AsyncComplete() {
            @Override
            public void OnJsonAsyncCompleted(List<String> json) {
                try {
                    int flag = 0;
                    if (object instanceof Restaurants || object instanceof PaidActivities) {
                        flag = 1;
                        Gson gson = new Gson();
                        if (json != null && json.size() > 0) {
                            JSONObject responseModel = new JSONObject(json.get(0));
                            JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                            String status = responseModel.getString("status");
                            if (status != null && status.equalsIgnoreCase("200")) {
                                JSONObject profObj = jsonArray.getJSONObject(0);
                                if (flag == 0) {
                                    CompleteTrip ctrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                                    if (ctrip != null) {
                                        Log.d("MapActivitiesFragment", "updated trip:" + gson.toJson(ctrip).toString());
                                        ToastMessage("Trip updated successfully!");
                                        AppContext.completeTrip.copyFromTrip(ctrip);

                                    } else {
                                        ToastMessage("Failed to update trip! try again later..");
                                        Log.d("MapActivitiesFragment", "Failed to update");
                                    }
                                } else {
                                    CompleteTrip ctrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                                    if (ctrip != null) {
                                        Log.d("MapActivitiesFragment", "updated profile:" + gson.toJson(ctrip).toString());
                                        ToastMessage("Trip updated successfully!");
                                        AppContext.completeTrip.copyFromTrip(ctrip);

                                    }
                                }
                                UpdateProfileServerCall();
                            } else {
                                ToastMessage("Failed to update Profile! try again later..");
                                Log.d("AddNewGemFragment", "Failed to update");
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("MapActivitiesFragment", "Exception converting profile from json:" + ex);
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
                                Log.d("MapActivitiesFragment", "updated profile:"+gson.toJson(sProfile).toString());
                                Log.d("MapActivitiesFragment","Profile updated successfully!");
                                AppContext.profile.copyFromProfile(sProfile);
                                getFragmentManager().popBackStackImmediate();
                                getActivity().finish();
                            }
                        }else{
                            ToastMessage("Failed to update Profile! try again later..");
                        }
                    }
                    else
                    {
                        ToastMessage("Failed to update Profile! try again later..");
                    }
                }
                catch (Exception ex) {
                    Log.d("ProfileActivity", "Exception converting profile from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_PROFILE_URL);
    }

    private void CreateTripServerCall() {

        DownloadJsonAsyncPostTask task = new DownloadJsonAsyncPostTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.completeTrip).toString());
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
                            if(ctrip!=null){
                                Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(ctrip).toString());
                                ToastMessage("Trip created/updated successfully!");
                                AppContext.completeTrip.copyFromTrip(ctrip);
                                boolean insertflag = MainActivity.dataBaseHelper.insertTripNameAndID("MYTRIPS",ctrip.getCreatorId(),ctrip.getId(),ctrip.getName());
                                if(insertflag){
                                    Log.d("MapActivitiesFragment" ,"Inserted into Local Successfully");
                                }
                                else{
                                    Log.d("MapActivitiesFragment" ,"Could not insert into Local database");
                                }
                                List<String> temp = null;
                                if (AppContext.profile.getTripsCompletedIds() != null)
                                    temp = AppContext.profile.getTripsCompletedIds();
                                else
                                    temp = new ArrayList<>();
                                temp.add(ctrip.getId());
                                UpdateProfileServerCall();
                                if(AppContext.CALL){
                                    AppContext.CALL = false;
                                }

                            }
                        }else{
                            ToastMessage("Failed to update Trip! try again later..");
                            Log.d("AddNewGemFragment", "Failed to update");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("MapActivitiesFragment", "Exception converting trip from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);
    }
//    private void uploadImagesToServer(final Object object) {
//
//        AsyncUploadImage imageUpload = new AsyncUploadImage(new AsyncComplete() {
//            @Override
//            public void OnJsonAsyncCompleted(List<String> json) {
//                try {
//                    if (json != null && json.size() > 0) {
//                        JSONObject responseModel = new JSONObject(json.get(0));
//                        String message = responseModel.getString("message");
//                        String status = responseModel.getString("status");
//                        if (status != null && status.equalsIgnoreCase("200")) {
//                            if (object instanceof Restaurants) {
//                                restaurants.setImageUrls(Arrays.asList(AppContext.BASE_IMAGE_URL+fileName));
//                                if(AppContext.CALL){
//                                    CreateTripServerCall();
//                                }else{
//                                    UpdateTripServerCall(restaurants);
//                                }
//                            }
//                            else
//                            {
//                                activities.setImageUrls(Arrays.asList(AppContext.BASE_IMAGE_URL+fileName));
//                                if(AppContext.CALL){
//                                    CreateTripServerCall();
//                                }else{
//                                    UpdateTripServerCall(activities);
//                                }
//                            }
//
//                        }
//                        else {
//                            ToastMessage("Failed to upload image to server!! Try again later!");
//                            Log.d("MapActivitiesFragment", "Message from server"+message);
//
//                        }
//
//                        Log.d("MapActivitiesFragment", "image uploaded..filename"+fileName);
//                    }
//                } catch (Exception ex) {
//                    ToastMessage("Failed to upload image to server!! Try again later!");
//                    Log.d("MapActivitiesFragment", "Exception converting search trips from json:" + ex);
//                }
//            }
//        });
//        imageUpload.setFilePath(filePath);
//        imageUpload.execute(AppContext.IMAGE_UPLOAD_URL);
//
//    }
//
    //Multiple Images Upload
    private void uploadImagesToServer(final Object object) {

        MultipleImageUploadAsyncTask imageUpload = new MultipleImageUploadAsyncTask(new AsyncComplete() {
            @Override
            public void OnJsonAsyncCompleted(List<String> json) {
                try {
                    if (json != null && json.size() > 0) {
                        JSONObject responseModel = new JSONObject(json.get(0));
                        String message = responseModel.getString("message");
                        String status = responseModel.getString("status");
                        if (status != null && status.equalsIgnoreCase("200")) {
                            if (object instanceof Restaurants) {
                                //restaurants.setImageUrls(Arrays.asList(AppContext.BASE_IMAGE_URL+fileName));
                                List temp = new ArrayList<>();
                                if (restaurants.getImageUrls().size()>0)
                                {
                                    temp = restaurants.getImageUrls();
                                }
                                for (int i=0;i<filenames.size();i++)
                                {
                                    temp.add(AppContext.BASE_IMAGE_URL+filenames.get(i));
                                }
                                restaurants.setImageUrls(temp);
                                if(AppContext.CALL){
                                    CreateTripServerCall();
                                }else{
                                    UpdateTripServerCall(restaurants);
                                }
                            }
                            else
                            {
                                //activities.setImageUrls(Arrays.asList(AppContext.BASE_IMAGE_URL+fileName));
                                List temp = new ArrayList<>();
                                if (activities.getImageUrls().size()>0)
                                {
                                    temp = activities.getImageUrls();
                                }
                                for (int i=0;i<filenames.size();i++)
                                {
                                    temp.add(AppContext.BASE_IMAGE_URL+filenames.get(i));
                                }
                                activities.setImageUrls(temp);
                                if(AppContext.CALL){
                                    CreateTripServerCall();
                                }else{
                                    UpdateTripServerCall(activities);
                                }
                            }

                        }
                        else {
                            ToastMessage("Failed to upload image to server!! Try again later!");
                            Log.d("MapActivitiesFragment", "Message from server"+message);

                        }

                        Log.d("MapActivitiesFragment", "image uploaded..filename"+fileName);
                    }
                } catch (Exception ex) {
                    ToastMessage("Failed to upload image to server!! Try again later!");
                    Log.d("MapActivitiesFragment", "Exception converting search trips from json:" + ex);
                }
            }
        });
        imageUpload.setFilePath(imageURL);
        imageUpload.execute(AppContext.IMAGE_UPLOAD_URL);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        try{

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            createFileFromBitmap(thumbnail);
        }catch(Exception ex){
            Log.d("MapActivitiesFragment", "error capturing from camera"+ex);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    createFileFromBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            Log.d("MapActivitiesFragment", "error capturing from gallery" + ex);
        }
    }

    private void createFileFromBitmap(Bitmap bm) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            Log.d("Utility", "onCaptureImageResult, writing image to disk..");

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            filePath = destination.getPath();
            fileName = destination.getName();
            Log.d("AddTrip", "Image file created at" + filePath);
            ToastMessage("Photos Added Successfully");
            imageURL.add(filePath);
            filenames.add(fileName);
        } catch (Exception ex) {
            Log.d("MapActivitiesFragment", "runtime error caught:" + ex);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void ToastMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
