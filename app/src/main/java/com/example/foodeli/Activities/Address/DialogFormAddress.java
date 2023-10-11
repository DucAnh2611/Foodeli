package com.example.foodeli.Activities.Address;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.MySqlSetUp.Schemas.Address.Address;
import com.example.foodeli.R;


public class DialogFormAddress extends DialogFragment {

    private int uid;
    private Address address;
    private Context context;
    private EditText addressInput;
    private AppCompatButton confirmBtn;
    private String tempDesc;
    private HandleFormAddress handleFormAddress;

    public DialogFormAddress(Context context, int uid) {
        this.context = context;
        this.uid = uid;
    }

    public DialogFormAddress(Context context, int uid, Address address) {
        this.context = context;
        this.uid = uid;
        this.address = address;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_address, null);
        dialog.setContentView(view);

        addressInput = view.findViewById(R.id.address_form_text);
        confirmBtn = view.findViewById(R.id.address_form_confirm);

        if(address!=null) {
            addressInput.setText(address.getAddress());
        }

        addressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tempDesc = s.toString();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address!= null && tempDesc != address.getAddress()) {
                    handleFormAddress.onUpdateAddress(address, tempDesc);
                }
                else {
                    handleFormAddress.onCreateAddress(tempDesc);
                }
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

    public interface HandleFormAddress{
        void onUpdateAddress(Address address, String newAdd);
        void onCreateAddress(String address);
    }

    public void setHandleFormAddress(HandleFormAddress handleFormAddress) {
        this.handleFormAddress = handleFormAddress;
    }
}
