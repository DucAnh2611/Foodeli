package com.example.foodeli.Fragments.ShopEmployee;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodeli.Activities.Voucher.FindVoucherAdapter;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.FindUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
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

public class DialogShopUser extends DialogFragment {

    private Context context;
    private EditText keyUser;
    private GridView userFound;
    private ArrayList<User> listUser;
    private ImageButton findBtn;
    private String key;
    private Pool pool;
    private UserFoundAdapter adapter;
    private UserFoundAdapter.OnAddUserToShop onAddUserToShop;
    private ScrollView employeeLoading;
    private LinearLayout employeeEmpty;

    public DialogShopUser(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_employee_find, null);
        dialog.setContentView(view);

        keyUser = view.findViewById(R.id.shop_find_user_key);
        findBtn = view.findViewById(R.id.shop_find_user_icon);
        userFound = view.findViewById(R.id.shop_find_user_res);
        employeeEmpty = view.findViewById(R.id.find_employee_empty);
        employeeLoading = view.findViewById(R.id.find_employee_loading);

        userFound.setVisibility(View.GONE);
        employeeEmpty.setVisibility(View.GONE);
        employeeLoading.setVisibility(View.VISIBLE);

        keyUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                key = s.toString().toUpperCase();
            }
        });
        keyUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    findUser();
                    return true;
                }
                return false;
            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findUser();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    private void findUser() {
        pool = new Pool();

        Call<FindUserResponse> findU = pool.getApiCallUserProfile().findUser(key);

        findU.enqueue(new Callback<FindUserResponse>() {
            @Override
            public void onResponse(Call<FindUserResponse> call, Response<FindUserResponse> response) {
                if (response.code() != 200) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        System.out.println(res.getMessage());
                        Toast.makeText(getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }
                else {
                    ArrayList<User> list = response.body().getUsers();
                    if(list.isEmpty()) {
                        userFound.setVisibility(View.GONE);
                        employeeEmpty.setVisibility(View.VISIBLE);
                    }
                    else {
                        adapter = new UserFoundAdapter(response.body().getUsers(), getContext());
                        adapter.setOnAddUserToShop(new UserFoundAdapter.OnAddUserToShop() {
                            @Override
                            public void onAddUser(User user) {
                                onAddUserToShop.onAddUser(user);
                            }
                        });
                        userFound.setAdapter(adapter);

                        userFound.setVisibility(View.VISIBLE);
                        employeeEmpty.setVisibility(View.GONE);
                    }
                    employeeLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FindUserResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void setOnAddUserToShop(UserFoundAdapter.OnAddUserToShop onAddUserToShop) {
        this.onAddUserToShop = onAddUserToShop;
    }
}
