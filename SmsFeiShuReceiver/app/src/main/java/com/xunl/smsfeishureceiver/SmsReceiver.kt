package com.xunl.smsfeishureceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            // 修正点 1：必须先获取 bundle
            val bundle = intent.extras
            if (bundle != null) {
                try {
                    val pdus = bundle.get("pdus") as Array<*>
                    val format = bundle.getString("format") // 获取短信格式 (gsm/cdma)

                    for (pdu in pdus) {
                        // 修正点 2：使用带 format 的方法
                        val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                        val content = sms.displayMessageBody

                        Log.d("SmsReceiver", "收到短信内容: $content")
                        // 触发发送逻辑
                        sendPostRequest(content)
                    }
                } catch (e: Exception) {
                    Log.e("SmsReceiver", "解析短信失败: ${e.message}")
                }
            }
        }
    }

    private fun sendPostRequest(smsContent: String) {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val url = URL("https://work.longwellfans.com/smsReceiver")
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                // 修正点 3：正确的字符串拼接和 URL 编码
                val postData = "phoneNum=19905843402&smsContent=" + URLEncoder.encode(smsContent, "UTF-8")

                connection.outputStream.use { it.write(postData.toByteArray()) }

                val responseCode = connection.responseCode
                Log.d("SmsReceiver", "HTTP 响应码: $responseCode")
            } catch (e: Exception) {
                Log.e("SmsReceiver", "网络请求失败: ${e.message}")
            } finally {
                connection?.disconnect()
            }
        }
    }
}
