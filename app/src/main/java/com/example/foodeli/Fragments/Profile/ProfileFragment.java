package com.example.foodeli.Fragments.Profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Activities.Address.Address;
import com.example.foodeli.Activities.Auth.Login.Login;
import com.example.foodeli.Activities.MethodCheckout.PaymentMethodActivity;
import com.example.foodeli.Activities.Voucher.Voucher;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private static final int REQUEST_CODE_LIBRARY_PICTURE = 1;
    private static final int CAMERA_REQUEST_CODE = 100, CAMERA_PERMISSION_REQUEST_CODE =50;
    private TextView username, phone;
    private ShapeableImageView avatar;
    private LinearLayout paymentLay, addressLay, voucherLay, logoutLay;
    private AppCompatButton changeAva;
    private Bitmap imageBitmap;
    private User user;
    private MenuSelectImage dialog;
    private PreviewPictureDialog dialog2;
    private SupportImage supportImage = new SupportImage();

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

        paymentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(getContext(), PaymentMethodActivity.class);
                paymentIntent.putExtra("uid", user.getId());
                getContext().startActivity(paymentIntent);
            }
        });

        addressLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(getContext(), Address.class);
                addressIntent.putExtra("uid", user.getId());
                getContext().startActivity(addressIntent);
            }
        });

        voucherLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucherIntent = new Intent(getContext(), Voucher.class);
                voucherIntent.putExtra("uid", user.getId());
                getContext().startActivity(voucherIntent);
            }
        });

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
        dialog = new MenuSelectImage(getContext());
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_select_avatar");
        dialog.setOnSelectMenuPicture(new OnSelectMenuPicture() {
            @Override
            public void onTakePicture() {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // The permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    startImageCaptureIntent();
                }
            }

            @Override
            public void onSelectFromLibrary() {
                openLibraryPictureChooser();
            }
        });
    }
    private void showPreviewPicture() {
        dialog2 = new PreviewPictureDialog(getContext(), imageBitmap, user.getId());
        dialog2.show(getActivity().getSupportFragmentManager(), "dialog_preview_avatar");

        dialog2.setOnConfirmImage(new ImageChangeInterface() {
            @Override
            public void onSelectedImage() {

            }

            @Override
            public void onConfirmImage() {
                avatar.setImageBitmap(imageBitmap);
            }
        });
    }
    private void openLibraryPictureChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_LIBRARY_PICTURE);
    }
    private void startImageCaptureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), "No camera application is available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageCaptureIntent();
            } else {
                Toast.makeText(getContext(), "Camera permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LIBRARY_PICTURE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imageBitmap = getBitmapFromUri(selectedImageUri);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = supportImage.convert(supportImage.CompressBitMap(150, (Bitmap) extras.get("data")));
        }
        if(imageBitmap != null) {
            showPreviewPicture();
            dialog.dismiss();
        }

    }
    public Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            byte[] compressedImage = supportImage.CompressBitMap(100, bitmap);
            bitmap = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);

            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}