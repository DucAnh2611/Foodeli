package com.example.foodeli.Activities.Review;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.R;

public class Review extends AppCompatActivity {
    private Pool pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);



    }
}