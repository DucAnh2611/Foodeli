package com.example.foodeli.Fragments.ShopProduct;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class DynamicHeightGridView extends GridView {
    public DynamicHeightGridView(Context context) {
        super(context);
    }

    public DynamicHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Calculate the height of the grid items dynamically based on their content height
        int height = 0;
        int columns = getNumColumns();
        int rows = getCount() / columns;
        int extraRow = getCount() % columns > 0 ? 1 : 0;
        for (int i = 0; i < rows + extraRow; i++) {
            View listItem = getAdapter().getView(i, null, this);
            listItem.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight() + getVerticalSpacing();
        }

        // Set the calculated height as the measured height of the grid
        setMeasuredDimension(getMeasuredWidth(), height + getPaddingBottom());
    }
}
