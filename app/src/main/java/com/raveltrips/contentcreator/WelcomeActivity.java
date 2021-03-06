package com.raveltrips.contentcreator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
      /*  if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }*/

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
/*        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);*/


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome1,
                R.layout.welcome2,
                R.layout.welcome3,
                R.layout.welcome4,
                R.layout.welcome5};

        // adding bottom dots
        //addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

/*        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });*/
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent intent = new Intent(WelcomeActivity.this,TripCreatorActivity.class );
        intent.putExtra("newtrip",1);
        startActivity(intent);
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            //addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
       /*     if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Start");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("Next");
                btnSkip.setVisibility(View.VISIBLE);
            }*/
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView header,body,subheader1,subbody1,subheader2,subbody2,subheader3,subbody3;
            Button movenext;
            Typeface museo = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                    "fonts/Museo700-Regular.ttf");
            Typeface dinlight = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                    "fonts/DIN2014Light.ttf");
            Typeface dindemi = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                    "fonts/DIN 2014 Demi.ttf");
            Typeface montserrat = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                    "fonts/Montserrat-SemiBold.ttf");
            View view = null;
            Log.d("WelcomeActivity",String.valueOf(position));
            switch (position){
                case 0: view = layoutInflater.inflate(layouts[0], container, false);
                        container.addView(view);
                        header = (TextView) view.findViewById(R.id.headertextslider);
                         body = (TextView)view.findViewById(R.id.bodytextslider);
                    movenext = (Button) view.findViewById(R.id.create_trip);
                    movenext.setTypeface(dinlight);
                    MoveScreen(movenext);
                        header.setTypeface(museo);
                        body.setTypeface(dinlight);

                    break;
                case 1:  view = layoutInflater.inflate(layouts[1], container, false);
                    container.addView(view);
                     header = (TextView)view.findViewById(R.id.headertextslider);
                    subheader1 = (TextView)view.findViewById(R.id.subheader1);
                    subbody1 = (TextView)view.findViewById(R.id.subbody1);
                    subheader2 = (TextView)view.findViewById(R.id.subheader2);
                    subbody2 = (TextView)view.findViewById(R.id.subbody2);
                    subheader3 = (TextView)view.findViewById(R.id.subheader3);
                    subbody3 = (TextView)view.findViewById(R.id.subbody3);

                    header.setTypeface(museo);
                    subheader1.setTypeface(dindemi);
                    subbody1.setTypeface(dinlight);
                    subheader2.setTypeface(dindemi);
                    subbody2.setTypeface(dinlight);
                    subheader3.setTypeface(dindemi);
                    subbody3.setTypeface(dinlight);
                    movenext = (Button) view.findViewById(R.id.create_trip);
                    movenext.setTypeface(dinlight);
                    MoveScreen(movenext);
                    break;
                case 2:   view = layoutInflater.inflate(layouts[2], container, false);
                    container.addView(view);
                    header = (TextView)view.findViewById(R.id.headertextslider);
                    subheader1 = (TextView)view.findViewById(R.id.subheader1);
                    subbody1 = (TextView)view.findViewById(R.id.subbody1);
                    subheader2 = (TextView)view.findViewById(R.id.subheader2);
                    subbody2 = (TextView)view.findViewById(R.id.subbody2);
                    subheader3 = (TextView)view.findViewById(R.id.subheader3);
                    subbody3 = (TextView)view.findViewById(R.id.subbody3);
                    header.setTypeface(museo);
                    subheader1.setTypeface(dindemi);
                    subbody1.setTypeface(dinlight);
                    subheader2.setTypeface(dindemi);
                    subbody2.setTypeface(dinlight);
                    subheader3.setTypeface(dindemi);
                    subbody3.setTypeface(dinlight);
                    movenext = (Button) view.findViewById(R.id.create_trip);
                    movenext.setTypeface(dinlight);
                    MoveScreen(movenext);
                    break;
                case 3:  view = layoutInflater.inflate(layouts[3], container, false);
                    container.addView(view);
                    header = (TextView)view.findViewById(R.id.headertextslider);
                    body = (TextView)view.findViewById(R.id.bodytextslider);
                    header.setTypeface(museo);
                    body.setTypeface(dinlight);
                    movenext = (Button) view.findViewById(R.id.create_trip);
                    movenext.setTypeface(dinlight);
                    MoveScreen(movenext);
                    break;
                case 4:   view = layoutInflater.inflate(layouts[4], container, false);
                    container.addView(view);
                    header = (TextView)view.findViewById(R.id.headertextslider);
                    subbody1 = (TextView)view.findViewById(R.id.subbody1);
                    subheader2 = (TextView)view.findViewById(R.id.subheader2);
                    subbody2 = (TextView)view.findViewById(R.id.subbody2);
                    subheader3 = (TextView)view.findViewById(R.id.subheader3);
                    subbody3 = (TextView)view.findViewById(R.id.subbody3);

                    header.setTypeface(museo);
                    subbody1.setTypeface(dinlight);
                    subheader2.setTypeface(dindemi);
                    subbody2.setTypeface(dinlight);
                    subheader3.setTypeface(dindemi);
                    subbody3.setTypeface(dinlight);
                    movenext = (Button) view.findViewById(R.id.create_trip);
                    movenext.setTypeface(dinlight);
                    MoveScreen(movenext);
                    AppContext.CALL = true;
                    break;
            }
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void MoveScreen(Button movenext) {

        movenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }
}
