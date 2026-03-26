package com.launcher.samiboxtv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.launcher.samiboxtv.data.AppInfo
import kotlinx.coroutines.delay

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppContextMenu(
    appInfo: AppInfo,
    onDismiss: () -> Unit,
    onMove: () -> Unit,
    onHide: () -> Unit
) {
    var buttonsEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400)
        buttonsEnabled = true
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color(0xFF0B1426), RoundedCornerShape(12.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Opciones para: ${appInfo.name}", 
                    color = Color(0xFF00E5FF),
                    fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.launcher.samiboxtv.R.font.press_start_2p)),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                
                Button(
                    onClick = { 
                        onMove()
                        onDismiss()
                    },
                    enabled = buttonsEnabled,
                    colors = androidx.tv.material3.ButtonDefaults.colors(
                        containerColor = Color(0xFF132238),
                        focusedContainerColor = Color(0xFFFF8C00),
                        focusedContentColor = Color.White
                    )
                ) {
                    Text("Mover de Posición", fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.launcher.samiboxtv.R.font.press_start_2p)))
                }
                
                Button(
                    onClick = { 
                        onHide()
                        onDismiss()
                    },
                    enabled = buttonsEnabled,
                    colors = androidx.tv.material3.ButtonDefaults.colors(
                        containerColor = Color(0xFF132238),
                        focusedContainerColor = Color(0xFFFF8C00),
                        focusedContentColor = Color.White
                    )
                ) {
                    Text("Ocultar Aplicación", fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.launcher.samiboxtv.R.font.press_start_2p)))
                }
            }
        }
    }
}


