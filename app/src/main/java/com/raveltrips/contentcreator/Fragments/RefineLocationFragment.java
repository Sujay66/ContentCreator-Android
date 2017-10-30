package com.raveltrips.contentcreator.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Restaurants;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RefineLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RefineLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefineLocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Object mParam1;
    private String mParam2;

    Button refine;
    private OnFragmentInteractionListener mListener;

    public RefineLocationFragment() {
        // Required empty public constructor
    }

    public static RefineLocationFragment newInstance(Object pindrop) {
        RefineLocationFragment fragment = new RefineLocationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) pindrop);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RefineLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RefineLocationFragment newInstance(String param1, String param2) {
        RefineLocationFragment fragment = new RefineLocationFragment();
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
            mParam1 = (Object) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_refine_location, container, false);
        refine = (Button) rootView.findViewById(R.id.refinelocation);

        refine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParam1 != null && mParam1 instanceof Pindrop){
                    getFragmentManager().beginTransaction().
                            replace(R.id.hiddengemscontainer, HiddenGemsFragment.newInstace((Pindrop) mParam1))
                            .addToBackStack(null).commit();
                }
                else if(mParam1!=null && mParam1 instanceof Restaurants)
                {
                    getFragmentManager().beginTransaction().
                            replace(R.id.restaurantcontainer, HiddenGemsFragment.newInstace((Restaurants) mParam1))
                            .addToBackStack(null).commit();
                }
                else if (mParam1!= null && mParam1 instanceof PaidActivities)
                {
                    getFragmentManager().beginTransaction().
                            replace(R.id.paidcontainer, HiddenGemsFragment.newInstace((PaidActivities) mParam1))
                            .addToBackStack(null).commit();
                }

            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
