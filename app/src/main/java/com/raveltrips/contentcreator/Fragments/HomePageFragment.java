package com.raveltrips.contentcreator.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.PrefManager;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.TripCreatorActivity;
import com.raveltrips.contentcreator.WelcomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {


    ImageView pinDropDropDown;
    Button createTrip;
    private PrefManager prefManager;
    TextView header,body;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
/*
        aboutus = (TextView) rootView.findViewById(R.id.abtustext);
        pipe = (TextView) rootView.findViewById(R.id.pipetext);
        faq = (TextView) rootView.findViewById(R.id.faqtext);*/



//        Log.d("TOKEN", AppContext.FCM_TOKEN );
        pinDropDropDown = (ImageView)rootView.findViewById(R.id.pinDropDown);
        pinDropDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).
                        replace(R.id.main_container,new OptionsNavigatorFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });

/*        Button dummy = (Button)rootView.findViewById(R.id.dummy_check);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(myIntent);
            }
        });*/
        createTrip = (Button)rootView.findViewById(R.id.create_trip);
        header = (TextView)rootView.findViewById(R.id.Header);
        body = (TextView)rootView.findViewById(R.id.body);
//        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
//                "fonts/Museo700-Regular.ttf");
//        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
//                "fonts/DIN-2014-Light.otf");
//        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
//                "fonts/DIN-2014-Demi.otf");
//        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
//                "fonts/Montserrat-SemiBold.otf");
//        header.setTypeface(museo);
//        body.setTypeface(dinlight);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        header.setTypeface(museo);
        body.setTypeface(dinlight);
        createTrip.setTypeface(dinlight);

/*        aboutus.setTypeface(dinlight);
        pipe.setTypeface(dinlight);
        faq.setTypeface(dinlight);*/
        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppContext.OfflineMode) {
                    prefManager = new PrefManager(getContext());
                    if (prefManager.isFirstTimeLaunch()) {
                        prefManager.setFirstTimeLaunch(true);
                        Intent myIntent = new Intent(getActivity(), WelcomeActivity.class);
                        startActivity(myIntent);
                        getActivity().finish();
                    } else {
                        AppContext.CALL = true;
                        Intent intent = new Intent(getActivity(), TripCreatorActivity.class);
                        intent.putExtra("newtrip", 1);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"You are Offline!! Please check your Internet connection to create a trip",Toast.LENGTH_LONG).show();
                }
            }

        });
        return rootView;
    }

}
