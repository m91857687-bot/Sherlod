package com.example.ui.history

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
import com.example.model.Transfer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = (context.applicationContext as ShareLoadApplication).container.shareLoadRepository
    val transfers by repository.allTransfers.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("سجل النقل") })
        }
    ) { padding ->
        if (transfers.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("لا يوجد سجل نقل")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(transfers) { transfer ->
                    TransferHistoryRow(transfer = transfer)
                }
            }
        }
    }
}

@Composable
fun TransferHistoryRow(transfer: Transfer) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            val direction = if (transfer.isSender) "📤 أُرسل إلى" else "📥 أُستقبل من"
            Text("$direction ${transfer.deviceId}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("حجم النقل: ${transfer.totalBytes / (1024 * 1024)} MB", style = MaterialTheme.typography.bodyMedium)
            Text("حالة: ${transfer.status}", style = MaterialTheme.typography.bodyMedium)
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(transfer.timestamp))
            Text("التاريخ: $date", style = MaterialTheme.typography.bodySmall)
        }
    }
}
