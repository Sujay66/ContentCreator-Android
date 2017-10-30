package com.raveltrips.contentcreator.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raveltrips.contentcreator.Adapters.TripCreatorAdapter;
import com.raveltrips.contentcreator.Adapters.TripCreatorAdapterTwo;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.MyTrips;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPostTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainTripCreatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainTripCreatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTripCreatorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tripName;
    // TODO: Rename and change types of parameters
    private int mParam1 = -1;
    private String mParam2;
    String fileName;
    String filePath;
    private OnFragmentInteractionListener mListener;

    public MainTripCreatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment MainTripCreatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTripCreatorFragment newInstance(int position) {
        MainTripCreatorFragment fragment = new MainTripCreatorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);

        fragment.setArguments(args);
        return fragment;
    }
    public static MainTripCreatorFragment newInstance() {
        MainTripCreatorFragment fragment = new MainTripCreatorFragment();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_main_trip_creator, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        ImageView topicon = (ImageView) rootView.findViewById(R.id.topicon);
        TextView submit = (TextView) rootView.findViewById(R.id.submit);
        submit.setTypeface(dinlight);
        topicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getFragmentManager().beginTransaction().replace(R.id.trip_container,new OptionsNavigatorFragment()).addToBackStack(null).commit();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("flag","tripcreator");
                startActivity(intent);
            }
        });
        TripCreatorAdapter adapter = new TripCreatorAdapter(getContext());
        RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        Typeface custom_font = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        TextView save =(TextView)rootView.findViewById(R.id.save);
        TextView tripContent =(TextView)rootView.findViewById(R.id.tripcontent);
        tripName =(TextView)rootView.findViewById(R.id.tripname);
        if (mParam1>=0)
            tripName.setText(AppContext.completedTrips.get(mParam1).getName().toString());
        else
            tripName.setText("New Trip");
        save.setTypeface(dinlight);
        tripContent.setTypeface(montserrat);
        tripName.setTypeface(dindemi);

        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
        TripCreatorAdapterTwo adapterTwo = new TripCreatorAdapterTwo(getContext());
        RecyclerView secondRecycler = (RecyclerView) rootView.findViewById(R.id.recycler2);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        secondRecycler.setLayoutManager(layoutManager1);
        secondRecycler.setAdapter(adapterTwo);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParam1>=0)
                {

                    if(AppContext.CALL){
                        CreateTripServerCall();
                    }else{
                        updateCompleteTripToServer(AppContext.completeTrip);
                    }
                }
                else if (AppContext.completeTrip.getName() == null || AppContext.completeTrip.getName().trim().length()<=0)
                {
                    ToastMessage("Please Enter a Name in Trip Overview to save");

                }
                else
                {
                    //update trip call

                            if(AppContext.CALL){
                                CreateTripServerCall();
                            }else{
                                updateCompleteTripToServer(AppContext.completeTrip);
                            }

                }
            }
        });
        TripCreatorAdapter.OnItemClickListener listener = new TripCreatorAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                mListener.onFragmentInteraction(v,position,1);
            }
        };
        adapter.setListener(listener);
        TripCreatorAdapterTwo.OnItemClickListener listener1 = new TripCreatorAdapterTwo.OnItemClickListener(){

            @Override
            public void onItemClick(View v, int position) {
                mListener.onFragmentInteraction(v,position,2);
            }
        };
        adapterTwo.setListener(listener1);
        rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppContext.completeTrip.setReadyForReview(true);
                AppContext.completeTrip.setRating(2.0);
                AppContext.completeTrip.setPrice(2.0);

                if (!AppContext.completeTrip.isNull()) {
                    if (AppContext.CALL)
                        CreateTripServerCall();
                    else
                        updateCompleteTripToServer(AppContext.completeTrip);

                }
                else
                {
                    ToastMessage("Please fill all Fields!");
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void updateCompleteTripToServer(CompleteTrip completeTrip) {



            try{
                DownloadJsonAsyncPutTask createCompleteTripTask = new DownloadJsonAsyncPutTask();

                final Gson gson = new Gson();
                AppContext.completeTrip.setRating(2.0);
                AppContext.completeTrip.setPrice(2.0);
                String body = gson.toJson(AppContext.completeTrip).toString();
                createCompleteTripTask.setBodyContent(body);
                createCompleteTripTask.setAsyncComplete(new AsyncComplete() {
                    @Override
                    public void OnJsonAsyncCompleted(List<String> jsons) {
                        try {
                            if (jsons != null && jsons.size() > 0) {
                                JSONObject responseModel = new JSONObject(jsons.get(0));
                                JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                                String status = responseModel.getString("status");
                                if (status != null && status.equalsIgnoreCase("200")) {
                                    JSONObject profObj = jsonArray.getJSONObject(0);
                                    CompleteTrip sProfile = gson.fromJson(profObj.toString(), CompleteTrip.class);
                                    if(sProfile!=null){
                                        Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(sProfile).toString());
                                        ToastMessage("Trip updated successfully!");
                                        AppContext.completeTrip.copyFromTrip(sProfile);
                                        AppContext.profile.setTripsCompletedIds(Arrays.asList(sProfile.getId()));
                                        AppContext.completeTrip = null;
                                        AppContext.restaurantsList.clear();
                                        AppContext.activitiesList.clear();
                                        AppContext.CALL = true;

                                    }
                                    UpdateProfileServerCall();
                                }else{
                                    ToastMessage("Failed to update Profile! try again later..");
                                    Log.d("MainTrip", "Failed to update");
                                }
                            }
                        } catch (Exception ex) {
                            Log.d("MainTripCreator", "Exception sending Complete trip to server:" + ex);
                            ToastMessage("Update Server Exception");
                        }
                    }
                });
                createCompleteTripTask.execute(AppContext.CREATE_COMPLETE_TRIP_URL);
            }catch (Exception ex){ ex.printStackTrace();}

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
        void onFragmentInteraction(View v , int position , int flag);
    }
    public void ToastMessage(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
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
                                Log.d("MainTripCreatorFragment", "updated profile:"+gson.toJson(sProfile).toString());
                                Log.d("MainTripCreatorFragment","Profile updated successfully!");
                                AppContext.profile.copyFromProfile(sProfile);
                            }
                            Intent intent = new Intent(getContext(), MyTrips.class);
                            startActivity(intent);

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




    private void CreateTripServerCall() {

        DownloadJsonAsyncPostTask task = new DownloadJsonAsyncPostTask();
        Gson gson = new Gson();
        AppContext.completeTrip.setRating(2.0);
        AppContext.completeTrip.setPrice(2.0);
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
                                ToastMessage("Trip created/updated successfully!");
                                AppContext.completeTrip.copyFromTrip(completeTrip);
                                List<String> temp = null;
                                if (AppContext.profile.getTripsCompletedIds() != null)
                                    temp = AppContext.profile.getTripsCompletedIds();
                                else
                                    temp = new ArrayList<>();
                                temp.add(completeTrip.getId());
                                AppContext.profile.setTripsCompletedIds(temp);
                                AppContext.completedTrips.add(completeTrip);
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

    @Override
    public void onResume() {
        if (AppContext.selectedCompleteTrip !=null ){
                tripName.setText(AppContext.selectedCompleteTrip.getName());
        }
        super.onResume();
    }
}
