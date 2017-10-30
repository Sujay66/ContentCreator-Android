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
 * {@link TripOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TripOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripOverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "TripData";
    private static final String ARG_PARAM2 = "param2";
    String tripName = null;
    String tripDescription = null;
    String destination = null;
    String tripLength = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TripCreatorData tripData = null;
    public TripOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment TripOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripOverviewFragment newInstance() {
        TripOverviewFragment fragment = new TripOverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public static TripOverviewFragment newInstance(TripCreatorData tripData) {
        TripOverviewFragment fragment = new TripOverviewFragment();
        Bundle args = new Bundle();
        //args.putSerializable(ARG_PARAM1, (Serializable) tripData);

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
        View rootview =  inflater.inflate(R.layout.fragment_trip_overview, container, false);
        //tripData = (TripCreatorData) getArguments().getSerializable(ARG_PARAM1);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        final EditText titletext = (EditText) rootview.findViewById(R.id.titletext);
        final EditText descriptiontext = (EditText) rootview.findViewById(R.id.descriptiontext);
        final EditText lengthtext = (EditText) rootview.findViewById(R.id.lengthtext);
        final EditText destinationtext = (EditText) rootview.findViewById(R.id.destinationtext);
        TextView title = (TextView) rootview.findViewById(R.id.titlecontainer);
        TextView destinatoin = (TextView) rootview.findViewById(R.id.destination);
        TextView triplength = (TextView) rootview.findViewById(R.id.triplength);
        TextView description = (TextView) rootview.findViewById(R.id.description);
        TextView overview = (TextView) rootview.findViewById(R.id.overview);
        title.setTypeface(montserrat);
        destinatoin.setTypeface(montserrat);
        triplength.setTypeface(montserrat);
        description.setTypeface(montserrat);
        titletext.setTypeface(dinlight);
        descriptiontext.setTypeface(dinlight);
        lengthtext.setTypeface(dinlight);
        destinationtext.setTypeface(dinlight);
        overview.setTypeface(dindemi);
        tripName = AppContext.completeTrip.getName();
        tripDescription = AppContext.completeTrip.getDescription();
        Button save = (Button) rootview.findViewById(R.id.overviewsave);
        save.setTypeface(dinlight);
        if (tripName !=null &&tripDescription!=null )
        {
            titletext.setText(tripName);
            descriptiontext.setText(tripDescription);
        }
        if (AppContext.selectedCompleteTrip !=null )
        {
            AppContext.completeTrip = AppContext.selectedCompleteTrip;
            tripName = AppContext.selectedCompleteTrip.getName();
            tripDescription = AppContext.selectedCompleteTrip.getDescription();
            if (tripDescription!=null)
                descriptiontext.setText(tripDescription.toString());
            if (AppContext.selectedCompleteTrip.getTripLength()!=null)
                lengthtext.setText(AppContext.selectedCompleteTrip.getTripLength());
            if (AppContext.selectedCompleteTrip.getLocation()!=null)
                destinationtext.setText(AppContext.selectedCompleteTrip.getLocation().toString());
            if (tripName!=null)
                titletext.setText(tripName.toString());


        }
        rootview.findViewById(R.id.overviewsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.completeTrip.setName(titletext.getText().toString());
                AppContext.completeTrip.setDescription(descriptiontext.getText().toString());
                AppContext.completeTrip.setLocation(destinationtext.getText().toString());
                try {
                    AppContext.completeTrip.setTripLength((lengthtext.getText().toString()));
                }

                //Add Server call to updatetrip

                catch (Exception e)
                {

                }
                if(AppContext.CALL){
                    CreateTripServerCall();
                }else{
                    UpdateTripServerCall();
                }

               // UpdateTripServerCall();
            }
        });



        return rootview;
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
                            CompleteTrip sProfile = gson.fromJson(profObj.toString(), CompleteTrip.class);
                            if(sProfile!=null){
                                Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(sProfile).toString());
                                ToastMessage("Trip updated successfully!");
                                AppContext.completeTrip.copyFromTrip(sProfile);
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
                                Log.d("TripOverview", "updated profile:"+gson.toJson(sProfile).toString());
                                Log.d("TripOverview","Profile updated successfully!");
                                AppContext.profile.copyFromProfile(sProfile);
                              getFragmentManager().popBackStackImmediate();
                            }
                        }else{
                            ToastMessage("Failed to update Profile! try again later..");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("TripOverview", "Exception converting profile from json:" + ex);
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
                            CompleteTrip completeTrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                            if(completeTrip!=null){
                                Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(completeTrip).toString());
                                //ToastMessage("Trip created/updated successfully!");
                                AppContext.completeTrip.copyFromTrip(completeTrip);
                                boolean flag = MainActivity.dataBaseHelper.insertTripNameAndID("MYTRIPS",completeTrip.getCreatorId(),completeTrip.getId(),completeTrip.getName());
                                if(flag){
                                    Log.d("TripOverView" ,"Inserted into Local Successfully");
                                }
                                else{
                                    Log.d("TripOverView" ,"Could not insert into Local database");
                                }
                                List<String> temp = null;

                                if (AppContext.profile.getTripsCompletedIds() != null)
                                    temp = AppContext.profile.getTripsCompletedIds();
                                else
                                    temp = new ArrayList<>();
                                temp.add(AppContext.completeTrip.getId());
                                AppContext.profile.setTripsCompletedIds(temp);
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
                    Log.d("AddNewGemFragment", "Exception converting trip from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);
    }

    private void ToastMessage(String s) {
        Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
