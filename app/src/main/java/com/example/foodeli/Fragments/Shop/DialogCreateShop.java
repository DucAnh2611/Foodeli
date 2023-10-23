package com.example.foodeli.Fragments.Shop;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.Fragments.Profile.MenuSelectImage;
import com.example.foodeli.Fragments.Profile.OnSelectMenuPicture;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;

public class DialogCreateShop extends DialogFragment {

    private final int CAMERA_PERMISSION_REQUEST_CODE = 2, REQUEST_CODE_LIBRARY_PICTURE = 3, CAMERA_REQUEST_CODE = 4;
    private Context context;
    private EditText sName, sDesc, sAddress;
    private ShapeableImageView sImage;
    private AppCompatButton cancelCreate, confirmCreate;
    private GetAllShopUserHaveResponse.ShopWithDetail shop;
    private OnCreateShopSelect onCreateShopSelect;
    private String name, desc, address;
    private Bitmap image;
    private MenuSelectImage dialog;
    private SupportImage supportImage = new SupportImage();

    public DialogCreateShop(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_form_create_shop, null);
        dialog.setContentView(view);

        sName = view.findViewById(R.id.dialog_create_shop_name);
        sDesc = view.findViewById(R.id.dialog_create_shop_desc);
        sAddress = view.findViewById(R.id.dialog_create_shop_address);
        sImage = view.findViewById(R.id.dialog_create_shop_image);
        cancelCreate = view.findViewById(R.id.dialog_create_shop_cancel);
        confirmCreate = view.findViewById(R.id.dialog_create_shop_confirm);

        name = desc = address = "";

        sName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }
        });
        sDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                desc = s.toString();
            }
        });
        sAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                address = s.toString();
            }
        });

        sImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePicker();
            }
        });

        cancelCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateShopSelect.onCancelShop();
            }
        });

        confirmCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.equals("") && !address.equals("") && !desc.equals("") && image!= null) {
                    onCreateShopSelect.onCreatedShop(name, desc, address, supportImage.bitmapToBase64(image));
                }else {
                    Toast.makeText(context, "All field require!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
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
            image = getBitmapFromUri(selectedImageUri);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
        }
        if(image != null) {
            sImage.setImageBitmap(image);
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

    public interface OnCreateShopSelect {
        void onCreatedShop(String name, String desc, String address, String image);
        void onCancelShop();
    }

    public void setOnCreateShopSelect(OnCreateShopSelect onCreateShopSelect) {
        this.onCreateShopSelect = onCreateShopSelect;
    }
}
