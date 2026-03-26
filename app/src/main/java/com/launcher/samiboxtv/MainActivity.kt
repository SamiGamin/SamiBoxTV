package com.launcher.samiboxtv

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.launcher.samiboxtv.ui.HomeScreen
import com.launcher.samiboxtv.ui.theme.SamiBoxTVTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var memoryTester: MemoryUsageTester
    private lateinit var overlayManager: OverlayWindowManager

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        memoryTester = MemoryUsageTester(this)
        memoryTester.startMonitoring(intervalMillis = 10000)

        overlayManager = OverlayWindowManager(applicationContext)

        // Si no tiene permiso de overlay, lo pide al usuario
        if (!overlayManager.canDrawOverlays()) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        setContent {
            SamiBoxTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    HomeScreen(
                        viewModel = viewModel,
                        onAppSelected = { appInfo ->
                            launchApp(appInfo.packageName)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayManager.hide() // limpiar el overlay al destruir la activity
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: android.view.KeyEvent): Boolean {
        if (event.action == android.view.KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                // Tecla MENU (82) → toggle overlay global (funciona en cualquier app)
                android.view.KeyEvent.KEYCODE_MENU -> {
                    if (overlayManager.canDrawOverlays()) {
                        overlayManager.toggle()
                    } else {
                        Toast.makeText(this, "Activa el permiso de superposición en Ajustes", Toast.LENGTH_LONG).show()
                    }
                    return true
                }
                // Tecla BACK (4) → cerrar overlay si está abierto,
                // si no: bloquear para que no reinicie el launcher
                android.view.KeyEvent.KEYCODE_BACK -> {
                    if (overlayManager.isVisible()) {
                        overlayManager.hide()
                        return true
                    }
                    return true // bloquea el back en pantalla principal
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun launchApp(packageName: String) {
        val intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se pudo abrir la aplicación", Toast.LENGTH_SHORT).show()
        }
    }
}