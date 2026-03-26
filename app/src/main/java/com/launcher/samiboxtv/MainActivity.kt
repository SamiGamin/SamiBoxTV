package com.launcher.samiboxtv

import android.content.Intent
import android.os.Bundle
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

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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