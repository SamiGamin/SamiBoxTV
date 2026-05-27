package com.launcher.samiboxtv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.Text
import com.launcher.samiboxtv.MainViewModel
import com.launcher.samiboxtv.ui.theme.*
import androidx.compose.foundation.border

@Composable
fun AddAppDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val hiddenApps by viewModel.hiddenApps.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.85f)
                .background(CyberCard, RoundedCornerShape(16.dp))
                .border(1.5.dp, CyberCyan, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Añadir / Restaurar Aplicaciones Ocultas",
                    color = CyberCyan,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (hiddenApps.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay aplicaciones ocultas.", color = Color.Gray)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(hiddenApps, key = { it.packageName }) { appInfo ->
                             AppCard(
                                 appInfo = appInfo,
                                 onClick = {
                                     viewModel.unhideApp(appInfo)
                                 }
                             )
                        }
                    }
                }
            }
        }
    }
}
