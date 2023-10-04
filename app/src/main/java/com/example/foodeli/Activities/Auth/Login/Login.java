package com.example.foodeli.Activities.Auth.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.foodeli.Activities.Auth.ForgotPassword.ForgotPasswordTypeEmall;
import com.example.foodeli.Activities.Auth.PasswordShow;
import com.example.foodeli.Activities.Auth.Signup.SignUp;
import com.example.foodeli.Activities.Auth.ValidationField;
import com.example.foodeli.Activities.Home.HomeActivity;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.LoginForm;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.LoginRes;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    
    Pool pool = new Pool();
    private EditText fieldForm, password;
    private ImageButton showPassword;
    private String fieldData, passData;
    PasswordShow passwordShow;
    
    @Override
    protected void onCreate(Bundle saveInstanceStanceState) {
        super.onCreate(saveInstanceStanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_login);

        TextView forgot = (TextView) findViewById(R.id.forgot_link);
        TextView signup = (TextView) findViewById(R.id.signup_link);
        Button login = findViewById(R.id.btn_login);

        fieldForm = findViewById(R.id.field_form);
        password  = findViewById(R.id.password);
        showPassword = findViewById(R.id.login_icon_password);

        PasswordShow passwordShow = new PasswordShow(password, showPassword, this);
        passwordShow.setUp();

        fieldForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fieldData = s.toString();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
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

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPasswordTypeEmall.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fieldData.equals("") && !passData.equals("")) {

                    ValidationField valid = new ValidationField(
                            fieldData,
                            fieldData,
                            passData
                    );

                    LoginForm form = null;

                    if(valid.verifyBothEmailAndPhone()) {

                        if(valid.verifyPhone()) {
                            form = new LoginForm(
                                    "",
                                    fieldData,
                                    passData
                            );
                        }
                        else if(valid.verifyEmail()) {
                            form = new LoginForm(
                                    fieldData,
                                    "",
                                    passData
                            );
                        }

                        Call<LoginRes> login = pool.getApiCallUserProfile().login(form);

                        login.enqueue(new Callback<LoginRes>() {
                            @Override
                            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                                if(response.isSuccessful()) {
                                    Intent homeIntent = new Intent(Login.this, HomeActivity.class);

                                    SharedPreferences uPrefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                                    User userLogined = response.body().getUser();

                                    SharedPreferences.Editor prefsEditor = uPrefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(userLogined);
                                    prefsEditor.putString("user", json);
                                    prefsEditor.apply();

                                    startActivity(homeIntent);
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
                            public void onFailure(Call<LoginRes> call, Throwable t) {
                                System.out.println(t.getMessage().toString());
                            }
                        });
                    }

                }
                else if(fieldData.isEmpty()){
                    Toast.makeText(Login.this, "No email or phone provide", Toast.LENGTH_SHORT).show();
                }
                else if(passData.isEmpty()) {
                    Toast.makeText(Login.this, "No password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}