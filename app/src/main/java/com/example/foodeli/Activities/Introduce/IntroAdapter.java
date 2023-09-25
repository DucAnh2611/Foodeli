package com.example.foodeli.Activities.Introduce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import com.example.foodeli.R;

public class IntroAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public IntroAdapter(Context context){
        this.context=context;
    }

    //Array of screen layouts
    public int[] layouts={
            R.layout.activity_first_introduce,
            R.layout.activity_sec_introduce
    };


    // Returns total number of screens
    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view= layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}