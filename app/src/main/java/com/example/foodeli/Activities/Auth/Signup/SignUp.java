package com.example.foodeli.Activities.Auth.Signup;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodeli.Activities.Auth.PasswordShow;
import com.example.foodeli.Activities.Auth.ValidationField;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.CreateAccount;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.CreateUserResponse;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    Pool pool = new Pool();
    private boolean show = false;
    private EditText email, fullname, phone, password;
    private ImageButton showPassword;
    PasswordShow passwordShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner day = findViewById(R.id.day_form);
        Spinner month = findViewById(R.id.month_form);
        Spinner year = findViewById(R.id.year_form);
        Button signup_btn = findViewById(R.id.signup_btn);


        email = findViewById(R.id.email_form);
        fullname = findViewById(R.id.fullname_form);
        phone = findViewById(R.id.phone_form);
        password = findViewById(R.id.password);

        showPassword = findViewById(R.id.signup_icon_password);

        passwordShow = new PasswordShow(password, showPassword, this);
        passwordShow.setUp();

        DatePickerCustom datePickerCustom = new DatePickerCustom(day, month, year, 50,this);
        datePickerCustom.setup();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailData = email.getText().toString();
                String fullnameData = fullname.getText().toString();
                String phoneData = phone.getText().toString();
                String passwordData = password.getText().toString();

                ValidationField valid = new ValidationField(emailData, phoneData, passwordData, fullnameData);
                if(valid.isCorrectFormat()) {
                    CreateAccount newAcc = new CreateAccount(
                            fullnameData, emailData, passwordData, phoneData,
                            String.valueOf(datePickerCustom.getSelectYear()) + "-" +
                                    String.valueOf(datePickerCustom.getSelectMonth()) + "-" +
                                    String.valueOf(datePickerCustom.getSelectDay()));

                    Call<CreateUserResponse> createUser = pool.getApiCallUserProfile().signup(newAcc);

                    createUser.enqueue(new Callback<CreateUserResponse>() {
                        @Override
                        public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                            if(response.isSuccessful()) {
                                finish();
                            }
                            else {
                                Gson gson = new GsonBuilder().create();
                                ResponseApi res;
                                try {
                                    res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                                    System.out.println(res.getMessage());

                                    Toast toast = Toast.makeText(getApplicationContext(), res.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();

                                } catch (IOException e) {
                                    System.out.println("parse err false");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateUserResponse> call, Throwable t) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error on format", Toast.LENGTH_LONG).show();
                }

            }

        });


        TextView loginLink = findViewById(R.id.login_link);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}