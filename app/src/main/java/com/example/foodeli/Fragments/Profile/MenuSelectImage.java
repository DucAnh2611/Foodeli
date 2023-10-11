package com.example.foodeli.Fragments.Profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.foodeli.R;

public class MenuSelectImage extends DialogFragment {

    private Context context;
    private AppCompatButton takePicture, choosePicture;
    private OnSelectMenuPicture onSelectMenuPicture;
    private Dialog dialog;

    Bitmap imageBitmap = null;
    public MenuSelectImage(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_profile_change_avatar, null);
        dialog.setContentView(view);

        takePicture = view.findViewById(R.id.change_picture_take);
        choosePicture = view.findViewById(R.id.change_picture_choose);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMenuPicture.onTakePicture();
            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectMenuPicture.onSelectFromLibrary();
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 800;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    public void setOnSelectMenuPicture(OnSelectMenuPicture onSelectMenuPicture) {this.onSelectMenuPicture = onSelectMenuPicture;}
}
