package com.ssevening.bestcuts.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

public class Utils {

    // check the connect
    public static boolean checkInternetConnection(String strUrl, String strEncoding) {
        int timeOut = 5;
        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible;MSIE 6.0; Windows 2000");
            urlConnection.setRequestProperty("Content-type", "text/html; charset=" + timeOut);
            urlConnection.setConnectTimeout(timeOut * 1000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * �õ���������
     * 
     * @param urlStr
     * @return
     */
    public static String getIntenetData(String urlStr) {
        URL url;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (MalformedURLException e) {
            return sb.toString();
        } catch (IOException e) {
            return sb.toString();
        } catch (Exception e) {
            return sb.toString();
        }

        return sb.toString();
    }

    public static boolean isShowAd(Context paramContext) {
        //return false;
         // ��������˷���ʱ��10��,�Զ�����Ϊ�����ʾ
//        Calendar ca = Calendar.getInstance();
//         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//         try {
//         if (ca.getTimeInMillis() - sdf.parse("2016-04-07 00:00:00").getTime() > 0) {
//         return false;
//         } else {
//         return false;
//         }
//         } catch (ParseException e) {
//         e.printStackTrace();
//         }
         return false;

    }

    public static boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[1-9][0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * �����������������,�����Ѷ�֪ͨID id ���ŷָ�1,2,3,4,5,6
     * 
     * @param paramContext
     * @param paramInt
     */
    public static void setNoticeReaded(Context paramContext, String noticeReaded) {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("net.xunl.smsbulk", 0).edit();
        localEditor.putString("noticeReaded", noticeReaded);
        localEditor.commit();
    }

    /**
     * �õ������С
     * 
     * @param paramContext
     * @return
     */
    public static String getNoticeReaded(Context paramContext) {
        return paramContext.getSharedPreferences("net.xunl.smsbulk", 0).getString("noticeReaded", "");
    }

    /**
     * �ӷ�����������APK�ļ�
     * 
     * @param path
     * @param pd
     * @return
     * @throws Exception
     */
    public static void installApp(String path, ProgressDialog pd, Activity context) {
        // �����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url;
            try {
                url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                // ��ȡ���ļ��Ĵ�С
                pd.setMax(conn.getContentLength());
                InputStream is = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), "update.apk");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    // ��ȡ��ǰ������
                    pd.setProgress(total);
                }
                fos.close();
                bis.close();
                is.close();
                installApk(file, context);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // ��װapk
    public static void installApk(File file, Activity context) {
        Intent intent = new Intent();
        // ִ�ж���
        intent.setAction(Intent.ACTION_VIEW);
        // ִ�е���������
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static int getVersion(Activity context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packInfo.versionCode;
            return versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setSharedPerferences(Context paramContext, String key, String value) {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences(paramContext.getPackageName(), 0).edit();
        localEditor.putString(key, value);
        localEditor.commit();
    }

    public static String getSharedPerferences(Context paramContext, String key, String defaultValue) {
        return paramContext.getSharedPreferences(paramContext.getPackageName(), 0).getString(key, defaultValue);
    }

}
