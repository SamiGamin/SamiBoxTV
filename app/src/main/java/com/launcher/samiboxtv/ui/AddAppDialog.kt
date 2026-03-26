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
                .background(Color(0xFF0B1426), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Añadir / Restaurar Aplicaciones Ocultas",
                    color = Color(0xFF00E5FF),
                    fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.launcher.samiboxtv.R.font.press_start_2p)),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (hiddenApps.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay aplicaciones ocultas.", color = Color.Gray, fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.launcher.samiboxtv.R.font.press_start_2p)))
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
