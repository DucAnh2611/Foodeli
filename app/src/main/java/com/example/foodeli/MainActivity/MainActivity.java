package com.example.foodeli.MainActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.foodeli.Introduce.IntroduceFragment;
import com.example.foodeli.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceStanceState) {
        super.onCreate(saveInstanceStanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, new IntroduceFragment())
            .commit();

    }

}
