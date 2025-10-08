package com.example.javadesignpatterntutorials;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);
        supplyDependencies();
    }

    public abstract void supplyDependencies();

}