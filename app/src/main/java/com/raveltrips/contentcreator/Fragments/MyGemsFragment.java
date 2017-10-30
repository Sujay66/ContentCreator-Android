package com.raveltrips.contentcreator.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raveltrips.contentcreator.Adapters.HiddenGemsAdapter;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Profile;
import com.raveltrips.contentcreator.models.Restaurants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGemsFragment extends Fragment {
    private OnFragmentInteractionList mListener;
    Toolbar mToolBar, topToolBar;
    TextView gemcount;
    SearchView search;
    HiddenGemsAdapter adapter;
    int mParam1;
    List<?> traverse;
    boolean sflag = true;

    RecyclerView MyTripsRecycler;
    private static final String ARG_PARAM1 = "param1";
    LinearLayoutManager layoutManager;
  //  String[] options = new String[]{"Home", "Create New Trip", "My Trips", "Trip Creation GuideLines", "Logout", "Profile"};

    public MyGemsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);

        }
    }
    public static MyGemsFragment newInstance(int position) {
        MyGemsFragment fragment = new MyGemsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_gems, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");

        if(AppContext.completedTrips.size()!=0) {

            gemcount = (TextView) rootView.findViewById(R.id.numofgems);
            gemcount.setTypeface(montserrat);
            topToolBar = (Toolbar) rootView.findViewById(R.id.toolbar_top);
            search = (SearchView) rootView.findViewById(R.id.search_my_trips);

            adapter = new HiddenGemsAdapter(getContext(), AppContext.completedTrips , mParam1);
            MyTripsRecycler = (RecyclerView) rootView.findViewById(R.id.mygemsrecycler);
            MyTripsRecycler.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            MyTripsRecycler.setLayoutManager(layoutManager);
            MyTripsRecycler.setAdapter(adapter);
            //Set gem count
            try {
                if (mParam1 == 1) {
                    if (AppContext.selectedCompleteTrip.getPindrops() != null) {
                        gemcount.setText(Integer.toString(AppContext.selectedCompleteTrip.getPindrops().size()) + " HIDDEN GEMS");
                        traverse =   AppContext.selectedCompleteTrip.getPindrops();
                    } else
                        gemcount.setText("0");

                } else if (mParam1 == 4) {
                    if (AppContext.selectedCompleteTrip.getRestaurants() != null) {
                        gemcount.setText(Integer.toString(AppContext.selectedCompleteTrip.getRestaurants().size()) + " RESTAURANTS");
                        traverse =   AppContext.selectedCompleteTrip.getRestaurants();
                    }
                    else
                        gemcount.setText("0");
                } else {
                    if ((AppContext.selectedCompleteTrip.getPaidActivities() != null)) {
                        gemcount.setText(Integer.toString(AppContext.selectedCompleteTrip.getPaidActivities().size()) + " ACTIVITIES");
                        traverse =   AppContext.selectedCompleteTrip.getPaidActivities();
                    }
                    else
                        gemcount.setText("0");
                }
            }
            catch (Exception e)
            {
                Log.d("MyGemsFragment","Error in selectedtriplist"+e.getStackTrace());
            }
            final HiddenGemsAdapter.OnItemClickListener listener = new HiddenGemsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position , int selectedType) {
                    mListener.Sujay(v, position, 0, AppContext.completedTrips , selectedType);
                }

                @Override
                public void onEditClick(View v, int position,int selectedType) {
                    mListener.Sujay(v, position, 1, AppContext.completedTrips,selectedType);
                }

                @Override
                public void onDeleteClick(final View v, final int position, final int selectedType) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Are you sure to delete this gem?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mListener.Sujay(v, position, 2, AppContext.completedTrips , selectedType);
                                    adapter.notifyDataSetChanged();
                                    //Check for gem count after delete
                                    setItemCount(selectedType);

                                    UpdateTripServerCall();
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

                }

            };

            adapter.setListener(listener);


            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    int pos = findTrip(query,traverse);
                    if (pos > -1) {
                        try {
                            MyTripsRecycler.scrollToPosition(pos);
                            search.clearFocus();
                        } catch (Exception e) {
                            Log.d("MyGemsFragment", "Inside Search Exception");
                        }
                    } else {
                        Toast.makeText(getActivity(), "No Trips found", Toast.LENGTH_SHORT).show();
                        search.clearFocus();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }else{
            if (mParam1 ==1)
                Toast.makeText(getContext(),"You don't have any Hidden Gems",Toast.LENGTH_SHORT).show();
            else if (mParam1 ==4)
                Toast.makeText(getContext(),"You don't have any Restaurants",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(),"You don't have any Paid Activities",Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void setItemCount(int selectedType) {
        switch (selectedType)
        {
            case 1: gemcount.setText(String.valueOf(adapter.getItemCount()+ " HIDDEN GEMS"));
                break;
            case 4:gemcount.setText(String.valueOf(adapter.getItemCount()+" RESTAURANTS"));
                break;
            case 7:gemcount.setText(String.valueOf(adapter.getItemCount()+" ACTIVITIES"));
                break;
        }
    }

    private int findTrip(String query, List traverse)
    {

       for(Object trip:traverse) {

           if (trip instanceof Pindrop) {
               if ((boolean) ((Pindrop) trip).getName().toLowerCase().contains(query)) {
                   return traverse.indexOf(trip);
               }
           } else if (trip instanceof Restaurants) {
               if ((boolean) ((Restaurants) trip).getName().toLowerCase().contains(query)) {
                   return traverse.indexOf(trip);
               }
           } else {
               if ((boolean) ((PaidActivities) trip).getName().toLowerCase().contains(query)) {
                   return traverse.indexOf(trip);
               }
           }
       }
        return -1;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionList) {
            mListener = (OnFragmentInteractionList) context;
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
    public interface OnFragmentInteractionList {
        // TODO: Update argument type and name
        void Sujay(View v , int position , int flag, List<CompleteTrip> pindrops , int selectedType);
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
                                Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(ctrip).toString());
                                ToastMessage("Profile updated successfully!");
                                AppContext.completeTrip.copyFromTrip(ctrip);
                                sflag = false;
                                if(AppContext.CALL){
                                    AppContext.CALL = false;
                                }
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

    private void ToastMessage(String s) {
        Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
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
                                ToastMessage("Profile updated successfully!");
                                AppContext.profile.copyFromProfile(sProfile);
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

    @Override
    public void onResume() {
        if (adapter!=null)
            adapter.notifyDataSetChanged();
        super.onResume();
    }
}
