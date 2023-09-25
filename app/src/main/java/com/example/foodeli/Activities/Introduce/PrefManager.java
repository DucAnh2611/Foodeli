package com.example.foodeli.Activities.Introduce;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String PREF_NAME="IntroScreen";

    private static final String IS_FIRST_LAUNCH="IsFirstLaunch";

    private int MODE_PRIVATE=0;

    PrefManager(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void setIsFirstLaunch(boolean isFirstLaunch){
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch);
        editor.commit();
    }

    public boolean isFirstLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH,true);
    }
}