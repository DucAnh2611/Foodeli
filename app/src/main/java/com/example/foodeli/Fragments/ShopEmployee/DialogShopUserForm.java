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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.Fragments.Profile.ImageChangeInterface;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.ShopPermission;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.FindUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogShopUserForm extends DialogFragment {

    private Context context;
    private Pool pool;
    private ShapeableImageView prevImage, afterImage;
    private TextView prevRole, prevName, afterRole, afterName;
    private Spinner selectPermission;
    private AppCompatButton cancel, confirm;
    private SupportImage supportImage = new SupportImage();

    private ArrayList<ShopPermission> shopPermissions;
    private SpinnerSelectPermissionAdapter spinnerAdapter;
    private GetAllUserInShop.UserInShop user;
    private int temp_pmid;
    private String temp_role;
    private OnSelectMethodEditUser onSelectMethodEditUser;

    public DialogShopUserForm(Context context, ArrayList<ShopPermission> shopPermissions, GetAllUserInShop.UserInShop user) {
        this.context = context;
        this.shopPermissions = shopPermissions;
        this.user = user;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_form_employee, null);
        dialog.setContentView(view);

        prevImage = view.findViewById(R.id.shop_form_user_prev_image);
        prevRole = view.findViewById(R.id.shop_form_user_prev_role);
        prevName = view.findViewById(R.id.shop_form_user_prev_name);
        afterImage = view.findViewById(R.id.shop_form_user_after_image);
        afterRole = view.findViewById(R.id.shop_form_user_after_role);
        afterName = view.findViewById(R.id.shop_form_user_after_name);
        selectPermission = view.findViewById(R.id.shop_form_user_perrmission);
        cancel = view.findViewById(R.id.shop_form_user_cancel);
        confirm = view.findViewById(R.id.shop_form_user_confirm);

        if(!user.getAvatar().equals("")) {
            prevImage.setImageBitmap(supportImage.convertBase64ToBitmap(user.getAvatar()));
            afterImage.setImageBitmap(supportImage.convertBase64ToBitmap(user.getAvatar()));
        }

        prevRole.setText(user.getRole());
        afterRole.setText(user.getRole());

        prevName.setText(user.getName());
        afterName.setText(user.getName());

        spinnerAdapter = new SpinnerSelectPermissionAdapter(context, shopPermissions);
        selectPermission.setAdapter(spinnerAdapter);

        selectPermission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp_pmid = shopPermissions.get(position).getPid();
                temp_role = shopPermissions.get(position).getType();
                afterRole.setText(shopPermissions.get(position).getType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMethodEditUser.onCancel();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMethodEditUser.onEdit(temp_pmid, temp_role);
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    private void editUser() {
        pool = new Pool();

        Call<FindUserResponse> findU = pool.getApiCallUserProfile().findUser("");

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
                }
            }

            @Override
            public void onFailure(Call<FindUserResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public interface OnSelectMethodEditUser {
        void onCancel();
        void onEdit(int pmid, String role);
    }

    public void setOnSelectMethodEditUser(OnSelectMethodEditUser onSelectMethodEditUser) {
        this.onSelectMethodEditUser = onSelectMethodEditUser;
    }
}
