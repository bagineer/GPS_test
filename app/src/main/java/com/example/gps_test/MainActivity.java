package com.example.gps_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("test", "HelloWorld!");
        Log.d("test", "HelloWorld!");
        Log.d("test", "HelloWorld!2");
        Log.d("test", "HelloWorld!2");
    }
}