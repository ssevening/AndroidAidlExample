package com.xunl.smsfeishureceiver

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                val context = LocalContext.current
                val sharedPreferences = remember {
                    context.getSharedPreferences("vpn_prefs", Context.MODE_PRIVATE)
                }

                // 1. 定义权限请求启动器（短信）
                val smsPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val isGranted = permissions[Manifest.permission.RECEIVE_SMS] == true
                    if (isGranted) {
                        Toast.makeText(this, "短信权限已授予，拦截功能已就绪", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "请授予短信权限，否则无法转发", Toast.LENGTH_LONG).show()
                    }
                }

                // 2. 页面进入时自动触发短信权限申请
                LaunchedEffect(Unit) {
                    smsPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS
                        )
                    )
                }

                // State
                var urlInput by remember {
                    mutableStateOf(
                        sharedPreferences.getString("trojan_url", "trojan://!%40%23gdwgbngsu!%40%23@www.xunl.net:4433") ?: ""
                    )
                }

                var isVpnRunning by remember { mutableStateOf(TrojanVpnService.isRunning) }
                var vpnServerInfo by remember { mutableStateOf(TrojanVpnService.serverInfo) }

                DisposableEffect(Unit) {
                    TrojanVpnService.setOnStateChangeListener {
                        isVpnRunning = TrojanVpnService.isRunning
                        vpnServerInfo = TrojanVpnService.serverInfo
                    }
                    onDispose {
                        TrojanVpnService.setOnStateChangeListener {}
                    }
                }

                // VPN permission launcher
                val vpnLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        startVpn(urlInput)
                    } else {
                        Toast.makeText(this, "需要VPN授权才能启动", Toast.LENGTH_SHORT).show()
                    }
                }

                // 3. UI 界面布局
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = "短信转发与网络助手",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Card for SMS Receiver
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "短信转发服务",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "正在监听短信并转发至：")
                                Text(
                                    text = "https://www.xunl.net/smsReceiver",
                                    color = Color.Blue,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "转发目标号码：19905843402",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Card for Trojan VPN
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Trojan VPN 配置",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                OutlinedTextField(
                                    value = urlInput,
                                    onValueChange = { urlInput = it },
                                    label = { Text("Trojan 网址 (URL)") },
                                    placeholder = { Text("trojan://password@host:port") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                // Parse result check
                                val config = remember(urlInput) { parseTrojanUri(urlInput) }
                                if (config != null) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(text = "解析结果:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        Text(text = "• 服务器地址: ${config.host}", fontSize = 13.sp)
                                        Text(text = "• 端口: ${config.port}", fontSize = 13.sp)
                                        Text(text = "• 密码: " + "*".repeat(minOf(config.password.length, 8)), fontSize = 13.sp)
                                    }
                                } else if (urlInput.isNotEmpty()) {
                                    Text(
                                        text = "网址格式无效，需以 trojan:// 开头",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 12.sp
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "VPN 状态:", fontSize = 14.sp)
                                        Text(
                                            text = if (isVpnRunning) "正在运行 (${vpnServerInfo})" else "已断开",
                                            fontWeight = FontWeight.Bold,
                                            color = if (isVpnRunning) Color(0xFF4CAF50) else Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            if (isVpnRunning) {
                                                stopVpn()
                                            } else {
                                                if (config == null) {
                                                    Toast.makeText(context, "请先输入有效的 Trojan 网址", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                // Save URL
                                                sharedPreferences.edit().putString("trojan_url", urlInput).apply()
                                                
                                                // Check VPN permission
                                                val vpnPrepareIntent = VpnService.prepare(context)
                                                if (vpnPrepareIntent != null) {
                                                    vpnLauncher.launch(vpnPrepareIntent)
                                                } else {
                                                    startVpn(urlInput)
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isVpnRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(if (isVpnRunning) "停止 VPN" else "保存并启动")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startVpn(url: String) {
        val intent = Intent(this, TrojanVpnService::class.java).apply {
            action = TrojanVpnService.ACTION_START
            putExtra("vpn_url", url)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Trojan VPN 已保存并启动", Toast.LENGTH_SHORT).show()
    }

    private fun stopVpn() {
        val intent = Intent(this, TrojanVpnService::class.java).apply {
            action = TrojanVpnService.ACTION_STOP
        }
        startService(intent)
        Toast.makeText(this, "Trojan VPN 已停止", Toast.LENGTH_SHORT).show()
    }
}
