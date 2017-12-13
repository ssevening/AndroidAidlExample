package com.ssevening.www.androidaidlexample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ssevening.www.base.MyServices;
import com.ssevening.www.moduleproduct.ProductActivity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_go_to_hostel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(i);
            }
        });

        registerService(this);
    }

    public static void registerService(Context c) {
        Properties props = new Properties();
        try {
            InputStream in = c.getResources().openRawResource(R.raw.services);
            props.load(in);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        for (Object object : props.keySet()) {
            MyServices.register((String) object, (String) props.get(object));
        }
    }
}
