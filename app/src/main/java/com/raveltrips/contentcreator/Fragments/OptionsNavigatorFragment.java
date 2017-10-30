package com.raveltrips.contentcreator.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raveltrips.contentcreator.Adapters.OptionsNavigatorAdapter;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.ProfileNewActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.async.DownloadImageAsyncTask;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsNavigatorFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    String[] options = new String[]{"Home","Create New Trip","My Trips","Trip Creation GuideLines","Logout"};
    OptionsNavigatorAdapter adapter;
    RecyclerView optionsrecycler;
    LinearLayoutManager layoutManager;
    ImageView icon,profilepic;
    TextView profilename;
    private OnFragmentInteractionListener mListener;
    public OptionsNavigatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_options_navigator, container, false);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
       // rootView.setAnimation(animation);
        icon = (ImageView)rootView.findViewById(R.id.pinDropDown);

        profilepic = (ImageView) rootView.findViewById(R.id.review_image);
        profilename = (TextView) rootView.findViewById(R.id.profilename);
        if (AppContext.OfflineMode)
        {
            adapter = new OptionsNavigatorAdapter(getContext(),new String []{"Home","My Trips"});
        }
        else {
            adapter = new OptionsNavigatorAdapter(getContext(), options);
        }
        optionsrecycler = (RecyclerView) rootView.findViewById(R.id.optionsrecycler);
        optionsrecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        optionsrecycler.setLayoutManager(layoutManager);
        optionsrecycler.setAdapter(adapter);
        Typeface museo = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");

        profilename.setTypeface(dindemi);
        try {
            if (AppContext.profile.getName() != null) {
                try {
                    profilename.setText(AppContext.profile.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Profile Download in progress", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            if (AppContext.profile.getImageUrl() != null && !AppContext.profile.getImageUrl().isEmpty()) {

                if (AppContext.USE_PICASSO)
                    Picasso.with(getContext()).load(AppContext.profile.getImageUrl()).into(profilepic);
                else {
                    Bitmap bitmap = AppContext.getBitmapFromMemCache(AppContext.profile.getImageUrl());
                    if (bitmap != null) {
                        profilepic.setImageBitmap(bitmap);
                    } else {
                        DownloadImageAsyncTask task = new DownloadImageAsyncTask(profilepic);
                        task.execute(AppContext.profile.getImageUrl());
                    }
                }
            }
        }catch (Exception ex){ex.printStackTrace();}

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),ProfileNewActivity.class);
                startActivity(i);
            }
        });
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                            .replace(R.id.main_container, new HomePageFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });

        OptionsNavigatorAdapter.OnItemClickListener listener = new OptionsNavigatorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mListener.onFragmentInteraction(v,position);
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
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(View v , int position );
    }
}
