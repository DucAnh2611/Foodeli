package com.example.foodeli.Activities.Find;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FilterScreenDialog extends DialogFragment {

    private OnApplyFilter onApplyFilter;
    private Filter filter;
    private Context context;
    private ArrayList<Category> categories;
    private CategoriesFindAdapter adapter;
    private GridView gridView;
    private EditText minPriceET, maxPriceET;
    private Button apply;
    private int selectId;
    private float min = 0, max = 0;
    private DecimalFormat df;

    public FilterScreenDialog(ArrayList<Category> categories, Filter filter, Context context) {
        this.categories = categories;
        this.filter = filter;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_filter_fullscreen, null);
        dialog.setContentView(view);

        gridView = view.findViewById(R.id.search_filter_gridview_category);
        minPriceET = view.findViewById(R.id.search_filter_gridview_min);
        maxPriceET = view.findViewById(R.id.search_filter_gridview_max);
        apply = view.findViewById(R.id.search_filter_apply_btn);

        adapter = new CategoriesFindAdapter(categories, context);
        adapter.setOnUpdateFindData(new OnUpdateFindData() {
            @Override
            public void onClickHistory() {

            }

            @Override
            public void onRemoveHistory() {

            }

            @Override
            public void onChangeMin() {

            }

            @Override
            public void onChangeMax() {

            }

            @Override
            public void onClickCategory() {
                selectId = adapter.getSelectId();
            }
        });
        gridView.setAdapter(adapter);

        df = new DecimalFormat("#.##");

        minPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    String num = df.format(Float.parseFloat(s.toString())).replace(",", ".");
                    min = Float.parseFloat(num);
                    if(min > max) {
                        min = max;
                        minPriceET.setText(df.format(min).replace(",", "."));
                    }
                }
                else min = 0;
            }
        });

        maxPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    String num = df.format(Float.parseFloat(s.toString())).replace(",", ".");
                    max = Float.parseFloat(num);
                    if(max < min) {
                        max = min;
                        maxPriceET.setText(df.format(max).replace(",", "."));
                    }
                }
                else max = 0;
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApplyFilter.onApplyFilter(selectId, min, max);
                dismiss();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    public interface OnApplyFilter {
        void onApplyFilter(int cid, float min, float max);
    }

    public void setOnApplyFilter(OnApplyFilter onApplyFilter) {this.onApplyFilter = onApplyFilter;}
}
