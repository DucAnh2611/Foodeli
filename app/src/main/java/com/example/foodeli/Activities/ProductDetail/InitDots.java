package com.example.foodeli.Activities.ProductDetail;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.foodeli.R;

import java.util.ArrayList;
import java.util.List;

public class InitDots {
    LinearLayout dotsLayout = null;
    private List<View> dotViews = new ArrayList<>();
    private Context context;

    public InitDots(Context context, LinearLayout layout) {
        this.dotsLayout = layout;
        this.context = context;
    }

    public void createDotIndicators(int numDots) {
        dotViews.clear();
        dotsLayout.removeAllViews();

        for (int i = 0; i < numDots; i++) {
            View dotView = new View(context);
            dotView.setBackgroundResource(R.drawable.dot_bg);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            int margin = context.getResources().getDimensionPixelSize(R.dimen.dot_indicator_margin);
            params.setMargins(margin, 0, margin, 0);
            dotView.setLayoutParams(params);
            if(i==0) animateDotIndicator(dotView, true);

            dotsLayout.addView(dotView);
            dotViews.add(dotView);
        }
    }

    public void updateDotIndicator(int currentIndex) {
        for (int i = 0; i < dotViews.size(); i++) {
            View dotView = dotViews.get(i);
            if (i == currentIndex) {
                animateDotIndicator(dotView, true);
            } else {
                animateDotIndicator(dotView, false);
            }
        }
    }

    public void animateDotIndicator(View dotView, boolean isSelected) {
        int targetWidth = isSelected ? context.getResources().getDimensionPixelSize(R.dimen.dot_indicator_selected_width)
                : context.getResources().getDimensionPixelSize(R.dimen.dot_indicator_unselected_width);
        ValueAnimator animator = ValueAnimator.ofInt(dotView.getLayoutParams().width, targetWidth);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = dotView.getLayoutParams();
                layoutParams.width = width;
                dotView.setLayoutParams(layoutParams);
                if(isSelected) {
                    dotView.setBackgroundResource(R.drawable.dot_bg_hightlight);
                }
                else {
                    dotView.setBackgroundResource(R.drawable.dot_bg);
                }

            }
        });
        animator.setDuration(200);
        animator.start();
    }
}
