package com.example.foodeli.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.Body.LoginForm;
import com.example.foodeli.MySqlSetUp.Schemas.User.Response.LoginRes;
import com.example.foodeli.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View LoginView = inflater.inflate(R.layout.fagment_login, container, false);

        Button back = LoginView.findViewById(R.id.bck_btn);
        Button typeEmail = LoginView.findViewById(R.id.login_by_email);
        Button typePhone = LoginView.findViewById(R.id.login_by_phone);
        EditText field = LoginView.findViewById(R.id.field_form);
        Button loginBtn = LoginView.findViewById(R.id.btn_login);

        LoginForm loginForm = new LoginForm("", "", "");
        Pool pool = new Pool();

        final int[] type = {0};
        final String[] form = {"", ""};

        typeEmail.setOnClickListener(v-> {
            type[0] = 0;
            form[1] = "";
            field.setHint("Email");
        });

        typePhone.setOnClickListener(v-> {
            type[0] = 1;
            form[1] = "";
            field.setHint("Phone number");
        });

        loginBtn.setOnClickListener(v -> {
            EditText pass = LoginView.findViewById(R.id.password);
            form[0] = field.getText().toString();
            form[1] = pass.getText().toString();
            System.out.println(field.getText().toString());

            if(type[0]==0) {
                loginForm.setPhone("");
                loginForm.setEmail(form[0]);
            }
            else {
                loginForm.setPhone(form[0]);
                loginForm.setEmail("");
            }

            loginForm.setPassword(form[1]);

            Call<LoginRes> call = pool.getPool().login(loginForm);

            call.enqueue(new Callback<LoginRes>() {
                @Override
                public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                    if (response.code() == 404) {
                        Gson gson = new GsonBuilder().create();
                        ResponseApi res;
                        try {
                            res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                            // handle false;
                            Toast.makeText(LoginView.getContext(), res.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            // handle failure at error parse
                        }
                    }
                    else {
                        Toast.makeText(
                                LoginView.getContext(),
                                String.valueOf(response.body().getUser().getEmail()),
                                Toast.LENGTH_LONG).show();
                        //handle true request
                    }
                }


                @Override
                public void onFailure(Call<LoginRes> call, Throwable t) {
                    Toast.makeText(LoginView.getContext(), "failed", Toast.LENGTH_LONG).show();
                }
            });

        });

        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return LoginView;
    }
}
