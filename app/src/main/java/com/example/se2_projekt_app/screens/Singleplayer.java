package com.example.se2_projekt_app.screens;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.Button;

import com.example.se2_projekt_app.R;

public class Singleplayer extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleplayer);

        findViewById(R.id.singleplayer_back).setOnClickListener(v -> finish());
    }
}
