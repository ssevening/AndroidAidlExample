package com.ssevening.bestcuts.view;

import java.io.IOException;
import java.io.InputStream;


import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ssevening.bestcuts.R;

/**
 * ϵͳ����
 */
public class AboutActivity extends BaseActivity {

    private TextView txtHelp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        txtHelp = (TextView) findViewById(R.id.txtHelp);
        try {
            txtHelp.setText(getAssetsData());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            actionBar.setTitle("关于作者");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAssetsData() {
        String result = "";
        try {
            //获取输入流
            InputStream mAssets = getAssets().open("about.txt");

            //获取文件的字节数
            int lenght = mAssets.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer, "GBK");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public String getPageName() {
        return AboutActivity.class.getName();
    }
}
