package com.example.ui.discovery

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(isSender: Boolean, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isSender) "إرسال" else "استقبال") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(if (isSender) "🔎 البحث عن أجهزة قريبة..." else "في انتظار الاتصال...")
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { /* Navigate to Transfer */ }) {
                Text("محاكاة اتصال ناجح")
            }
        }
    }
}
