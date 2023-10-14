package com.example.foodeli.Activities.ShopManage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.foodeli.Fragments.Order.OrderFragmentLayout;
import com.example.foodeli.Fragments.ShopEmployee.ShopEmployee;
import com.example.foodeli.Fragments.ShopProduct.ShopProduct;
import com.example.foodeli.Fragments.ShopVoucher.ShopVoucher;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ShopManageActivity extends AppCompatActivity {

    private int sid;
    private GetAllShopUserHaveResponse.ShopWithDetail shop;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShopManageViewModel shopManageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manage);

        shopManageViewModel = new ViewModelProvider(this).get(ShopManageViewModel.class);

        Intent shopUser = getIntent();
        shop = (GetAllShopUserHaveResponse.ShopWithDetail) shopUser.getSerializableExtra("shop");
        sid = shop.getSid();

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.shop_manage_tab_layout);
        viewPager = findViewById(R.id.shop_manage_tab_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        ArrayList<Fragment> frags = new ArrayList<>();

        frags.add(new ShopProduct());
        frags.add(new ShopVoucher());
        frags.add(new ShopEmployee());

        setupViewPager(viewPager, frags);

        tabLayout.getTabAt(0).setText("Product");
        tabLayout.getTabAt(1).setText("Voucher");
        tabLayout.getTabAt(2).setText("Employee");

        if(shop!=null) {
            shopManageViewModel.setShopInfo(shop);
        }
        else {
            shopManageViewModel.getShopInfo(sid).observe(this, new Observer<GetAllShopUserHaveResponse.ShopWithDetail>() {
                @Override
                public void onChanged(GetAllShopUserHaveResponse.ShopWithDetail shopWithDetail) {
                    shop = shopWithDetail;
                }
            });

        }
    }
    public void setupViewPager(ViewPager viewPager, ArrayList<Fragment> frags) {

        OrderFragmentLayout.TabAdapter adapter = new OrderFragmentLayout.TabAdapter(getSupportFragmentManager());

        for (Fragment frag : frags ) {
            adapter.addFragment(frag);
        }

        viewPager.setAdapter(adapter);
    }

}