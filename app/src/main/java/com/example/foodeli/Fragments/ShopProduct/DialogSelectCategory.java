package com.example.foodeli.Fragments.ShopProduct;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.Activities.Find.CategoriesFindAdapter;
import com.example.foodeli.Activities.Find.Filter;
import com.example.foodeli.Activities.Find.OnUpdateFindData;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DialogSelectCategory extends DialogFragment {

    private OnApplyCategory onApplyCategory;
    private Context context;
    private ArrayList<Category> categories;
    private CategoriesFindAdapter adapter;
    private GridView gridView;
    private AppCompatButton apply;
    private int selectId;

    public DialogSelectCategory(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_category_list, null);
        dialog.setContentView(view);

        gridView = view.findViewById(R.id.dialog_category_list_gv);
        apply = view.findViewById(R.id.dialog_category_list_apply_btn);

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

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApplyCategory.onApply(selectId);
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

    public interface OnApplyCategory {
        void onApply(int cid);
    }

    public void setOnApplyCategory(OnApplyCategory onApplyCategory) {
        this.onApplyCategory = onApplyCategory;
    }
}
