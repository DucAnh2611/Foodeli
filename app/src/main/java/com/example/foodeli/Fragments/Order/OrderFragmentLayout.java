package com.example.foodeli.Fragments.Order;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.foodeli.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragmentLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragmentLayout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabLayout tabLayout = null;
    ViewPager viewPager = null;

    public OrderFragmentLayout() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderActiveTab.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragmentLayout newInstance(String param1, String param2) {
        OrderFragmentLayout fragment = new OrderFragmentLayout();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_layout_tab, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.tab_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        ArrayList<Fragment> frags = new ArrayList<>();

        frags.add(new OrderActive());
        frags.add(new OrderCompleted());
        frags.add(new OrderCancelled());

        setupViewPager(viewPager, frags);

        tabLayout.getTabAt(0).setText("Active");
        tabLayout.getTabAt(1).setText("Completed");
        tabLayout.getTabAt(2).setText("Cancelled");

        return view;
    }

    public void setupViewPager(ViewPager viewPager, ArrayList<Fragment> frags) {

        TabAdapter adapter = new TabAdapter(getChildFragmentManager());

        for (Fragment frag : frags ) {
            adapter.addFragment(frag);
        }

        viewPager.setAdapter(adapter);

    }

    public static class TabAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragmentArrayList= new ArrayList<>();

        public TabAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            fragmentArrayList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

    }

}