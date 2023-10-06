package com.example.foodeli.Activities.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.foodeli.Activities.Find.FindActivity;
import com.example.foodeli.Fragments.Home.HomeFragment;
import com.example.foodeli.Fragments.Order.OrderFragmentLayout;
import com.example.foodeli.Fragments.Profile.ProfileFragment;
import com.example.foodeli.Fragments.Shop.ShopFragment;
import com.example.foodeli.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navbar_view);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_icon);

        bottomNavigationView.getMenu().findItem(R.id.empty).setEnabled(false);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.page_home) {
                    HomeFragment homeFragment = new HomeFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    return true;
                }
                else if (itemId == R.id.page_order) {
                    OrderFragmentLayout orderLayout = new OrderFragmentLayout();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, orderLayout).commit();
                    return true;
                }
                else if (itemId == R.id.page_shop) {
                    ShopFragment shopFragment = new ShopFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, shopFragment).commit();
                    return true;
                }
                else if (itemId == R.id.page_profile) {
                    ProfileFragment profileFragment = new ProfileFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                    return true;
                }

                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findIntent = new Intent(HomeActivity.this, FindActivity.class);
                startActivity(findIntent);
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.page_home);
        }

    }
}