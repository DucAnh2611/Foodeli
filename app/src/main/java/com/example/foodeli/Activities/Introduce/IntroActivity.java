package com.example.foodeli.Activities.Introduce;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.foodeli.Activities.Auth.Login.Login;
import com.example.foodeli.R;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private Button btnNext, btnSkip;
    private IntroAdapter introAdapter;
    private TextView[] mDots;
    private PrefManager prefManager;
    private int currentItem;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        prefManager = new PrefManager(this);

        if(!prefManager.isFirstLaunch()) {
            launchMainScreen();
        }

        setContentView(R.layout.activity_intro);

        transparentStatusBar();

        findViews();

        setupViewPager();

        setClickListener();

        addDotsIndicator(0);
    }

    private void launchMainScreen(){
        prefManager.setIsFirstLaunch(false);
        startActivity( new Intent(IntroActivity.this, Login.class));
        finish();
    }

    private void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void setupViewPager() {
        introAdapter = new IntroAdapter(this);
        viewPager.setAdapter(introAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private void findViews() {
        viewPager = findViewById( R.id.view_pager );
        dotsLayout = findViewById( R.id.dots_pages );
        btnNext = findViewById( R.id.btn_next );
        btnSkip = findViewById( R.id.btn_skip );
    }

    private void setClickListener() {
        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    public void addDotsIndicator(int position){
        // Adding TetView dynamically
        mDots=new TextView[introAdapter.getCount()];

        /* Remove aprvious views when called next time
         if not called then views will keep on adding*/
        dotsLayout.removeAllViews();

        // Set bullets in each dot text view
        for (int i=0; i< mDots.length; i++){
            mDots[i]= new TextView(this);
            mDots[i].setText(Html.fromHtml("•"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.grey_400));

            dotsLayout.addView(mDots[i]);
        }

        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.green_100));
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            addDotsIndicator(position);

            if (position==introAdapter.getCount()-1){
                // last page, make it "finish" and make the skip button invisible
                btnNext.setText("Enjoy!");
                btnSkip.setVisibility(View.INVISIBLE);
            }else {
                // not last page, set "next" text and make skip button visible
                btnNext.setText("Next");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_next) {
            if (currentItem<introAdapter.getCount()-1){
                ++currentItem; // increase the value by 1
                viewPager.setCurrentItem(currentItem); // set the layout at next position
            }
            else launchMainScreen();

        }
        else if(v.getId() == R.id.btn_skip) {
            launchMainScreen();
        }
    }
}
