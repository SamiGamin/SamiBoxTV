package com.launcher.samiboxtv.data

import android.content.Context
import android.content.pm.PackageManager

class AppRepository(private val context: Context) {
    fun getInstalledApps(): List<AppInfo> {
        val packageManager = context.packageManager
        val apps = mutableListOf<AppInfo>()

        // Find all installed packages
        val packages = packageManager.getInstalledPackages(0)

        for (packageInfo in packages) {
            val packageName = packageInfo.packageName
            
            // Exclude our own launcher
            if (packageName == context.packageName) continue

            // If the package has a launch intent, it means it can be opened by the user
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                try {
                    val appInfo = packageManager.getApplicationInfo(packageName, 0)
                    val name = packageManager.getApplicationLabel(appInfo).toString()
                    val icon = packageManager.getApplicationIcon(appInfo)
                    
                    apps.add(AppInfo(name, packageName, icon))
                } catch (e: PackageManager.NameNotFoundException) {
                    // Ignore package if it wasn't found
                }
            }
        }

        // Return apps alphabetically sorted
        return apps.sortedBy { it.name.lowercase() }
    }
}

