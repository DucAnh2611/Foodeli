package com.example.foodeli.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodeli.R;

public class Login extends Fragment {

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View IntroduceView = inflater.inflate(R.layout.fagment_login, container, false);

        Button back = (Button) IntroduceView.findViewById(R.id.back_btn);

        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return IntroduceView;
    }
}
