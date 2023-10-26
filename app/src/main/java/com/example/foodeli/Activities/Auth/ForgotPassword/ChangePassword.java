package com.example.foodeli.Activities.Auth.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.foodeli.Activities.Auth.Login.Login;
import com.example.foodeli.Activities.Auth.PasswordShow;
import com.example.foodeli.Activities.Auth.ValidationField;
import com.example.foodeli.Activities.Home.HomeActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.ChangeForm;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.CreateUserResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.SendOTPResponse;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    Pool pool = new Pool();
    private EditText pass, cfPass;
    private String passData, cfPassData;
    private ImageButton iconPass, iconCfPass;
    private PasswordShow passwordShow, cfPasswordShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent currentIntent = getIntent();
        int uid = currentIntent.getIntExtra("uid", 0);
        System.out.println(uid);

        FloatingActionButton back_btn = findViewById(R.id.previous_btn);
        Button confirm = findViewById(R.id.btn_change_pw);

        pass = findViewById(R.id.pass_form);
        cfPass = findViewById(R.id.cf_pass_form);
        iconPass = findViewById(R.id.changepass_icon_password);
        iconCfPass = findViewById(R.id.changepass_icon_cf_password);

        passData = pass.getText().toString();
        cfPassData = cfPass.getText().toString();

        passwordShow = new PasswordShow(pass, iconPass, this);
        passwordShow.setUp();
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passData = s.toString();
            }
        });

        cfPasswordShow = new PasswordShow(cfPass, iconCfPass, this);
        cfPasswordShow.setUp();
        cfPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cfPassData = s.toString();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!passData.isEmpty() && !cfPassData.isEmpty()) {
                    if(!passData.equals(cfPassData)) {
                        Toast.makeText(ChangePassword.this, "Password is not same", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ValidationField valid1 = new ValidationField();
                        ValidationField valid2 = new ValidationField();

                        valid1.setPassword(passData);
                        valid2.setPassword(cfPassData);

                        if(valid1.verifyPassword() && valid2.verifyPassword()) {

                            ArrayList<ChangeForm> listForm = new ArrayList<>();
                            listForm.add(new ChangeForm("UserPassword", cfPassData) );

                            Call<CreateUserResponse> update = pool.getApiCallUserProfile().update(uid, listForm);

                            update.enqueue(new Callback<CreateUserResponse>() {
                                @Override
                                public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                                    if (response.code() != 200) {
                                        Gson gson = new GsonBuilder().create();
                                        ResponseApi res;
                                        try {
                                            res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                                            System.out.println(res.getMessage());
                                        } catch (IOException e) {
                                            System.out.println("parse err false");
                                        }
                                    }
                                    else {
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CreateUserResponse> call, Throwable t) {

                                }
                            });
                        }
                        else if(!valid1.verifyPassword()) {
                            Toast.makeText(ChangePassword.this, "New password is not correct format", Toast.LENGTH_SHORT).show();
                        }
                        else if(!valid2.verifyPassword()) {
                            Toast.makeText(ChangePassword.this, "Confirm password is not correct format", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else if(passData.isEmpty()) {
                    Toast.makeText(ChangePassword.this, "No new password provided", Toast.LENGTH_SHORT).show();
                }
                else if(cfPassData.isEmpty()){
                    Toast.makeText(ChangePassword.this, "No confirm password provided", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}