package com.example.foodeli.MainActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodeli.Introduce.IntroduceFragment;
import com.example.foodeli.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceStanceState) {
        super.onCreate(saveInstanceStanceState);
        setContentView(R.layout.activity_main);

        IntroduceFragment IntroduceFragment = new IntroduceFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, IntroduceFragment)
                .commit();

    }

}
