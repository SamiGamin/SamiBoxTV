package com.launcher.samiboxtv.data

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("samibox_prefs", Context.MODE_PRIVATE)

    fun getHiddenApps(): Set<String> {
        return prefs.getStringSet("hidden_apps", emptySet()) ?: emptySet()
    }

    fun hideApp(packageName: String) {
        val hidden = getHiddenApps().toMutableSet()
        hidden.add(packageName)
        prefs.edit().putStringSet("hidden_apps", hidden).apply()
    }

    fun unhideApp(packageName: String) {
        val hidden = getHiddenApps().toMutableSet()
        hidden.remove(packageName)
        prefs.edit().putStringSet("hidden_apps", hidden).apply()
    }

    fun getCustomOrder(): List<String> {
        val orderStr = prefs.getString("custom_order", "") ?: ""
        return if (orderStr.isEmpty()) emptyList() else orderStr.split(",")
    }

    fun saveCustomOrder(order: List<String>) {
        prefs.edit().putString("custom_order", order.joinToString(",")).apply()
    }
}
