package com.raveltrips.contentcreator.Fragments.OfflineFragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
 * {@link Offline_AddNewGemFragment} interface
 * to handle interaction events.
 * Use the {@link Offline_AddNewGemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offline_AddNewGemFragment extends Fragment implements DialogInterface.OnCancelListener,DialogInterface.OnKeyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Boolean locationReceived = false;
    ProgressDialog dialog;
    // TODO: Rename and change types of
    // parameters
    private String mParam1;
    private String mParam2;
    Toolbar toolbar;
    TextView save,cancel;
    EditText name,lat,lon,des;
  /*  ProgressBar progressBar;*/

 //   private OnFragmentInteractionListener mListener;

    public Offline_AddNewGemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offline_AddNewGemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Offline_AddNewGemFragment newInstance(String param1, String param2) {
        Offline_AddNewGemFragment fragment = new Offline_AddNewGemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View rootView = inflater.inflate(R.layout.fragment_offline__add_new_gem, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        final Handler handler = new Handler();

         lat = (EditText) rootView.findViewById(R.id.latitude_offline);
         lon = (EditText) rootView.findViewById(R.id.longitude_offline);
        lat.setTypeface(dinlight);
        lon.setTypeface(dinlight);
      /*  progressBar = (ProgressBar) rootView.findViewById(R.id.progresscheck);*/

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                dialog.dismiss();
                lat.setText(String.valueOf(location.getLatitude()));
                lon.setText(String.valueOf(location.getLongitude()));
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
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_rest);
        toolbar.setVisibility(View.VISIBLE);
        save = (TextView) toolbar.findViewById(R.id.toolbar_save_offline);
        cancel = (TextView) toolbar.findViewById(R.id.toolbar_cancel_offline);
        name = (EditText) rootView.findViewById(R.id.gemname_offline);
        des = (EditText) rootView.findViewById(R.id.gemdescription_offline);
        save.setTypeface(dinlight);
        cancel.setTypeface(dinlight);
        name.setTypeface(dinlight);
        des.setTypeface(dinlight);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (locationReceived){

                    CompleteTrip selectedTrip = (CompleteTrip) AppContext.offlineTripsList.get(AppContext.offlineSelectedTripPosition);
                boolean success =  MainActivity.dataBaseHelper.insert("HIDDENGEMS",selectedTrip.getId() ,selectedTrip.getName(),
                        name.getText().toString(), lat.getText().toString(),lon.getText().toString(),des.getText().toString(),null,null,selectedTrip.getCreatorId());
                if(success){
                    Toast.makeText(getContext(), "Updated Gem Successfully", Toast.LENGTH_SHORT)
                            .show();
                    getFragmentManager().popBackStack();
                }
                else{
                    Toast.makeText(getContext(), "Oops couldn't do that operation try again!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
                else
                {

                    Toast.makeText(getContext(), "Waiting for Location", Toast.LENGTH_SHORT)
                            .show();
                }
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


/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
   /*     if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (toolbar!=null)
            toolbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCancel(DialogInterface dialog) {


        //getFragmentManager().popBackStack();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    return;

                }
                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
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
