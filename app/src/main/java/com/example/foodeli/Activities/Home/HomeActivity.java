package com.example.foodeli.Activities.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodeli.Fragments.Home.HomeFragment;
import com.example.foodeli.Fragments.Order.OrderFragmentLayout;
import com.example.foodeli.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navbar_view);

        bottomNavigationView.getMenu().findItem(R.id.empty).setEnabled(false);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.page_home) {
                    HomeFragment homeFragment = new HomeFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    return true;
                }
                else if(itemId == R.id.page_order) {
                    OrderFragmentLayout orderLayout = new OrderFragmentLayout();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, orderLayout).commit();
                    return true;
                }
                return false;
            }
        });

        if(savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.page_home);
        }


    }
}