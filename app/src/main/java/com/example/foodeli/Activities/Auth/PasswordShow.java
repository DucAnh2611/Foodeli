package com.example.foodeli.Activities.Auth;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.foodeli.R;

public class PasswordShow {
    private boolean show = false;
    private EditText editText;
    private ImageButton icon;
    private Context context;

    public PasswordShow(EditText editText, ImageButton icon, Context context) {
        this.editText = editText;
        this.icon = icon;
        this.context = context;
    }

    public void setUp() {

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = !show;
                if(show) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    icon.setImageResource(R.drawable.eye_close);

                }
                else {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    icon.setImageResource(R.drawable.eye_open);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    editText.setTypeface(context.getResources().getFont(R.font.poppins_regular));
                }
                editText.setLetterSpacing(0);
            }
        });
    }
}
