package com.raveltrips.contentcreator.Fragments;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.Utils.TripCreatorData;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPostTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoUploadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoUploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoUploadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List tripvideos = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VideoUploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //* @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment VideoUploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoUploadFragment newInstance() {
        VideoUploadFragment fragment = new VideoUploadFragment();

        //fragment.setArguments(args);
        return fragment;
    }
    public static VideoUploadFragment newInstance(TripCreatorData tripData) {
        VideoUploadFragment fragment = new VideoUploadFragment();
        Bundle args = new Bundle();
       // args.putSerializable("TripData", (Serializable) tripData);

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_video_upload, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        TextView url = (TextView) rootview.findViewById(R.id.urlcontainer);
        TextView videostring = (TextView) rootview.findViewById(R.id.video);
        TextView upload = (TextView) rootview.findViewById(R.id.upload);
        final EditText edittext = (EditText) rootview.findViewById(R.id.urltext);
        url.setTypeface(montserrat);
        videostring.setTypeface(dinlight);
        edittext.setTypeface(dinlight);
        upload.setTypeface(dindemi);
        Button button = (Button) rootview.findViewById(R.id.videosave);
        button.setTypeface(dinlight);
        tripvideos = AppContext.completeTrip.getVideoUrls();

        if (tripvideos !=null)
        {
            for (int i=0;i<tripvideos.size();i++)
            {
                if (i==0)
                {
                    edittext.setText(tripvideos.get(i)+"\n");
                }
                else {
                    edittext.append((CharSequence) tripvideos.get(i) + "\n");
                }
            }
        }
        rootview.findViewById(R.id.videosave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List videoList = new ArrayList();
                String videourl = edittext.getText().toString();
                String[] vidurl = videourl.split("\n");
                for (int i=0;i<vidurl.length;i++)
                {
                    if (tripvideos!= null)
                    {
                            tripvideos.clear();
                            tripvideos.add(vidurl[i]);
                            AppContext.completeTrip.setVideoUrls(tripvideos);


                    }
                    else
                    {
                        videoList.add(vidurl[i]);
                        AppContext.completeTrip.setVideoUrls(videoList);
                    }

                }

            //update server call
                if(AppContext.CALL){
                    CreateTripServerCall();
                }else {
                    UpdateTripServerCall();
                }
            }
        });



        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void UpdateTripServerCall() {

        DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.completeTrip).toString());
        Log.d("AddNewGemFragment", "profile json"+gson.toJson(AppContext.completeTrip).toString());
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
                                Log.d("VideoUpload", "updated trip:"+gson.toJson(ctrip).toString());
                                ToastMessage("Trip updated successfully!");
                                AppContext.completeTrip.copyFromTrip(ctrip);
                                UpdateProfileServerCall();
                            }
                        }else{
                            ToastMessage("Failed to update Profile! try again later..");
                            Log.d("AddNewGemFragment", "Failed to update");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("AddNewGemFragment", "Exception converting profile from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);
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
                            CompleteTrip completeTrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                            if(completeTrip!=null){
                                Log.d("VideoUpload", "updated trip:"+gson.toJson(completeTrip).toString());
                                ToastMessage("Trip created/updated successfully!");
                                AppContext.completeTrip.copyFromTrip(completeTrip);
                                boolean flag = MainActivity.dataBaseHelper.insertTripNameAndID("MYTRIPS",completeTrip.getCreatorId(),completeTrip.getId(),completeTrip.getName());
                                if(flag){
                                    Log.d("VideoUpload" ,"Inserted into Local Successfully");
                                }
                                else{
                                    Log.d("VideoUpload" ,"Could not insert into Local database");
                                }
                                List<String> temp = null;

                                if (AppContext.profile.getTripsCompletedIds() != null)
                                    temp = AppContext.profile.getTripsCompletedIds();
                                else
                                    temp = new ArrayList<>();
                                AppContext.profile.setTripsCompletedIds(temp);
                                if(AppContext.CALL){
                                    AppContext.CALL = false;
                                }
                                UpdateProfileServerCall();


                            }
                        }else{
                            ToastMessage("Failed to update Trip! try again later..");
                            Log.d("AddNewGemFragment", "Failed to update");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("AddNewGemFragment", "Exception converting trip from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);
    }

    private void ToastMessage(String s) {
        Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

                                AppContext.profile.copyFromProfile(sProfile);
                                getFragmentManager().popBackStackImmediate();
                            }
                        }else{
                            ToastMessage("Failed to update Profile! try again later..");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("ProfileActivity", "Exception converting profile from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_PROFILE_URL);
    }
}
