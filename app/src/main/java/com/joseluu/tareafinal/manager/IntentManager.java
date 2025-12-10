package com.joseluu.tareafinal.manager;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class IntentManager {

    /*
    This class is to do more simplify this function but i dont know
    how to do it
     */
    public void startActivityWithIntent(AppCompatActivity appCompatActivity, Class<AppCompatActivity> appCompatActivity1){
        try {
            Intent intent = new Intent(appCompatActivity.getClass().newInstance(), appCompatActivity1.getClass());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
