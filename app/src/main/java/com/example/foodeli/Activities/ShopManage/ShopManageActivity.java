package com.example.foodeli.Activities.ShopManage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodeli.Fragments.Order.OrderFragmentLayout;
import com.example.foodeli.Fragments.Profile.ImageChangeInterface;
import com.example.foodeli.Fragments.Profile.MenuSelectImage;
import com.example.foodeli.Fragments.Profile.OnSelectMenuPicture;
import com.example.foodeli.Fragments.ShopEmployee.ShopEmployee;
import com.example.foodeli.Fragments.ShopOrder.ShopOrder;
import com.example.foodeli.Fragments.ShopProduct.DynamicHeightGridView;
import com.example.foodeli.Fragments.ShopProduct.ShopProduct;
import com.example.foodeli.Fragments.ShopVoucher.ShopVoucher;
import com.example.foodeli.MySqlSetUp.Pool;
import com.example.foodeli.MySqlSetUp.ResponseApi;
import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body.UpdateShopBody;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllShopUserHaveResponse;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetShopInformationResponse;
import com.example.foodeli.R;
import com.example.foodeli.Supports.SupportImage;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopManageActivity extends AppCompatActivity {

    private int sid;
    private int CAMERA_PERMISSION_REQUEST_CODE = 1, CAMERA_REQUEST_CODE = 2, REQUEST_CODE_LIBRARY_PICTURE = 3;
    private static GetAllShopUserHaveResponse.ShopWithDetail shop;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ShopManageViewModel shopManageViewModel;
    private TextView sAddress, sRate, sSold, sProduct;
    private AppCompatButton confirmEditButton;
    private SupportImage supportImage = new SupportImage();
    private EditText shopName, shopDesc;
    private AppCompatImageView shopImage;
    private boolean isChanged = false;
    private String name = "", desc = "";
    private MenuSelectImage dialog;
    private PreviewPictureShopDialog dialog2;
    private Bitmap imageBitmap;
    private User user;
    private boolean adjustP = false, adjustO = false, adjustE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manage);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String json = mPrefs.getString("user", "");
        user = gson.fromJson(json, User.class);

        shopManageViewModel = new ViewModelProvider(this).get(ShopManageViewModel.class);

        Intent shopUser = getIntent();
        shop = (GetAllShopUserHaveResponse.ShopWithDetail) shopUser.getSerializableExtra("shop");
        sid = shop.getSid();

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopUser.putExtra("shop", shop);
                shopUser.putExtra("position", shopUser.getIntExtra("position", 0));
                shopUser.putExtra("adjustP", adjustP);
                shopUser.putExtra("adjustO", adjustO);
                shopUser.putExtra("adjustE", adjustE);
                setResult(RESULT_OK, shopUser);
                finish();
            }
        });

        shopName = findViewById(R.id.shop_manage_name);
        shopDesc = findViewById(R.id.shop_manage_desc);
        shopImage = findViewById(R.id.shop_manage_image);
        sAddress = findViewById(R.id.shop_manage_address);
        confirmEditButton = findViewById(R.id.shop_manage_edit_confirm);
        sRate = findViewById(R.id.shop_manage_rate);
        sSold = findViewById(R.id.shop_manage_sold);
        sProduct = findViewById(R.id.shop_manage_product);
        tabLayout = findViewById(R.id.shop_manage_tab_layout);
        viewPager = findViewById(R.id.shop_manage_tab_view_pager);

        confirmEditButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        ArrayList<Fragment> frags = new ArrayList<>();

        ShopProduct shopProduct = ShopProduct.newInstance(sid);
        ShopVoucher shopVoucher = ShopVoucher.newInstance(sid);
        ShopEmployee shopEmployee = ShopEmployee.newInstance(sid);
        ShopOrder shopOrder = ShopOrder.newInstance(sid);

        shopProduct.setOnCreateNewProduct(new ShopProduct.OnCreateNewProduct() {
            @Override
            public void onCreate() {
                shop.setProductQuantity(shop.getProductQuantity() + 1);
                sProduct.setText(shop.getProductQuantity() + " Product");
                if(!adjustP) adjustP = true;
            }

            @Override
            public void onDelete() {
                shop.setProductQuantity(shop.getProductQuantity() - 1);
                sProduct.setText(shop.getProductQuantity() + " Product");

                if(!adjustP) adjustP = true;
            }
        });
        shopOrder.setOnAdjustOrder(new ShopOrder.OnAdjustOrder() {
            @Override
            public void onAdjust() {
                if(!adjustO) adjustO = true;
            }
        });
        shopEmployee.setOnAdjustOrder(new ShopOrder.OnAdjustOrder() {
            @Override
            public void onAdjust() {
                if(!adjustE) adjustE = true;
            }
        });

        frags.add(shopProduct);
        frags.add(shopVoucher);
        frags.add(shopEmployee);
        frags.add(shopOrder);

        TabAdapter adapter = new TabAdapter(ShopManageActivity.this);

        for (Fragment frag : frags ) {
            adapter.addFragment(frag);
        }
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Product");
                    break;
                case 1:
                    tab.setText("Voucher");
                    break;
                case 2:
                    tab.setText("Employee");
                    break;
                case 3:
                    tab.setText("Order");
                    break;
                default:
                    tab.setText("Undefined");
                    break;
            }
        }).attach();

        shopManageViewModel.setShopInfo(shop);
        shopName.setText(shop.getName());
        shopDesc.setText(shop.getDesc());
        sAddress.setText("Address: " + shop.getAddress());
        shopImage.setImageBitmap(supportImage.convertBase64ToBitmap(shop.getAvatar()));
        sRate.setText(String.valueOf(shop.getShopRating()));
        sSold.setText(shop.getSold() + " Sold");
        sProduct.setText(shop.getProductQuantity() + " Product");

        name = shop.getName();
        desc = shop.getDesc();

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
                if(!desc.equals(shop.getDesc()) || !name.equals(shop.getName())) isChanged = true;
                else isChanged = false;
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
                if(!desc.equals(shop.getDesc()) || !name.equals(shop.getName())) isChanged = true;
                else isChanged = false;
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

    private void updateUICfBtn() {
        if(isChanged) {
            confirmEditButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            confirmEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmChangeShopData(user.getId());
                }
            });
        }
        else {
            confirmEditButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
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
            imageBitmap = supportImage.convert(supportImage.CompressBitMap(150, (Bitmap) extras.get("data")));
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

            byte[] compressedImage = supportImage.CompressBitMap(100, bitmap);
            bitmap = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);

            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmChangeShopData(int uid) {
        ArrayList<UpdateShopBody> mapVal = new ArrayList<>();

        mapVal.add(new UpdateShopBody(uid, sid, "name", name ));
        mapVal.add(new UpdateShopBody(uid, sid, "desc", desc ));

        Pool pool = new Pool();

        for (UpdateShopBody body: mapVal) {
            Call<GetShopInformationResponse> updateData = pool.getApiCallUserShop().updateShop("update", body);
            updateData.enqueue(new Callback<GetShopInformationResponse>() {
                @Override
                public void onResponse(Call<GetShopInformationResponse> call, Response<GetShopInformationResponse> response) {
                    if (response.code() != 200) {
                        Gson gson = new GsonBuilder().create();
                        ResponseApi res;
                        try {
                            res = gson.fromJson(response.errorBody().string(), ResponseApi.class);
                            System.out.println(res.getMessage());
                            Toast.makeText(ShopManageActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            System.out.println("parse err false");
                        }
                        isChanged = true;
                    }
                    else {
                        shop = response.body().getInfo();
                        isChanged = false;
                        updateUICfBtn();
                    }
                }

                @Override
                public void onFailure(Call<GetShopInformationResponse> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }

    }

    public static class TabAdapter extends FragmentStateAdapter {

        ArrayList<Fragment> fragmentArrayList= new ArrayList<>();

        public TabAdapter(@NonNull FragmentActivity fa) {
            super(fa);
        }

        public void addFragment(Fragment fragment) {
            fragmentArrayList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentArrayList.size();
        }

        public Fragment getFragment(int position) {
            return fragmentArrayList.get(position);
        }

    }

}