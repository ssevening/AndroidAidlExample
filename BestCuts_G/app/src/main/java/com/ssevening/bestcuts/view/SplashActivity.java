package com.ssevening.bestcuts.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;


import androidx.appcompat.app.AppCompatActivity;

import com.ssevening.bestcuts.R;

public class SplashActivity extends BaseActivity {

    private View btnBestCut;
    private View btnHelp;
    private View btnAbout;
    private View btnConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        btnBestCut = findViewById(R.id.btnBestCut);
        btnHelp = findViewById(R.id.btnHelp);
        btnAbout = findViewById(R.id.btnAbout);
        btnConfig = findViewById(R.id.btnConfig);
        btnBestCut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        btnHelp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, HelpActivity.class);
                startActivity(i);

            }
        });

        btnAbout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, AboutActivity.class);
                startActivity(i);

            }
        });

        btnConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public String getPageName() {
        return SplashActivity.class.getName();
    }
}
