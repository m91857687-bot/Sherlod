package com.example.ui.apps

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class AppItem(
    val name: String,
    val packageName: String,
    val size: Long,
    val sourceDir: String,
    var isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<AppItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val appItems = installedApps.filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.map { info ->
                val file = File(info.sourceDir)
                AppItem(
                    name = pm.getApplicationLabel(info).toString(),
                    packageName = info.packageName,
                    size = file.length(),
                    sourceDir = info.sourceDir
                )
            }.sortedBy { it.name }
            apps = appItems
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("التطبيقات") },
                navigationIcon = {
                    // Back button placeholder
                }
            )
        },
        bottomBar = {
            val selectedCount = apps.count { it.isSelected }
            if (selectedCount > 0) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("تم تحديد $selectedCount تطبيق")
                        Button(onClick = { /* Send */ }) {
                            Text("إرسال المحدد")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(apps) { app ->
                    AppRow(app = app, onCheckedChange = { checked ->
                        apps = apps.map { if (it.packageName == app.packageName) it.copy(isSelected = checked) else it }
                    })
                }
            }
        }
    }
}

@Composable
fun AppRow(app: AppItem, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Simple placeholder for icon
        Box(modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.name, style = MaterialTheme.typography.bodyLarge)
            Text("${app.size / (1024 * 1024)} MB", style = MaterialTheme.typography.bodySmall)
        }
        Checkbox(
            checked = app.isSelected,
            onCheckedChange = onCheckedChange
        )
    }
}
