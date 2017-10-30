package com.raveltrips.contentcreator.Fragments.OfflineFragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.Utils.PermUtility;
import com.raveltrips.contentcreator.models.CompleteTrip;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Offline_MapActivitiesFragment} interface
 * to handle interaction events.
 * Use the {@link Offline_MapActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offline_MapActivitiesFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnKeyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Boolean locationReceived =false;
    Dialog dialog;
    String lat,longitude;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Toolbar toolbar;
    EditText name =null;
    EditText phone = null;
    EditText web=null, desc = null;
   // private OnFragmentInteractionListener mListener;
   public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public Offline_MapActivitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment Offline_MapActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Offline_MapActivitiesFragment newInstance(String param1) {
        Offline_MapActivitiesFragment fragment = new Offline_MapActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_offline__map_activities, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        name = (EditText) rootView.findViewById(R.id.offlinenamefield);
         desc = (EditText) rootView.findViewById(R.id.descriptiontext);
         phone= (EditText) rootView.findViewById(R.id.phonenumber);
         web= (EditText) rootView.findViewById(R.id.websitetext);
        name.setTypeface(dinlight);
        desc.setTypeface(dinlight);
        phone.setTypeface(dinlight);
        web.setTypeface(dinlight);
        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar_rest);
        toolbar.setVisibility(View.VISIBLE);
        TextView save = (TextView) toolbar.findViewById(R.id.toolbar_save_offline);
        TextView cancel = (TextView) toolbar.findViewById(R.id.toolbar_cancel_offline);
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                dialog.dismiss();
                lat = (String.valueOf(location.getLatitude()));
                longitude = (String.valueOf(location.getLongitude()));

                locationReceived = true;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (PermUtility.checkFineLocationPermission(getContext())) {
            if (!locationReceived) {


                dialog = ProgressDialog.show(getActivity(), "Searching for Location",
                        "Please Wait", true, true, this);
                dialog.setOnKeyListener(this);
            }

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            // requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
            save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationReceived) {
                    insert(mParam1);
                    getFragmentManager().popBackStack();
                }
                else
                    Toast.makeText(getContext(), "Waiting for Location", Toast.LENGTH_SHORT)
                            .show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return rootView;
    }

    private void insert(String mParam1) {
        CompleteTrip selectedTrip = (CompleteTrip) AppContext.offlineTripsList.get(AppContext.offlineSelectedTripPosition);
        boolean success =  MainActivity.dataBaseHelper.insert(mParam1,selectedTrip.getId() ,selectedTrip.getName(),
                name.getText().toString(), lat,longitude,desc.getText().toString(),web.getText().toString(),phone.getText().toString(),selectedTrip.getCreatorId());
        if(success){
            Toast.makeText(getContext(), "Updated"+mParam1+" Successfully", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Toast.makeText(getContext(), "Oops couldn't do that operation try again!", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            *//*mListener.onFragmentInteraction(uri);*//*
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    /*    if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        dialog.dismiss();
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            getFragmentManager().popBackStack();
        }
        return false;
    }

/*    *//**//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//**//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
