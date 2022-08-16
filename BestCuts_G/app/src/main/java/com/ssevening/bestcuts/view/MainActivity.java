package com.ssevening.bestcuts.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ssevening.bestcuts.R;
import com.ssevening.bestcuts.object.PipeInfo;
import com.ssevening.bestcuts.object.PipeInfoAdapter;

public class MainActivity extends BaseActivity {

    static int totalLength = 19;
    static ArrayList<PipeInfo> results = new ArrayList<PipeInfo>();
    private ListView listView;
    private PipeInfoAdapter pipeInfoAdapter;
    private Button btnAdd;
    private Button btn_best_cut;
    private EditText et_count;
    private EditText et_length;
    private EditText et_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        btn_best_cut = (Button) findViewById(R.id.btn_best_cut);
        btnAdd = (Button) findViewById(R.id.btn_add);
        et_length = (EditText) findViewById(R.id.et_length);
        et_count = (EditText) findViewById(R.id.et_count);
        et_total = (EditText) findViewById(R.id.et_total);

        pipeInfoAdapter = new PipeInfoAdapter(this, results);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(pipeInfoAdapter);
        listView.setStackFromBottom(true);
        listView.setTranscriptMode(2);
        listView.setDividerHeight(1);

        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String total = et_total.getText().toString();
                if (TextUtils.isEmpty(total)) {
                    Toast.makeText(MainActivity.this, "ԭ�����ܳ��Ȳ���Ϊ��!", Toast.LENGTH_SHORT).show();
                    et_total.requestFocus();
                    return;
                }

                String length = et_length.getText().toString();
                if (TextUtils.isEmpty(length)) {
                    Toast.makeText(MainActivity.this, "���Ȳ���Ϊ��!", Toast.LENGTH_SHORT).show();
                    et_length.requestFocus();
                    return;
                }
                if (Integer.parseInt(length) > Integer.parseInt(total)) {
                    Toast.makeText(MainActivity.this, "�������Ȳ��ܴ����ܳ���!", Toast.LENGTH_SHORT).show();
                    et_length.requestFocus();
                    return;
                }
                String count = et_count.getText().toString();
                if (TextUtils.isEmpty(count)) {
                    Toast.makeText(MainActivity.this, "��������Ϊ��!", Toast.LENGTH_SHORT).show();
                    et_count.requestFocus();
                    return;
                }
                results.add(new PipeInfo(Integer.parseInt(length), Integer.parseInt(count)));
                pipeInfoAdapter.notifyDataSetChanged();
                et_length.setText("");
                et_count.setText("");
                et_length.requestFocus();
            }
        });

        btn_best_cut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String total = et_total.getText().toString();
                if (TextUtils.isEmpty(total)) {
                    Toast.makeText(MainActivity.this, "ԭ�����ܳ��Ȳ���Ϊ��", Toast.LENGTH_SHORT).show();
                    return;
                }

                // if (Integer.parseInt(total) > 20) {
                // Toast.makeText(MainActivity.this, "ԭ�����ܳ��ȴ���20������ϵ���߻�ȡ�����ư�!", Toast.LENGTH_SHORT).show();
                // return;
                // }

                if (results == null || results.isEmpty()) {
                    Toast.makeText(MainActivity.this, "������Ҫһ���и�����!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(MainActivity.this, CutResultActivity.class);
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < results.size(); j++) {
                    PipeInfo p = results.get(j);
                    sb.append(p.getLength()).append("x").append(p.getCount()).append(" ");
                }
                i.putExtra("resultStr", sb.toString());
                i.putExtra("totalLength", Integer.parseInt(total));
                startActivity(i);

            }
        });

        Collections.sort(results, new Comparator<PipeInfo>() {

            @Override
            public int compare(PipeInfo b1, PipeInfo b2) {
                if (b1.getLength() > b2.getLength()) {
                    return -1;
                } else if (b1.getLength() == b2.getLength()) {
                    return 0;
                } else if (b1.getLength() < b2.getLength()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("开始切割");

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(MainActivity.this).setTitle("��ʾ").setMessage("ȷ��Ҫ�˳��и�༭��?").setPositiveButton("ȷ��",
                    new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            if (dialog != null) {
                                dialog.cancel();
                            }
                            finish();
                        }
                    }).setNegativeButton("ȡ��",
                    new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.cancel();
                        }
                    }).create().show();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public String getPageName() {
        return MainActivity.class.getName();
    }
}
