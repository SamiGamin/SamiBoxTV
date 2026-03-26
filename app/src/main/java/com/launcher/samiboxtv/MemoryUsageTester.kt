package com.launcher.samiboxtv

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MemoryUsageTester(private val context: Context) {
    private val TAG = "MemoryUsageTester"
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    fun startMonitoring(intervalMillis: Long = 5000) {
        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                logMemoryUsage()
                delay(intervalMillis)
            }
        }
    }

    private fun logMemoryUsage() {
        Log.d(TAG, "--- Memory Usage Report ---")
        
        // 1. Información General de Memoria
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        Log.d(TAG, "Total RAM: ${memoryInfo.totalMem / (1024 * 1024)} MB")
        Log.d(TAG, "Available RAM: ${memoryInfo.availMem / (1024 * 1024)} MB")
        Log.d(TAG, "Low Memory Threshold: ${memoryInfo.threshold / (1024 * 1024)} MB")
        Log.d(TAG, "Is Low Memory: ${memoryInfo.lowMemory}")

        // 2. Procesos en ejecución y su consumo (Limitado en Android moderno por privacidad)
        // Nota: En Android 5.1+ getRunningAppProcesses solo devuelve el proceso de tu app y servicios en segundo plano.
        val runningProcesses = activityManager.runningAppProcesses
        if (runningProcesses != null) {
            for (processInfo in runningProcesses) {
                val pids = intArrayOf(processInfo.pid)
                val processMemoryInfo = activityManager.getProcessMemoryInfo(pids)
                
                for (info in processMemoryInfo) {
                    val totalPss = info.totalPss / 1024 // En MB
                    Log.d(TAG, "Process: ${processInfo.processName} (PID: ${processInfo.pid}) - RAM Usage: $totalPss MB")
                }
            }
        } else {
            Log.d(TAG, "No se pudieron obtener los procesos en ejecución (Restricciones de Android)")
        }

        // 3. Servicios en ejecución
        @Suppress("DEPRECATION")
        val runningServices = activityManager.getRunningServices(100)
        for (service in runningServices) {
            Log.d(TAG, "Running Service: ${service.service.className} (Package: ${service.service.packageName})")
        }

        Log.d(TAG, "---------------------------")
    }
}
