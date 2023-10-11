package com.example.foodeli.Activities.MethodCheckout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.MySqlSetUp.Schemas.General.Body.MethodSupport;
import com.example.foodeli.MySqlSetUp.Schemas.Method.Body.CreateMethod;
import com.example.foodeli.MySqlSetUp.Schemas.Method.MethodWithTypeName;
import com.example.foodeli.MySqlSetUp.Schemas.UserOrder.Response.OrderTrackRes;
import com.example.foodeli.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogPaymentForm extends DialogFragment {

    private Context context;
    private MethodWithTypeName method;
    private AppCompatButton cancelBtn, submitBtn;
    private EditText numberCard, expiredMonth, expiredYear;
    private Spinner selectType;
    private OnSelectMethodUpdate onSelectMethodUpdate;
    private HashMap<String, Integer> mapIconSupportList;
    private ArrayList<MethodSupport> methods;
    private String num = "", type = "";
    private int month = 0, year = 0, typeId = 0;

    public DialogPaymentForm(Context context) {
        this.context = context;
    }

    public DialogPaymentForm(MethodWithTypeName method, Context context) {
        this.context = context;
        this.method = method;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_form_payment, null);
        dialog.setContentView(view);

        selectType = view.findViewById(R.id.payment_form_select_type);
        numberCard = view.findViewById(R.id.payment_form_number);
        expiredMonth = view.findViewById(R.id.payment_form_expired_month);
        expiredYear = view.findViewById(R.id.payment_form_expired_year);
        cancelBtn = view.findViewById(R.id.payment_form_cancel);
        submitBtn = view.findViewById(R.id.payment_form_submit);

        ArrayAdapter<MethodSupport> adapterSpinner = new MethodSpinnerAdapter(context, methods,mapIconSupportList);
        selectType.setAdapter(adapterSpinner);

        selectType.setSelection(0);
        typeId = methods.get(0).getMid();
        type = methods.get(0).getType();

        selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeId = methods.get(position).getMid();
                type = methods.get(position).getType();
                resetUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        resetUI();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!num.equals("") && month!=0 && year!=0) {
                    onSelectMethodUpdate.onCreatePayment(typeId, type, num, String.format("20%02d-%02d-01", year, month));
                }
                else {
                    Toast.makeText(context, "All field are required!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        numberCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) num = s.toString();
            }
        });
        expiredMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) month = Integer.parseInt(s.toString());
            }
        });
        expiredYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) year = Integer.parseInt(s.toString());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void resetUI() {
        num = "";
        year = 0;
        month = 0;

        numberCard.setText(num);
        expiredYear.setText("");
        expiredMonth.setText("");
    }

    public void setOnSelectMethodUpdate(OnSelectMethodUpdate onSelectMethodUpdate) {
        this.onSelectMethodUpdate = onSelectMethodUpdate;
    }

    public void setMapIconSupportList(HashMap<String, Integer> mapIconSupportList) {
        this.mapIconSupportList = mapIconSupportList;
    }

    public void setMethods(ArrayList<MethodSupport> methods) {
        ArrayList<MethodSupport> methodWithoutCash = new ArrayList<>();
        for (int i = 1; i < methods.size(); i++) {
            methodWithoutCash.add(methods.get(i));
        }
        this.methods = methodWithoutCash;
    }
}
