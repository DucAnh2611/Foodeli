package com.example.foodeli.Activities.ProductDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodeli.Activities.Auth.Signup.DatePickerCustom;
import com.example.foodeli.Activities.ShopManage.ShopManageViewModel;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Body.AddToCartBody;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.AddToCartRes;
import com.example.foodeli.MySqlSetUp.Schemas.Cart.Response.DeleteItemRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDeleteCart extends DialogFragment {
    private Context context;
    private User user;

    private TextView title, message ;
    private AppCompatButton cancelBtn, confirmBtn;
    private int pid, quantity;

    public DialogDeleteCart(Context context, int pid, int quantity) {
        this.context = context;
        this.pid = pid;
        this.quantity = quantity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete_cart, null);
        dialog.setContentView(view);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        title = view.findViewById(R.id.dialog_title);
        message = view.findViewById(R.id.dialog_message);
        confirmBtn = view.findViewById(R.id.dialog_confirm_btn);
        cancelBtn = view.findViewById(R.id.dialog_cancel_btn);

        title.setText("Want to delete?");
        message.setText("This product is not same shop with other in cart\nDo you want to delete all product in cart?");
        cancelBtn.setText("Keep current");
        confirmBtn.setText("Delete and Add");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCart();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    private void deleteCart() {
        Pool pool = new Pool();

        Call<DeleteItemRes> deleteCart = pool.getApiCallUserCart().deleteCart(user.getId());

        deleteCart.enqueue(new Callback<DeleteItemRes>() {
            @Override
            public void onResponse(Call<DeleteItemRes> call, Response<DeleteItemRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }else {

                    if(response.body().isDelete()) {
                        AddItemToCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteItemRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void AddItemToCart() {
        Pool pool = new Pool();

        AddToCartBody body = new AddToCartBody(user.getId(), pid, quantity);

        Call<AddToCartRes> deleteCart = pool.getApiCallUserCart().addItemToCart(body);

        deleteCart.enqueue(new Callback<AddToCartRes>() {
            @Override
            public void onResponse(Call<AddToCartRes> call, Response<AddToCartRes> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    ResponseApi res;
                    try {
                        res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(res.getMessage());
                    } catch (IOException e) {
                        System.out.println("parse err false");
                    }
                }else {
                    Toast.makeText(context, response.body().isAdded() ? "Successfully" : "Failed on adding this item to cart", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<AddToCartRes> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
