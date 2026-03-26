package com.launcher.samiboxtv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.launcher.samiboxtv.data.AppInfo
import com.launcher.samiboxtv.data.AppPreferences
import com.launcher.samiboxtv.data.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)
    private val preferences = AppPreferences(application)

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _hiddenApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val hiddenApps: StateFlow<List<AppInfo>> = _hiddenApps.asStateFlow()

    init {
        loadApps()
    }

    private var cachedInstalledApps: List<AppInfo>? = null

    private fun loadApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val installedApps = cachedInstalledApps ?: repository.getInstalledApps().also {
                cachedInstalledApps = it
            }
            val hiddenSet = preferences.getHiddenApps()

            val (hidden, visible) = installedApps.partition { it.packageName in hiddenSet }

            val customOrder = preferences.getCustomOrder()
            val orderedVisible = visible.sortedWith { a, b ->
                val indexA = customOrder.indexOf(a.packageName)
                val indexB = customOrder.indexOf(b.packageName)
                
                when {
                    indexA != -1 && indexB != -1 -> indexA.compareTo(indexB)
                    indexA != -1 -> -1
                    indexB != -1 -> 1
                    else -> a.name.lowercase().compareTo(b.name.lowercase())
                }
            }

            _apps.value = orderedVisible
            _hiddenApps.value = hidden.sortedBy { it.name.lowercase() }
        }
    }

    fun hideApp(app: AppInfo) {
        preferences.hideApp(app.packageName)
        loadApps()
    }

    fun unhideApp(app: AppInfo) {
        preferences.unhideApp(app.packageName)
        loadApps()
    }

    fun moveApp(app: AppInfo, direction: Int) {
        val currentList = _apps.value.toMutableList()
        val index = currentList.indexOf(app)
        if (index == -1) return

        val newIndex = index + direction
        if (newIndex in 0 until currentList.size) {
            currentList.removeAt(index)
            currentList.add(newIndex, app)
            
            preferences.saveCustomOrder(currentList.map { it.packageName })
            _apps.value = currentList.toList()
        }
    }
}

