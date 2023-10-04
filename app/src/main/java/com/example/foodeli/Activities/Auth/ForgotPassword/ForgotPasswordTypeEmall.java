package com.example.foodeli.Activities.Auth.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Auth.ValidationField;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.SendOTPBody;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.SendOTPResponse;
import com.example.foodeli.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordTypeEmall extends AppCompatActivity {

    Pool pool = new Pool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_type_emall);

        FloatingActionButton back_btn = findViewById(R.id.previous_btn);
        Button verify = findViewById(R.id.btn_verify);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView email = findViewById(R.id.field_form);
                String emailData = email.getText().toString();

                if(emailData.isEmpty()) {
                    Toast.makeText(ForgotPasswordTypeEmall.this, "No email provide", Toast.LENGTH_SHORT).show();
                }
                else {
                    ValidationField validationField = new ValidationField(emailData);

                    if(validationField.verifyEmail()) {
                        SendOTPBody body = new SendOTPBody(emailData);
                        Call<SendOTPResponse> sendOTP = pool.getApiCallUserProfile().sendOTP(body);

                        sendOTP.enqueue(new Callback<SendOTPResponse>() {
                            @Override
                            public void onResponse(Call<SendOTPResponse> call, Response<SendOTPResponse> response) {
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
                                    SendOTPResponse res = response.body();
                                    if(res.isSent()) {
                                        Intent changePassIntent = new Intent(ForgotPasswordTypeEmall.this, ChangePassword.class);
                                        changePassIntent.putExtra("uid", res.getUid());
                                        startActivity(changePassIntent);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SendOTPResponse> call, Throwable t) {

                            }
                        });

                    }
                    else {
                        Toast.makeText(ForgotPasswordTypeEmall.this, "Email is not correct to it's format", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}