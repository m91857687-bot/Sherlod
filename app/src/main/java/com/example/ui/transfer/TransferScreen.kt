package com.example.ui.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("جاري النقل...") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("إرسال إلى: 📱 Galaxy A55", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(progress = 0.74f, modifier = Modifier.fillMaxWidth().height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("3.7 GB / 5 GB")
                Text("74%")
            }
            Text("⚡ 82.4 MB/s", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text("متبقي تقريبًا: 16 ثانية", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    TransferFileRow("video.mp4", 3700000000L, 5000000000L, "⚡ 82.4 MB/s", "TRANSFERRING")
                }
                item {
                    TransferFileRow("game.apk", 800000000L, 800000000L, "", "COMPLETED")
                }
                item {
                    TransferFileRow("photos.zip", 0L, 600000000L, "", "QUEUED")
                }
            }
        }
    }
}

@Composable
fun TransferFileRow(name: String, transferred: Long, total: Long, speed: String, status: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("📦 $name", style = MaterialTheme.typography.titleMedium)
                Text("${transferred / (1024 * 1024)} MB / ${total / (1024 * 1024)} MB", style = MaterialTheme.typography.bodyMedium)
                if (speed.isNotEmpty()) {
                    Text(speed, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
            }
            when (status) {
                "TRANSFERRING" -> Button(onClick = { /* Pause */ }) { Text("⏸ إيقاف") }
                "COMPLETED" -> Text("✅ مكتمل", color = MaterialTheme.colorScheme.secondary)
                "QUEUED" -> Text("⏳ في الانتظار")
                "PAUSED" -> Button(onClick = { /* Resume */ }) { Text("▶ استكمال") }
            }
        }
    }
}
