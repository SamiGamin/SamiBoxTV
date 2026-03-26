package com.launcher.samiboxtv.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.launcher.samiboxtv.R

// ─── Model ───────────────────────────────────────────────────────────────────

data class ScannedApp(
    val name: String,
    val packageName: String,
    val isSystemApp: Boolean,
    val isBloatware: Boolean
)

// ─── Scanner ─────────────────────────────────────────────────────────────────

fun scanAllApps(context: Context): List<ScannedApp> {
    val pm = context.packageManager
    val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

    val knownBloatware = listOf(
        // ── Launchers y gestores chinos ──
        "com.hkw.simplelauncher",       // AllApp - launcher chino de fábrica
        "com.wolf.google.lm",           // Launcher Manager chino
        "com.luancher.apps",            // More Apps - tienda china falsa

        // ── PELIGROSOS: Acceso remoto disfrazado ──
        "com.hcy.remoteAceess",         // "GoogleMail" falso - acceso remoto chino
        "com.hcy.remoteAceessdesk",     // Mouse assisted - mismo autor sospechoso

        // ── Instaladores y actualizadores chinos ──
        "com.www.intallapp",            // intallApp - instalador no oficial
        "com.abupdate.fota_demo_iot",   // OTA update chino (ABUpdate)

        // ── Limpiadores/Boosters falsos ──
        "com.charon.rocketfly",         // CleanUp - cleaner chino

        // ── Apps de fábrica Rockchip ──
        "com.rockchip.devicetest",      // DeviceTest - pruebas de fábrica
        "com.rockchip.mediacenter",     // DLNA chino
        "com.rockchips.mediacenter",    // Media Center (typo de rockchip)
        "com.hcy.firstbt",              // firstbt - app china desconocida

        // ── Apps de streaming chinas dudosas ──
        "com.android.mgstv",            // Xuper

        // ── Inútiles en TV ──
        "com.android.smart.terminal",   // 工厂测试 - test de fábrica chino
        "com.android.inputmethod.pinyin", // Teclado chino Pinyin

        // ── Legacy (lista anterior) ──
        "com.android.browser",
        "com.quick.appstore",
        "com.rockchip.weather",
        "com.speedbooster.cleaner",
        "com.rockchip.launcher",
        "com.example.weather",
        "com.allwinnertech.miracast"
    )

    val scannedList = mutableListOf<ScannedApp>()

    for (appInfo in packages) {
        val pkg = appInfo.packageName
        val appName = pm.getApplicationLabel(appInfo).toString()
        val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val isBloatware = knownBloatware.contains(pkg)
        scannedList.add(ScannedApp(appName, pkg, isSystemApp, isBloatware))
    }

    val sorted = scannedList.sortedWith(
        compareBy({ !it.isBloatware }, { it.isSystemApp }, { it.name.lowercase() })
    )

    // ─── LOGCAT: filtra por tag "SAMIBOX_SCANNER" en Android Studio ──────────
    android.util.Log.d("SAMIBOX_SCANNER", "════════════════════════════════════════")
    android.util.Log.d("SAMIBOX_SCANNER", "  TOTAL APPS INSTALADAS: ${sorted.size}")
    android.util.Log.d("SAMIBOX_SCANNER", "════════════════════════════════════════")
    sorted.forEach { app ->
        val tipo = when {
            app.isBloatware  -> "⚠ BLOATWARE"
            !app.isSystemApp -> "✔ USER APP "
            else             -> "  SYS APP  "
        }
        android.util.Log.d(
            "SAMIBOX_SCANNER",
            "$tipo | ${app.name.padEnd(30)} | ${app.packageName}"
        )
    }
    android.util.Log.d("SAMIBOX_SCANNER", "════════════════════════════════════════ FIN")

    return sorted
}

// ─── UI: pantalla principal del log ──────────────────────────────────────────

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SystemMonitorLog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scannedApps: List<ScannedApp> by remember { mutableStateOf(scanAllApps(context)) }
    val pressStartFont = FontFamily(Font(R.font.press_start_2p))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030A03))
            .padding(32.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "> SYSTEM INFO LOG (PRESS BACK TO EXIT)",
                color = Color(0xFF00FF00),
                fontFamily = pressStartFont,
                fontSize = 14.sp
            )
            Text(
                text = "${scannedApps.size} APPS INSTALLED",
                color = Color.LightGray,
                fontFamily = pressStartFont,
                fontSize = 12.sp
            )
        }

        // Lista
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = scannedApps, key = { it.packageName }) { app ->
                AppLogItem(app = app, pressStartFont = pressStartFont, context = context)
            }
        }
    }
}

// ─── UI: ítem individual con foco bien visible ───────────────────────────────

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppLogItem(
    app: ScannedApp,
    pressStartFont: FontFamily,
    context: Context
) {
    val textColor = when {
        app.isBloatware  -> Color(0xFFFF4444)
        !app.isSystemApp -> Color(0xFF00E5FF)
        else             -> Color(0xFF00CC00)
    }

    val focusBorderColor = when {
        app.isBloatware  -> Color(0xFFFF4444)
        !app.isSystemApp -> Color(0xFF00E5FF)
        else             -> Color(0xFF00FF00)
    }

    val prefix = when {
        app.isBloatware  -> "[BLOATWARE]"
        !app.isSystemApp -> "[USER APP] "
        else             -> "[SYS APP]  "
    }

    Card(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:${app.packageName}")
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        shape = CardDefaults.shape(RoundedCornerShape(6.dp)),
        colors = CardDefaults.colors(
            containerColor = Color(0xFF0A150A),
            focusedContainerColor = Color(0xFF1A2E1A)
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(3.dp, focusBorderColor),
                shape = RoundedCornerShape(6.dp)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra lateral de color indicador
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .background(textColor, shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "$prefix ${app.name}  ->  ${app.packageName}",
                color = textColor,
                fontFamily = pressStartFont,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
