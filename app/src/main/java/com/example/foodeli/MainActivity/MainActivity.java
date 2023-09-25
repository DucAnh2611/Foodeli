package com.example.foodeli.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.foodeli.Activities.Home.HomeActivity;
import com.example.foodeli.Activities.Introduce.IntroActivity;
import com.example.foodeli.R;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceStanceState) {
        super.onCreate(saveInstanceStanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
//        User user = gson.fromJson(json, User.class);

        Intent i  = new Intent(MainActivity.this, IntroActivity.class);;
        if(!json.isEmpty()) {
            i = new Intent(MainActivity.this, HomeActivity.class);
//            mPrefs.edit().remove("user").apply();
        }


        startActivity(i);
        finish();

    }

}
