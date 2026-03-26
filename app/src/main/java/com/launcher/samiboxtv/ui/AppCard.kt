package com.launcher.samiboxtv.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import com.launcher.samiboxtv.data.AppInfo

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppCard(
    appInfo: AppInfo,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    isEditing: Boolean = false,
    onMoveLeft: () -> Unit = {},
    onMoveRight: () -> Unit = {},
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onExitEdit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val keyModifier = if (isEditing) {
        modifier.onKeyEvent { event ->
            if (event.type == KeyEventType.KeyDown) {
                when (event.key.nativeKeyCode) {
                    android.view.KeyEvent.KEYCODE_DPAD_LEFT -> { onMoveLeft(); true }
                    android.view.KeyEvent.KEYCODE_DPAD_RIGHT -> { onMoveRight(); true }
                    android.view.KeyEvent.KEYCODE_DPAD_UP -> { onMoveUp(); true }
                    android.view.KeyEvent.KEYCODE_DPAD_DOWN -> { onMoveDown(); true }
                    android.view.KeyEvent.KEYCODE_DPAD_CENTER, 
                    android.view.KeyEvent.KEYCODE_ENTER,
                    android.view.KeyEvent.KEYCODE_BACK -> { onExitEdit(); true }
                    else -> false
                }
            } else if (event.type == KeyEventType.KeyUp) {
               when (event.key.nativeKeyCode) {
                   android.view.KeyEvent.KEYCODE_DPAD_LEFT, 
                   android.view.KeyEvent.KEYCODE_DPAD_RIGHT, 
                   android.view.KeyEvent.KEYCODE_DPAD_UP, 
                   android.view.KeyEvent.KEYCODE_DPAD_DOWN, 
                   android.view.KeyEvent.KEYCODE_DPAD_CENTER, 
                   android.view.KeyEvent.KEYCODE_ENTER,
                   android.view.KeyEvent.KEYCODE_BACK -> true
                   else -> false
               }
            } else false
        }
    } else modifier

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Card(
        onClick = {
            if (isEditing) onExitEdit() else onClick()
        },
        onLongClick = onLongClick,
        interactionSource = interactionSource,
        modifier = keyModifier,
        shape = androidx.tv.material3.CardDefaults.shape(RoundedCornerShape(8.dp)),
        colors = androidx.tv.material3.CardDefaults.colors(
            containerColor = Color(0xFF0B1426),
            focusedContainerColor = Color(0xFF132238)
        ),
        border = androidx.tv.material3.CardDefaults.border(
            focusedBorder = androidx.tv.material3.Border(
                border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFFFF8C00)),
                shape = RoundedCornerShape(8.dp)
            ),
            pressedBorder = androidx.tv.material3.Border(
                border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFF00E5FF)),
                shape = RoundedCornerShape(8.dp)
            )
        )
    ) {
        val editingModifier = if (isEditing) {
            Modifier.background(Color(0xFF00E5FF).copy(alpha = 0.2f))
                .border(3.dp, Color(0xFF00E5FF), RoundedCornerShape(8.dp))
        } else Modifier

        Box(modifier = Modifier.fillMaxSize().then(editingModifier)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp).fillMaxSize()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(appInfo.icon),
                    contentDescription = appInfo.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = appInfo.name,
                    color = if (isFocused || isEditing) Color(0xFFFF8C00) else Color.White,
                    fontFamily = androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Font(com.launcher.samiboxtv.R.font.press_start_2p)),
                    fontWeight = if (isFocused || isEditing) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.then(if (isFocused || isEditing) Modifier.basicMarquee() else Modifier)
                )
            }
            
            if (isEditing) {
                Text("◄ ▲ ▼ ►", color = Color(0xFF00E5FF), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
                Text("OK", color = Color(0xFF00E5FF), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp))
            }
        }
    }
}


