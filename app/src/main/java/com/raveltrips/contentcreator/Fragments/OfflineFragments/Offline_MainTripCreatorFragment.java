package com.raveltrips.contentcreator.Fragments.OfflineFragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raveltrips.contentcreator.Adapters.OfflineTripCreatorAdapter;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.models.CompleteTrip;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offline_MainTripCreatorFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    int offlineTripPosition = -1;
    CompleteTrip selectedTrip = null;
    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    OfflineTripCreatorAdapter adapter;
    LinearLayoutManager layoutManager;
    String[] options = {"Drop a gem location","Drop Activity Location", "Drop Restaurant Location"};
    Toolbar toolbar;


    public Offline_MainTripCreatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_offline__main_trip_creator, container, false);

        if (getArguments() != null) {
             offlineTripPosition = getArguments().getInt(ARG_PARAM1);
            selectedTrip = (CompleteTrip) AppContext.offlineTripsList.get(AppContext.offlineSelectedTripPosition);
        }
        TextView tripName = (TextView) rootView.findViewById(R.id.offlinetripname);
        tripName.setText(selectedTrip.getName());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_rest);
        toolbar.setVisibility(View.GONE);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");

        rootView.findViewById(R.id.topicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("flag","flag");
                startActivity(intent);
            }
        });
        adapter = new OfflineTripCreatorAdapter(getContext(), options);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        OfflineTripCreatorAdapter.OnItemClickListener listener = new OfflineTripCreatorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mListener.onFragmentInteraction(v, position);
            }
        };
        adapter.setListener(listener);

        return rootView;
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
        toolbar.setVisibility(View.GONE);
    }

    public static Fragment newInstance(int position) {
        Offline_MainTripCreatorFragment fragment = new Offline_MainTripCreatorFragment();
        Bundle args = new Bundle();
        args.putInt("selectedPosition",position);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v , int position );
    }

}
