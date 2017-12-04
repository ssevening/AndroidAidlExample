package com.ssevening.www.module.shopcart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pan on 2017/12/1.
 */

public class HttpUtils {

    public static String getHtmlContent(URL url, String encode) {
        StringBuffer contentBuffer = new StringBuffer();

        int responseCode = -1;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// IE代理进行下载
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
            // 获得网页返回信息码
            responseCode = con.getResponseCode();
            if (responseCode == -1) {
                System.out.println(url.toString() + " : connection is failure...");
                con.disconnect();
                return null;
            }
            if (responseCode >= 400) // 请求失败
            {
                System.out.println("请求失败:get response code: " + responseCode);
                con.disconnect();
                return null;
            }

            InputStream inStr = con.getInputStream();
            InputStreamReader istreamReader = new InputStreamReader(inStr, encode);
            BufferedReader buffStr = new BufferedReader(istreamReader);

            String str = null;
            while ((str = buffStr.readLine()) != null)
                contentBuffer.append(str);
            inStr.close();
        } catch (IOException e) {
            e.printStackTrace();
            contentBuffer = null;
            System.out.println("error: " + url.toString());
        } finally {
            con.disconnect();
        }
        return contentBuffer.toString();
    }

    public static String getHtmlContent(String url, String encode) {
        if (!url.toLowerCase().startsWith("http://")) {
            url = "http://" + url;
        }
        try {
            URL rUrl = new URL(url);
            return getHtmlContent(rUrl, encode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
