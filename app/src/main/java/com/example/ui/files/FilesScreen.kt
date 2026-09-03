package com.example.ui.files

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(onBack: () -> Unit) {
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        selectedFiles = uris
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("الملفات") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { launcher.launch(arrayOf("*/*")) }) {
                Text("اختر ملفات")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("تم تحديد ${selectedFiles.size} ملف")
            
            if (selectedFiles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Send */ }) {
                    Text("إرسال المحدد")
                }
            }
        }
    }
}
