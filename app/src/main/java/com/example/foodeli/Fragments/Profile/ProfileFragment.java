package com.example.foodeli.Fragments.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodeli.Activities.Auth.Login.Login;
import com.example.foodeli.Activities.Find.FilterScreenDialog;
import com.example.foodeli.MySqlSetUp.Schemas.General.Body.Category;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView username, phone;
    private ShapeableImageView avatar;
    private LinearLayout paymentLay, addressLay, voucherLay, logoutLay;
    private AppCompatButton changeAva;
    private Bitmap imageBitmap;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        avatar = view.findViewById(R.id.profile_avatar);
        username = view.findViewById(R.id.profile_username);
        phone = view.findViewById(R.id.profile_phone);

        changeAva = view.findViewById(R.id.profile_btn_edit);
        paymentLay = view.findViewById(R.id.profile_btn_payment);
        addressLay = view.findViewById(R.id.profile_btn_address);
        voucherLay = view.findViewById(R.id.profile_btn_voucher);
        logoutLay = view.findViewById(R.id.profile_btn_logout);

        byte[] decodedSString = Base64.decode(user.getAvatar(), Base64.DEFAULT);
        Bitmap decodedSByte = BitmapFactory.decodeByteArray(decodedSString, 0, decodedSString.length);

        avatar.setImageBitmap(decodedSByte);
        username.setText(user.getName());
        phone.setText(user.getMobile());

        changeAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePicker();
            }
        });

        logoutLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getContext(), Login.class);

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.remove("user");

                if(editor.commit()) {
                    startActivity(loginIntent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    private void showPicturePicker() {
        ChangePicturePickDialog dialog = new ChangePicturePickDialog(getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_select_avatar");
        dialog.setOnSelectImage(new ChangePicturePickDialog.OnSelectedImage() {
            @Override
            public void onSelectedImage() {
                imageBitmap = dialog.getImageBitmap();
                showPreviewPicture();
            }
        });
    }

    private void showPreviewPicture() {
        PreviewPictureDialog dialog2 = new PreviewPictureDialog(getContext(), imageBitmap, user.getId());
        dialog2.show(getActivity().getSupportFragmentManager(), "dialog_preview_avatar");
    }

}