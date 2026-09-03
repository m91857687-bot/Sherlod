package com.example.ui.devices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ShareLoadApplication
import com.example.model.Device
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = (context.applicationContext as ShareLoadApplication).container.shareLoadRepository
    val devices by repository.allDevices.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("أجهزتي") })
        }
    ) { padding ->
        if (devices.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("لم تتصل بأي جهاز بعد")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(devices) { device ->
                    DeviceRow(device = device)
                }
            }
        }
    }
}

@Composable
fun DeviceRow(device: Device) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📱 ${device.name}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(device.lastConnected))
            Text("آخر اتصال: $date", style = MaterialTheme.typography.bodyMedium)
            Text("أُرسل: ${device.totalSentBytes / (1024 * 1024)} MB", style = MaterialTheme.typography.bodySmall)
            Text("أُستقبل: ${device.totalReceivedBytes / (1024 * 1024)} MB", style = MaterialTheme.typography.bodySmall)
        }
    }
}
