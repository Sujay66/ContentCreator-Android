package com.raveltrips.contentcreator.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raveltrips.contentcreator.Adapters.MyTripsAdapter;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.TripCreatorActivity;
import com.raveltrips.contentcreator.models.CompleteTrip;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTripsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private OfflineFragmentInteraction offlineListener;
//    String[] options = new String[]{"Home","Create New Trip","My Trips","Trip Creation GuideLines","Logout","Profile"};
    MyTripsAdapter adapter;
    RecyclerView MyTripsRecycler;
    LinearLayoutManager layoutManager;
    TextView createTrip;
    ImageView pinDropDropDown;
    CardView cardView;

    public MyTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_my_trips, container, false);


        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        TextView header = (TextView) rootView.findViewById(R.id.Header);
        header.setTypeface(montserrat);
        cardView = (CardView) rootView.findViewById(R.id.creattrip_cardview) ;
        createTrip = (TextView) cardView.findViewById(R.id.create_trip);
        createTrip.setTypeface(museo);
        if(AppContext.completedTrips.size()!=0 || AppContext.OfflineMode) {

            if (AppContext.OfflineMode)
            {

                cardView.setVisibility(View.GONE);
                Cursor cursor = MainActivity.dataBaseHelper.getAll("MYTRIPS");
                if (cursor.moveToFirst()){
                    AppContext.offlineTripsList.clear();
                    while(!cursor.isAfterLast()){
                        CompleteTrip completeTrip = new CompleteTrip();

                        String name = cursor.getString(cursor.getColumnIndex("TRIP_NAME"));
                        String id = cursor.getString(cursor.getColumnIndex("TRIP_ID"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("CREATOR_ID"));
                        // do what ever you want here
                        completeTrip.setName(name);
                        completeTrip.setCreatorId(creatorId);
                        completeTrip.setId(id);
                        AppContext.offlineTripsList.add(completeTrip);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                MyTripsAdapter.OnItemClickListener listener = new MyTripsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        offlineListener.offlineFragmentInteraction(v,position);
                       // getFragmentManager().beginTransaction().replace(R.id.offlinecontainer,new Offline_MainTripCreatorFragment()).commit();
                    }

                    @Override
                    public void onItemLongClick(View v, int position) {

                    }
                };
                adapter = new MyTripsAdapter(getContext(), AppContext.offlineTripsList);
                adapter.setListener(listener);
            }
            else {
                MyTripsAdapter.OnItemClickListener listener = new MyTripsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        mListener.onFragmentInteraction(v, position);
                    }

                    @Override
                    public void onItemLongClick(View v, int position) {
                        mListener.onLongClick(v,position);
                    }
                };

                adapter = new MyTripsAdapter(getContext(), AppContext.completedTrips);
                adapter.setListener(listener);
            }
            MyTripsRecycler = (RecyclerView) rootView.findViewById(R.id.mytripsrecycler);
            layoutManager = new LinearLayoutManager(getContext());
            MyTripsRecycler.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            MyTripsRecycler.setLayoutManager(layoutManager);
            MyTripsRecycler.setAdapter(adapter);



        }else{
            Toast.makeText(getContext(),"You don't have any completed trips",Toast.LENGTH_SHORT).show();
        }

        pinDropDropDown = (ImageView) rootView.findViewById(R.id.pinDropDown);
        pinDropDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("flag","flag");
                startActivity(intent);
            }
        });

        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyTripsFragment","Inside Card view create trip");
                AppContext.CALL = true;
                Intent intent = new Intent(getActivity(), TripCreatorActivity.class);
                intent.putExtra("newtrip",1);
                startActivity(intent);
            }
        });
        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OfflineFragmentInteraction) {
            offlineListener = (OfflineFragmentInteraction) context;

        }
        else if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        }

            else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v , int position );
        void onLongClick(View v , int position);
    }
    public interface OfflineFragmentInteraction {
        // TODO: Update argument type and name
        void offlineFragmentInteraction(View v , int position );
    }

    @Override
    public void onResume(){

        if (adapter!=null)
            adapter.notifyDataSetChanged();
        super.onResume();
    }
}
