package com.example.userone.shoppingproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    Bundle bundle;
    String coming_from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.d("am in ","About Activity");
        bundle=getIntent().getExtras();
        coming_from=bundle.getString("tab_name");
        Log.d("coming_from",coming_from);


    }
}
