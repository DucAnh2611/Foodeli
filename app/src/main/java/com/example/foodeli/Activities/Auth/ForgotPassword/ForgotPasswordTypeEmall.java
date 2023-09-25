package com.example.foodeli.Activities.Auth.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodeli.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForgotPasswordTypeEmall extends AppCompatActivity {

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
                startActivity(new Intent(ForgotPasswordTypeEmall.this, ChangePassword.class));
                finish();
            }
        });
    }
}