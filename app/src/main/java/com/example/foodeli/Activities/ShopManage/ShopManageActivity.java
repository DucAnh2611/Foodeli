package com.example.foodeli.Activities.ShopManage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.foodeli.Fragments.Order.OrderFragmentLayout;
import com.example.foodeli.Fragments.Profile.ImageChangeInterface;
import com.example.foodeli.Fragments.Profile.MenuSelectImage;
import com.example.foodeli.Fragments.Profile.OnSelectMenuPicture;
import com.example.foodeli.Fragments.Profile.PreviewPictureDialog;
import com.example.foodeli.Fragments.ShopEmployee.ShopEmployee;
import com.example.foodeli.Fragments.ShopProduct.ShopProduct;
import com.example.foodeli.Fragments.ShopVoucher.ShopVoucher;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ShopManageActivity extends AppCompatActivity {

    private int sid;
    private int CAMERA_PERMISSION_REQUEST_CODE = 1, CAMERA_REQUEST_CODE = 2, REQUEST_CODE_LIBRARY_PICTURE = 3;
    private GetAllShopUserHaveResponse.ShopWithDetail shop;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShopManageViewModel shopManageViewModel;
    private Button confirmEditButton;
    private SupportImage supportImage = new SupportImage();
    private EditText shopName, shopDesc;
    private AppCompatImageView shopImage;
    private boolean isChanged = false;
    private String name, desc;
    private MenuSelectImage dialog;
    private PreviewPictureShopDialog dialog2;
    private Bitmap imageBitmap;

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

        shopName = findViewById(R.id.shop_manage_name);
        shopDesc = findViewById(R.id.shop_manage_desc);
        shopImage = findViewById(R.id.shop_manage_image);
        confirmEditButton = findViewById(R.id.shop_manage_edit_confirm);
        tabLayout = findViewById(R.id.shop_manage_tab_layout);
        viewPager = findViewById(R.id.shop_manage_tab_view_pager);

        confirmEditButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        tabLayout.setupWithViewPager(viewPager);

        ArrayList<Fragment> frags = new ArrayList<>();

        frags.add(new ShopProduct());
        frags.add(new ShopVoucher());
        frags.add(new ShopEmployee());

        setupViewPager(viewPager, frags);

        tabLayout.getTabAt(0).setText("Product");
        tabLayout.getTabAt(1).setText("Voucher");
        tabLayout.getTabAt(2).setText("Employee");

        shopManageViewModel.setShopInfo(shop);
        shopName.setText(shop.getName());
        shopDesc.setText(shop.getDesc());
        shopImage.setImageBitmap(supportImage.convertBase64ToBitmap(shop.getAvatar()));

        shopDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                desc = editable.toString();
                if(!desc.equals(shop.getDesc())) isChanged = true;
                updateUICfBtn();
            }
        });
        shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
                if(!name.equals(shop.getName())) isChanged = true;
                updateUICfBtn();
            }
        });
        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicturePicker();
            }
        });
    }
    public void setupViewPager(ViewPager viewPager, ArrayList<Fragment> frags) {

        OrderFragmentLayout.TabAdapter adapter = new OrderFragmentLayout.TabAdapter(getSupportFragmentManager());

        for (Fragment frag : frags ) {
            adapter.addFragment(frag);
        }

        viewPager.setAdapter(adapter);
    }
    private void updateUICfBtn() {
        if(isChanged) {
            confirmEditButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            confirmEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isChanged = false;
                }
            });
        }
    }
    private void showPicturePicker() {
        dialog = new MenuSelectImage(ShopManageActivity.this);
        dialog.show(getSupportFragmentManager(), "dialog_select_avatar");
        dialog.setOnSelectMenuPicture(new OnSelectMenuPicture() {
            @Override
            public void onTakePicture() {
                if (ContextCompat.checkSelfPermission(ShopManageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // The permission is not granted, request it
                    ActivityCompat.requestPermissions(ShopManageActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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
        dialog2 = new PreviewPictureShopDialog(ShopManageActivity.this, imageBitmap, sid);
        dialog2.show(getSupportFragmentManager(), "dialog_preview_shopImage");

        dialog2.setOnConfirmImage(new ImageChangeInterface() {
            @Override
            public void onSelectedImage() {

            }

            @Override
            public void onConfirmImage() {
                shopImage.setImageBitmap(imageBitmap);
            }
        });
    }
    private void openLibraryPictureChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_LIBRARY_PICTURE);
    }
    private void startImageCaptureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(ShopManageActivity.this.getPackageManager()) != null) {
            // Start the image capture intent
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(ShopManageActivity.this, "No camera application is available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageCaptureIntent();
            } else {
                Toast.makeText(ShopManageActivity.this, "Camera permission is denied", Toast.LENGTH_SHORT).show();
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
            imageBitmap = (Bitmap) extras.get("data");
        }
        if(imageBitmap != null) {
            showPreviewPicture();
            dialog.dismiss();
        }

    }
    public Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}