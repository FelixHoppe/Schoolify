package de.felix_h.schulify;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;
    private Button mLastBtn;
    private ImageButton mLastImgBtn;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        RelativeLayout relativeLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Toast.makeText(getBaseContext(), " wische nach links ", Toast.LENGTH_LONG).show();


        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mNextBtn = (Button) findViewById(R.id.nextBtn);
        mBackBtn = (Button) findViewById(R.id.prevBtn);
        mLastBtn = (Button) findViewById(R.id.lastBtn);
        mLastImgBtn = (ImageButton) findViewById(R.id.lastImgBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

     //OnClickListeners

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSlideViewPager.setCurrentItem(mCurrentPage + 1);

            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

        mLastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),StartActivity.class);
                startActivity(i);

            }
        });

        mLastImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),StartActivity.class);
                startActivity(i);

            }
        });
    }






    public void addDotsIndicator(int position){

        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length > 0){

            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

            mCurrentPage = i;




            Context context;
            CharSequence text;
            int duration;

            Toast toast;




            if(i == 0) {

                mLastBtn.setVisibility(View.INVISIBLE);
                mLastBtn.setClickable(false);
                mLastBtn.setEnabled(false);
                mLastImgBtn.setVisibility(View.INVISIBLE);
                mLastImgBtn.setClickable(false);
                mLastImgBtn.setEnabled(false);

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setClickable(true);
                mNextBtn.setTag("Weiter");
                mBackBtn.setTag("");

            } else if(i == mDots.length - 1) {

                mLastBtn.setVisibility(View.VISIBLE);
                mLastBtn.setEnabled(true);
                mLastBtn.setClickable(false);
                mLastImgBtn.setVisibility(View.VISIBLE);
                mLastImgBtn.setEnabled(true);
                mLastImgBtn.setClickable(true);
                mLastBtn.setTag("Fertig");

                mNextBtn.setEnabled(false);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setClickable(false);
                mNextBtn.setTag("");
                mBackBtn.setTag("Zurück");

            } else {

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setClickable(true);
                mNextBtn.setTag("Weiter");
                mBackBtn.setTag("Zurück");

                mLastBtn.setVisibility(View.INVISIBLE);
                mLastBtn.setClickable(false);
                mLastBtn.setEnabled(false);
                mLastImgBtn.setVisibility(View.INVISIBLE);
                mLastImgBtn.setClickable(false);
                mLastImgBtn.setEnabled(false);


            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
