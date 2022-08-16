package com.ssevening.bestcuts.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssevening.bestcuts.R;
import com.ssevening.bestcuts.object.CutResultAdapter;
import com.ssevening.bestcuts.object.PipeInfo;
import com.ssevening.bestcuts.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CutResultActivity extends BaseActivity {

    private int                   totalLength = 0;
    private static List<PipeInfo> results;
    private static StringBuffer   sb;
    private static int            totalCount;
    private ExpandableListView    el_cut_resut;
    public String[]               pipeParentResultArray;
    public String[][]             pipeChildResultArray;
    private View                  ll_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cut_result);
        ll_loading = (View) findViewById(R.id.ll_loading);
        results = new ArrayList<PipeInfo>();
        sb = new StringBuffer();
        totalCount = 0;
        // ������и�����
        Utils.setSharedPerferences(this, "cutted", "");
        el_cut_resut = (ExpandableListView) findViewById(R.id.el_cut_resut);
        el_cut_resut.setGroupIndicator(null);

        String resultStr = getIntent().getExtras().getString("resultStr");
        totalLength = getIntent().getExtras().getInt("totalLength");
        Toast.makeText(this, "计算成功", Toast.LENGTH_LONG).show();
        CutDataASyncTask cut = new CutDataASyncTask(this);
        cut.execute(resultStr);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private static boolean make(int current, int delta, int to, String seq) {
        if (current + delta == to) {
            // System.out.println(totalLength + ":\t" + seq);
            sb.append(seq).append("\r\n");
            totalCount = totalCount + 1;
            return true;
        } else if (current + delta > to) {
            return false;
        } else {
            for (int i = 0; i < results.size(); i++) {
                PipeInfo p = results.get(i);
                if (p.getCount() > 0) {
                    int new_delta = p.getLength();
                    p.setCount(p.getCount() - 1);
                    if (make(current + delta, new_delta, to, seq + "," + new_delta)) {
                        return true;
                    }
                    p.setCount(p.getCount() + 1);
                }
            }
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("提醒").setMessage("确定返回首页吗").setPositiveButton("返回首页",
                                                                                                       new AlertDialog.OnClickListener() {

                                                                                                           @Override
                                                                                                           public void onClick(DialogInterface dialog,
                                                                                                                               int which) {
                                                                                                               if (dialog != null) {
                                                                                                                   dialog.cancel();
                                                                                                               }
                                                                                                               finish();

                                                                                                           }
                                                                                                       }).setNegativeButton("取消",
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class CutDataASyncTask extends AsyncTask<Object, Integer, String> {

        private Context context;

        CutDataASyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Object... params) {
            /**
             * ��������
             */
            String[] needs = ((String) params[0]).split(" ");

            for (int i = 0; i < needs.length; i++) {
                String[] cuts = needs[i].split("x");
                int length = Integer.parseInt(cuts[0]);
                int count = Integer.parseInt(cuts[1]);
                PipeInfo p = new PipeInfo(length, count);
                results.add(p);
            }

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

            for (PipeInfo p : results) {
                Log.d("CutResult", p.getLength() + "");
            }

            for (int j = totalLength; j > 0; j--) {
                while (true) {
                    ArrayList<PipeInfo> pipes = new ArrayList<PipeInfo>();
                    for (PipeInfo p : results) {
                        PipeInfo pp = new PipeInfo(p.getLength(), p.getCount());
                        pipes.add(pp);
                    }

                    String subs = "";
                    make(0, 0, j, subs);

                    boolean needBreak = true;
                    for (int i = 0; i < results.size(); i++) {
                        if (pipes.get(i).getCount() != results.get(i).getCount()) {
                            needBreak = false;
                            break;
                        }
                    }
                    if (needBreak) {
                        break;
                    }

                }
            }

            String[] pipeArray = sb.toString().split("\r\n");
            pipeParentResultArray = new String[pipeArray.length];
            pipeChildResultArray = new String[pipeParentResultArray.length][];
            Map<String, Integer> resultMap = new HashMap<String, Integer>();
            for (int i = 0; i < pipeArray.length; i++) {
                pipeParentResultArray[i] = "第 " + (i + 1) + " 根切割方案";
                String cutResultStr = pipeArray[i];
                if (cutResultStr.startsWith(",")) {
                    cutResultStr = cutResultStr.substring(1);
                    pipeChildResultArray[i] = cutResultStr.split(",");
                    if (resultMap.get(cutResultStr) != null) {
                        Integer temp = resultMap.get(cutResultStr);
                        resultMap.put(cutResultStr, temp + 1);
                    } else {
                        resultMap.put(cutResultStr, 1);
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(String notices) {
            ll_loading.setVisibility(View.GONE);
            CutResultAdapter cutResultAdapter = new CutResultAdapter(context, pipeParentResultArray,
                                                                     pipeChildResultArray);
            el_cut_resut.setAdapter(cutResultAdapter);

            Log.d("cutResult", sb.toString());

            String pipePrice = Utils.getSharedPerferences(context, "pipePrice", "");
            if (!TextUtils.isEmpty(pipePrice)) {
                Toast.makeText(context,
                               "总共需要：" + totalCount + "根原材料," + "总价：" + Integer.parseInt(pipePrice) * totalCount + " 元",
                               Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "总共需要" + totalCount + "原材料", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public String getPageName() {
        return CutResultActivity.class.getName();
    }

}
