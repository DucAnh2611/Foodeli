package com.example.foodeli.Fragment.Introduce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodeli.Fragment.Login.Login;
import com.example.foodeli.R;

public class IntroduceFragment extends Fragment {

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View IntroduceView = inflater.inflate(R.layout.fragment_introduce, container, false);

        Button btn_login = (Button) IntroduceView.findViewById(R.id.login_btn);

        btn_login.setOnClickListener(v -> {
            FragmentAdd(new Login());
        });

        return IntroduceView;
    }

    public void FragmentAdd(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_container, fragment);
        transaction.setReorderingAllowed(true);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
