package com.raveltrips.contentcreator.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.models.GPSCoOrdinate;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Restaurants;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 */
public class HiddenGemsFragment extends Fragment  implements LocationListener {

    MapView mMapView;
    private GoogleMap googleMap;
/*    Toolbar mToolBar;*/
    Button update,cancel;
    LocationManager locationManager;
    Location loc;
    private boolean canGetLocation;
    LocationListener listener;
    private static final String ARG_PARAM1 = "param1";
    private Object mParam1;
    Pindrop pin = null;
    Restaurants restaurants = null;
    PaidActivities activities = null;
    LatLng position;
    GPSCoOrdinate gps = new GPSCoOrdinate();
    public HiddenGemsFragment() {
        // Required empty public constructor
    }

    public static HiddenGemsFragment newInstace(Object object) {
        HiddenGemsFragment fragment = new HiddenGemsFragment();
        Bundle args = new Bundle();
        if (object != null)
        {
            args.putSerializable(ARG_PARAM1, (Serializable) object);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Object) getArguments().getSerializable(ARG_PARAM1);
            if (mParam1 instanceof Pindrop)
                pin = (Pindrop) mParam1;
            else if (mParam1 instanceof Restaurants)
                restaurants = (Restaurants) mParam1;
            else if(mParam1 instanceof PaidActivities)
                activities = (PaidActivities) mParam1;
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hidden_gems, container, false);

/*
        mToolBar = (Toolbar) rootView.findViewById(R.id.hiddengemstoolbar);
        mToolBar.inflateMenu(R.menu.gemsbar);*/
    //    setBottomBar();
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        update = (Button) rootView.findViewById(R.id.updateButton);
        cancel = (Button) rootView.findViewById(R.id.cancelButton);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mParam1 instanceof Pindrop) {
                    Double lat = Double.valueOf(((Pindrop) mParam1).getGps().getLattitude());
                    Double longitude = Double.valueOf(((Pindrop) mParam1).getGps().getLongitude());
                    LatLng temp = new LatLng(lat,longitude);
                    position = temp;
                    gps.setLattitude(String.valueOf(position.latitude));
                    gps.setLongitude(String.valueOf(position.longitude));
                    ((Pindrop) mParam1).setGps(gps);
                    ((Pindrop) mParam1).setaccuracy(true);
                    getFragmentManager().beginTransaction().
                            replace(R.id.hiddengemscontainer,AddNewGemFragment.newInstance((Pindrop)mParam1)).addToBackStack(null).commit();
                }else if(mParam1 instanceof Restaurants){
                    Double lat = Double.valueOf(((Restaurants) mParam1).getGps().getLattitude());
                    Double longitude = Double.valueOf(((Restaurants) mParam1).getGps().getLongitude());
                    LatLng temp = new LatLng(lat,longitude);
                    position = temp;
                    gps.setLattitude(String.valueOf(position.latitude));
                    gps.setLattitude(String.valueOf(position.longitude));
                    ((Restaurants) mParam1).setGps(gps);
                    ((Restaurants) mParam1).setaccuracy(true);

                    getFragmentManager().beginTransaction().
                            replace(R.id.restaurantcontainer,MapActivitiesFragment.newInstance((Restaurants)mParam1)).addToBackStack(null).commit();
                }else{
                    Double lat = Double.valueOf(((PaidActivities) mParam1).getGps().getLattitude());
                    Double longitude = Double.valueOf(((PaidActivities) mParam1).getGps().getLongitude());
                    LatLng temp = new LatLng(lat,longitude);
                    position = temp;
                    gps.setLattitude(String.valueOf(position.latitude));
                    gps.setLattitude(String.valueOf(position.longitude));
                    ((PaidActivities) mParam1).setGps(gps);
                    ((PaidActivities) mParam1).setaccuracy(true);

                    getFragmentManager().beginTransaction().
                            replace(R.id.paidcontainer,MapActivitiesFragment.newInstance((PaidActivities)mParam1)).addToBackStack(null).commit();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                mMap.getUiSettings().isZoomControlsEnabled();
                mMap.getUiSettings().isZoomGesturesEnabled();
                // For dropping a marker at a point on the Map
                Criteria criteria = new Criteria();
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, getActivity());
               // loc = locationManager.getLastKnownLocation(String.valueOf(locationManager.getBestProvider(criteria, true)));

              //  getLocation();
                if (mParam1 instanceof Pindrop) {
                    pin = (Pindrop) mParam1;
                    gps = pin.getGps();
                }
                else if(restaurants!= null) {
                    gps = restaurants.getGps();
                }
                else if(activities!=null) {
                    gps = activities.getGps();
                }

                    loc = new Location("gps");
                    if (gps!=null) {
                        loc.setLatitude(Double.parseDouble(gps.getLattitude()));
                        loc.setLongitude(Double.parseDouble(gps.getLongitude()));
                        LatLng sample = new LatLng(loc.getLatitude(), loc.getLongitude());
                        //    LatLng sydney = new LatLng(-34, 151);

                        googleMap.addMarker(new MarkerOptions().position(sample).title("Original GPS location").snippet("Original Gem").draggable(true));
                        googleMap.setBuildingsEnabled(true);
                        googleMap.getUiSettings().setZoomGesturesEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.getUiSettings().setMapToolbarEnabled(true);
                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sample).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        LatLng sample = new LatLng(loc.getLatitude(),loc.getLongitude());
                        //    LatLng sydney = new LatLng(-34, 151);
                        googleMap.addMarker(new MarkerOptions().position(sample).title("Marker Title").snippet("Marker Description"));
                        MarkerOptions options=new MarkerOptions().snippet("Mymarker").
                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .anchor(0.5f, 0.5f);

                        Toast.makeText(getContext(),"accuracy is "+loc.getAccuracy(),Toast.LENGTH_LONG).show();
                        options.position(latLng);
                        position = latLng;
                        googleMap.addMarker(options);

                    }
                });
            }
        });

        return rootView;
    }



/*    private void setBottomBar() {
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.savegem:
                        if(mParam1 instanceof Pindrop) {
                            gps.setLattitude(String.valueOf(position.latitude));
                            gps.setLattitude(String.valueOf(position.longitude));
                            ((Pindrop) mParam1).setGps(gps);

                            getFragmentManager().beginTransaction().
                                    replace(R.id.hiddengemscontainer,AddNewGemFragment.newInstance((Pindrop)mParam1)).addToBackStack(null).commit();
                        }else if(mParam1 instanceof Restaurants){
                            gps.setLattitude(String.valueOf(position.latitude));
                            gps.setLattitude(String.valueOf(position.longitude));
                            ((Restaurants) mParam1).setGps(gps);

                            getFragmentManager().beginTransaction().
                                    replace(R.id.hiddengemscontainer,MapActivitiesFragment.newInstance((Restaurants)mParam1)).addToBackStack(null).commit();
                        }else{
                            gps.setLattitude(String.valueOf(position.latitude));
                            gps.setLattitude(String.valueOf(position.longitude));
                            ((PaidActivities) mParam1).setGps(gps);

                            getFragmentManager().beginTransaction().
                                    replace(R.id.hiddengemscontainer,MapActivitiesFragment.newInstance((PaidActivities)mParam1)).addToBackStack(null).commit();
                        }
                    case R.id.cancelgem:getFragmentManager().popBackStackImmediate();
                }
                return false;
            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

public void getLocation()
{


    // getting GPS status
    boolean checkGPS = locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER);

    // getting network status
    boolean checkNetwork = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    if (!checkGPS && !checkNetwork) {
       // Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
    } else {
        this.canGetLocation = true;
        // First get location from Network Provider
        if (checkNetwork) {
            //Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0, (android.location.LocationListener) this);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    loc = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }

                if (loc != null) {

                }
            } catch (SecurityException e) {

            }
        }
    }
    // if GPS Enabled get lat/long using GPS Services
    if (checkGPS) {
       // Toast.makeText(mContext, "GPS", Toast.LENGTH_SHORT).show();
        if (loc == null) {
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,  (android.location.LocationListener)this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    loc = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc != null) {
//                        latitude = loc.getLatitude();
//                        longitude = loc.getLongitude();
                    }
                }
            } catch (SecurityException e) {

            }
        }
    }
}

    @Override
    public void onLocationChanged(Location location) {
        loc = location;

        Toast.makeText(getContext(),"accuracy is "+loc.getAccuracy(),Toast.LENGTH_LONG).show();
        LatLng sample = new LatLng(loc.getLatitude(),loc.getLongitude());
        //    LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sample).title("Current Location").snippet("Original Gem").draggable(true));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sample).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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


}

