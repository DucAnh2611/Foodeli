package com.example.foodeli.Fragments.ShopVoucher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.Activities.Auth.Signup.DatePickerCustom;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Body.VoucherShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportDate;

import java.lang.reflect.Field;

public class DialogCreateVoucher extends DialogFragment {

    private Context context;
    private Pool pool;
    private OnSelectMethodVoucher onSelectMethodVoucher;
    private String code, title, desc, target;
    private float discount, min, max;
    private int limit, expired;
    private EditText vCode, vTitle, vDesc, vMin, vMax, vLimit, vDiscount;
    private String[] typeDiscount = {"Percent", "Dollar"};
    private String[] typeVoucher = {"bill", "ship"};
    private int[] typeIcon = {R.drawable.percent, R.drawable.dollar};
    private Spinner day, month, year, type, vTarget;
    private AppCompatButton cancel, confirm;
    private ImageView labelType;
    private SupportDate supportDate = new SupportDate();

    public DialogCreateVoucher(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_form_voucher, null);
        dialog.setContentView(view);

        vCode = view.findViewById(R.id.shop_form_voucher_code);
        vTitle = view.findViewById(R.id.shop_form_voucher_title);
        vDesc = view.findViewById(R.id.shop_form_voucher_desc);
        vDiscount = view.findViewById(R.id.shop_form_voucher_discount);
        type = view.findViewById(R.id.shop_form_voucher_type);
        vTarget = view.findViewById(R.id.shop_form_voucher_type_discount);
        labelType = view.findViewById(R.id.shop_form_voucher_label);
        vMin = view.findViewById(R.id.shop_form_voucher_min);
        vLimit = view.findViewById(R.id.shop_form_voucher_limit);
        vMax = view.findViewById(R.id.shop_form_voucher_max);
        day = view.findViewById(R.id.shop_form_voucher_day);
        month = view.findViewById(R.id.shop_form_voucher_month);
        year = view.findViewById(R.id.shop_form_voucher_year);
        cancel = view.findViewById(R.id.shop_form_voucher_cancel);
        confirm = view.findViewById(R.id.shop_form_voucher_confirm);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, typeDiscount);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labelType.setImageResource(typeIcon[position]);
                if(!vDiscount.getText().toString().equals("")) {
                    int dis = Integer.parseInt(vDiscount.getText().toString());
                    if(typeDiscount[position].equals(typeDiscount[0])){
                        if(dis > 100) {
                            dis = 0;
                            vDiscount.setText("");
                        }
                        else {
                            discount = (float) (dis / 100);
                        }
                    }
                    else {
                        discount = dis;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setSelection(0);

        ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, typeVoucher);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vTarget.setAdapter(adapterTarget);
        vTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target = typeVoucher[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vTarget.setSelection(0);

        DatePickerCustom datePickerCustom = new DatePickerCustom(day, month, year, -50,getContext());
        datePickerCustom.setup();
        vCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String filteredInput = input.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

                if (!input.equals(filteredInput)) {
                    code = filteredInput;
                    vCode.setText(filteredInput);
                    vCode.setSelection(filteredInput.length());
                }
            }
        });
        vTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
            }
        });
        vDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                desc = s.toString();
            }
        });
        vDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(type.getSelectedItemId() == 0 && Integer.parseInt(s.toString()) > 100) {
                    vDiscount.setText("100");
                }
                discount = Float.parseFloat(s.toString());
            }
        });
        vMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) min = Float.parseFloat(s.toString());
            }
        });
        vLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) limit = Integer.parseInt(s.toString());
            }
        });
        vMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) max = Float.parseFloat(s.toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMethodVoucher.onCancel();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeBetween = supportDate.CalculateSecondBetween( String.format("%d-%02d-%02d",
                    datePickerCustom.getSelectYear(), datePickerCustom.getSelectMonth(), datePickerCustom.getSelectDay()) );
                expired = (int) timeBetween;
                if(expired <= 0) {
                    Toast.makeText(getContext(), "Can not select previous days", Toast.LENGTH_SHORT).show();
                }
                else {
                    onSelectMethodVoucher.onConfirm(
                            code, title, desc, type.getSelectedItemPosition() == 0 ? (discount / 100) : discount,
                            min, max, limit, target, expired
                    );
                }

            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }
    public interface OnSelectMethodVoucher {
        void onCancel();
        void onConfirm(String code, String title, String desc, float discount, float min, float max, int limit, String target, int expired);
    }


    public void setOnSelectMethodVoucher(OnSelectMethodVoucher onSelectMethodVoucher) {
        this.onSelectMethodVoucher = onSelectMethodVoucher;
    }
}
