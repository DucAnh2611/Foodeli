package com.example.foodeli.Activities.Voucher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Body.SaveVoucherBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.FindVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.GetAllVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Response.SaveVoucherRes;
import com.example.foodeli.MySqlSetUp.Schemas.UserVoucher.Voucher;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogFindVoucher extends DialogFragment {

    private Context context;
    private EditText codeVou;
    private GridView voucherFound;
    private LinearLayout voucherLayout;
    private ArrayList<Voucher> listVou;
    private ImageButton findBtn;
    private String code;
    private Pool pool;
    private FindVoucherAdapter adapter;
    private int uid;
    private OnSuccessSaveVoucher onSuccessSaveVoucher;
    private LinearLayout voucherEmpty;
    private ScrollView voucherLoading;
    public DialogFindVoucher(Context context, int uid) {
        this.context = context;
        this.uid = uid;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_find_voucher, null);
        dialog.setContentView(view);

        codeVou = view.findViewById(R.id.find_voucher_code);
        voucherFound = view.findViewById(R.id.find_voucher_gridview);
        voucherLayout = view.findViewById(R.id.find_voucher_gridview_holder);
        findBtn = view.findViewById(R.id.find_voucher_icon);
        voucherEmpty = view.findViewById(R.id.find_voucher_gridview_empty);
        voucherLoading = view.findViewById(R.id.find_voucher_gridview_loading);

        voucherFound.setVisibility(View.GONE);
        voucherEmpty.setVisibility(View.GONE);
        voucherLoading.setVisibility(View.VISIBLE);

        codeVou.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code = s.toString().toUpperCase();
            }
        });
        codeVou.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    findVoucher();
                    return true;
                }
                return false;
            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findVoucher();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    private void findVoucher() {

        voucherFound.setVisibility(View.GONE);
        voucherEmpty.setVisibility(View.GONE);
        voucherLoading.setVisibility(View.VISIBLE);

        pool = new Pool();

        Call<FindVoucherRes> findVoucher = pool.getApiCallUserVoucher().findVoucher(code);
        findVoucher.enqueue(new Callback<FindVoucherRes>() {
            @Override
            public void onResponse(Call<FindVoucherRes> call, Response<FindVoucherRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    listVou = response.body().getList();
                    if(listVou.isEmpty()) {
                        voucherFound.setVisibility(View.GONE);
                        voucherEmpty.setVisibility(View.VISIBLE);
                    }
                    else {
                        adapter = new FindVoucherAdapter(listVou, context);
                        adapter.setOnSaveVoucher(new FindVoucherAdapter.OnSaveVoucher() {
                            @Override
                            public void onSaveVoucher(int vid) {
                                saveVoucherForUser(uid, vid, "id");
                            }
                        });
                        voucherFound.setAdapter(adapter);

                        voucherFound.setVisibility(View.VISIBLE);
                        voucherEmpty.setVisibility(View.GONE);
                    }
                    voucherLoading.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<FindVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void saveVoucherForUser(int uid, int vid, String type) {
        pool = new Pool();

        SaveVoucherBody body = new SaveVoucherBody(uid, type, String.valueOf(vid));
        Call<SaveVoucherRes> saveVoucher = pool.getApiCallUserVoucher().saveVoucher(body);
        saveVoucher.enqueue(new Callback<SaveVoucherRes>() {
            @Override
            public void onResponse(Call<SaveVoucherRes> call, Response<SaveVoucherRes> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    onSuccessSaveVoucher.onSuccessSaveVoucher(response.body().getVoucher().get(0));
                }
            }

            @Override
            public void onFailure(Call<SaveVoucherRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
    public interface OnSuccessSaveVoucher {
        void onSuccessSaveVoucher(GetAllVoucherRes.VoucherWithRemain voucher);
    }

    public void setOnSuccessSaveVoucher(OnSuccessSaveVoucher onSuccessSaveVoucher) {
        this.onSuccessSaveVoucher = onSuccessSaveVoucher;
    }
}
