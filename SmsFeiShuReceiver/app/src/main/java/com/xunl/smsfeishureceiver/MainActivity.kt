package com.xunl.smsfeishureceiver

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xunl.smsfeishureceiver.ui.theme.SmsFeiShuReceiverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmsFeiShuReceiverTheme {
                // 1. 定义权限请求启动器
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val isGranted = permissions[Manifest.permission.RECEIVE_SMS] == true
                    if (isGranted) {
                        Toast.makeText(this, "权限已授予，拦截功能已就绪", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "请授予短信权限，否则无法转发", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                // 2. 页面进入时自动触发权限申请
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS
                        )
                    )
                }

                // 3. UI 界面布局
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "短信转发助手",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "正在监听短信并转发至：",
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = "https://www.xunl.net/smsReceiver",
                            color = Color.Blue,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        Text(
                            text = "转发号码：19905843402",
                            modifier = Modifier.padding(top = 10.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
